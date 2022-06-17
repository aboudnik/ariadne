package org.apache.ariadne.execution;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ariadne.Builder;
import org.apache.ariadne.DAG;
import org.apache.ariadne.Node;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Alexandre_Boudnik
 * @since 06/13/2018
 */
public class ForkJoinTest {

    private static final int MILLIS = 200;
    private static final String EXPECTED = "1.1.1 1.1.2 1.1 1.2.1 1.2.2 1.2.3 1.2.4 1.2 1.3 1";

    static class TreeNode implements Node<String> {
        private final String n;
        private final Collection<Node<String>> children;

        @Override
        public Collection<Node<String>> getChildren() {
            return children;
        }

        @Override
        public String payload() {
            return n;
        }

        TreeNode(String n, TreeNode... children) {
            this.n = n;
            this.children = Arrays.asList(children);
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

    private static String sleep(Builder<String> function, Node<String> node) {
        try {
            Thread.sleep(MILLIS);
        } catch (InterruptedException ignored) {
        }
        return function.apply(node);
    }

    private final BiFunction<String, String, String> combiner = (s1, s2) -> (s1 == null ? "" : s1 + " ") + s2;

    private final Builder<String> builder = node -> sleep(Node::payload, node);

    @Test
    public void recursive() {
        run(new DAG<>(root, builder, combiner));
    }

    @Test
    public void noCombiner() {
        assertNull(new ForkJoinPool().invoke(new DAG<>(root, builder)));
    }

    @Test
    public void recursiveOnIgnite() {
        try (Ignite ignite = Ignition.start(new IgniteConfiguration())) {
            run(new DAG<>(root, builder, combiner, (f, n) -> ignite.compute().apply(f::apply, n)));
        }
    }

    private void run(RecursiveTask<String> task) {
        for (int i : new int[]{1, 2, 4, 8}) {
            Stopwatch sw = new Stopwatch();

            String actual = new ForkJoinPool(i).invoke(task);

            System.out.printf("%d thread(s) %d secs %s%n", i, sw.seconds(), actual);
            assertEquals(EXPECTED, actual);

            task.reinitialize();
        }
    }

    @Test
    public void recursiveCF() {
        runCompletableFuture(new DAG<>(root, builder, combiner));
    }

    private void runCompletableFuture(DAG<String> dagCF) {
        for (int i : new int[]{1, 2, 4, 8}) {
            Stopwatch sw = new Stopwatch();
            String actual = dagCF.compute(new ForkJoinPool(i));
            System.out.printf("%d thread(s) %d secs %s%n", i, sw.seconds(), actual);
        }
    }

    @Test
    public void noCombinerCompletableFuture() {
        assertNull(new DAG<>(root, builder).compute(new ForkJoinPool(1)));
    }

    @Test
    public void recursiveOnIgniteCompletableFuture() {
        try (Ignite ignite = Ignition.start(new IgniteConfiguration())) {
            runCompletableFuture(
                    new DAG<>(root, builder, combiner,
                            (f, n) -> ignite.compute().apply(f::apply, n)));
        }
    }

    private static class Stopwatch {
        long start = System.currentTimeMillis();

        long mills() {
            return System.currentTimeMillis() - start;
        }

        long seconds() {
            return mills() / MILLIS;
        }
    }
}
