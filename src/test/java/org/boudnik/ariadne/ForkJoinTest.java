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
        String invoke1 = new ForkJoinPool(1).invoke(new Task(root));
        System.out.println("invoke1 = " + invoke1);
        String invoke2 = new ForkJoinPool(2).invoke(new Task(root));
        System.out.println("invoke2 = " + invoke2);
        assertEquals(invoke1, invoke2);
    }

    static class Task extends RecursiveTask<String> {
        private final Node node;

        Task(Node node) {
            this.node = node;
        }

        @Override
        protected String compute() {
            System.out.println(Thread.currentThread());
            Task[] tasks = new Task[node.children.length];
            for (int i = 0; i < node.children.length; i++)
                (tasks[i] = new Task(node.children[i])).fork();
            StringBuilder s = new StringBuilder();
            for (Task task : tasks)
                s.append(task.join());
            return s + " " + node.n;
        }
    }
}
