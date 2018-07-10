package org.boudnik.ariadne;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteBiClosure;
import org.apache.ignite.lang.IgniteClosure;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Alexandre_Boudnik
 * @since 06/13/2018
 */
public class ForkJoinTest {

    private static final int MILLIS = 200;

    static class TreeNode implements TreeTask.Node<String> {
        private final String n;
        private final TreeNode[] children;

        @Override
        public TreeNode[] getChildren() {
            return children;
        }

        @Override
        public String getN() {
            return n;
        }

        TreeNode(String n, TreeNode... children) {
            this.n = n;
            this.children = children;
        }
    }

//    private static Stopwatch sw;

    private TreeNode root =
            new TreeNode("1",
                    new TreeNode("1.1",
                            new TreeNode("1.1.1"),
                            new TreeNode("1.1.2")
                    ),
                    new TreeNode("1.2",
                            new TreeNode("1.2.1"),
                            new TreeNode("1.2.2"),
                            new TreeNode("1.2.3"),
                            new TreeNode("1.2.4")
                    ),
                    new TreeNode("1.3")

            );

    private IgniteClosure<TreeTask.Node<String>, String> builder = node -> {
        try {
            Thread.sleep(MILLIS);
        } catch (InterruptedException ignored) {
        }
        return node.getN();
    };

    private IgniteBiClosure<String, String, String> combiner = (s1, s2) -> (s1 == null ? "" : (s1 + " ")) + s2;


    @Test
    public void recursive() {
        run(() -> new TreeTask<>(root, builder, combiner));
    }

    @Test
    public void noCombiner() {
        assertNull(new ForkJoinPool(1).invoke(new TreeTask<>(root, builder, (s1, s2) -> null)));
    }

    @Test
    public void recursiveOnIgnite() {
        try (Ignite ignite = Ignition.start()) {
            run(() -> new TreeTask<>(root, node -> ignite.compute().apply(builder, node), combiner));
        }
    }

    private void run(Supplier<RecursiveTask<String>> supplier) {
        String expected = "1.1.1 1.1.2 1.1 1.2.1 1.2.2 1.2.3 1.2.4 1.2 1.3 1";
        for (int i : new int[]{1, 2, 4, 8}) {
            Stopwatch sw = new Stopwatch();
            String actual = new ForkJoinPool(i).invoke(supplier.get());
            System.out.printf("%d thread(s) %d secs %s%n", i, sw.seconds(), actual);
            assertEquals(expected, actual);
        }
    }

    static class Stopwatch {

        long start = System.currentTimeMillis();

        long mills() {
            return System.currentTimeMillis() - start;
        }

        long seconds() {
            return mills() / MILLIS;
        }

    }
}
