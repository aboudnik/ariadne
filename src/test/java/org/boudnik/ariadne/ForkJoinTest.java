package org.boudnik.ariadne;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteClosure;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexandre_Boudnik
 * @since 06/13/2018
 */
public class ForkJoinTest {

    private static Stopwatch sw;

    static class Node {
        final String n;
        final Node[] children;

        Node(String n, Node... children) {
            this.n = n;
            this.children = children;
        }
    }

    private Node root =
            new Node("1",
                    new Node("1.1",
                            new Node("1.1.1"),
                            new Node("1.1.2")
                    ),
                    new Node("1.2",
                            new Node("1.2.1"),
                            new Node("1.2.2"),
                            new Node("1.2.3"),
                            new Node("1.2.4")
                    ),
                    new Node("1.3")

            );

    private IgniteClosure<Node, String> builder = node -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        return node.n;
    };

    @Test
    public void recursive() {
        run(() -> new Tree(builder, root));
    }

    @Test
    public void recursiveOnIgnite() {
        try (Ignite ignite = Ignition.start()) {
            run(() -> new Tree(node -> ignite.compute().apply(builder, node), root));
        }
    }

    private void run(Supplier<RecursiveTask<String>> supplier) {
        String expected = " 1.1.1 1.1.2 1.1 1.2.1 1.2.2 1.2.3 1.2.4 1.2 1.3 1";
        for (int i : new int[]{1, 2, 4, 8}) {
            sw = new Stopwatch();
            String actual = new ForkJoinPool(i).invoke(supplier.get());
            System.out.printf("%d thread(s) %d secs %s%n", i, sw.seconds(), actual);
            assertEquals(expected, actual);
        }
    }

    static class Tree extends RecursiveTask<String> {

        private final Node node;
        private final IgniteClosure<Node, String> function;

        Tree(IgniteClosure<Node, String> function, Node node) {
            this.node = node;
            this.function = function;
        }

        @Override
        protected String compute() {
            System.out.printf("%s %2d -> %s%n", Thread.currentThread(), sw.seconds(), node.n);
            Tree[] tasks = new Tree[node.children.length];

            // initiate dependencies calculation
            for (int i = 0; i < node.children.length; i++)
                (tasks[i] = new Tree(function, node.children[i])).fork();

            // collect results from dependencies
            StringBuilder s = new StringBuilder();
            for (Tree task : tasks)
                s.append(task.join());

            // and apply the function
            s.append(" ").append(function.apply(node));

            System.out.printf("%s %2d <- %s%n", Thread.currentThread(), sw.seconds(), node.n);
            return s.toString();
        }

    }

    static class Stopwatch {
        long start = System.currentTimeMillis();

        long mills() {
            return System.currentTimeMillis() - start;
        }

        long seconds() {
            return mills() / 1000;
        }

    }
}
