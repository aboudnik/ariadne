package org.boudnik.ariadne;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

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

    @Test
    public void recursive() {
        String expected = " 1.1.1 1.1.2 1.1 1.2.1 1.2.2 1.2.3 1.2.4 1.2 1.3 1";
        for (int i : new int[]{1, 2, 4, 8}) {
            sw = new Stopwatch();
            String actual = new ForkJoinPool(i).invoke(new Task(root));
            System.out.printf("%d thread(s) %d secs %s%n", i, sw.seconds(), actual);
            assertEquals(expected, actual);
        }
    }

    static class Task extends RecursiveTask<String> {
        private final Node node;

        Task(Node node) {
            this.node = node;
        }

        @Override
        protected String compute() {
            System.out.println(Thread.currentThread() + " " + sw.seconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            Task[] tasks = new Task[node.children.length];
            for (int i = 0; i < node.children.length; i++)
                (tasks[i] = new Task(node.children[i])).fork();
            StringBuilder s = new StringBuilder();
            for (Task task : tasks)
                s.append(task.join());
            return s + " " + node.n;
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
