package org.boudnik.ariadne;

import org.apache.ignite.lang.IgniteBiClosure;
import org.apache.ignite.lang.IgniteClosure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author Alexandre_Boudnik
 * @since 07/06/2018
 */
public class TreeTask<T> extends RecursiveTask<T> {

    private final Node<T> node;

    private final IgniteClosure<Node<T>, T> function;
    private final IgniteBiClosure<T, T, T> combiner;

    TreeTask(Node<T> root, IgniteClosure<Node<T>, T> function, IgniteBiClosure<T, T, T> combiner) {
        this.node = root;
        this.function = function;
        this.combiner = combiner;
    }

    @Override
    protected T compute() {
        Node<T>[] children = node.getChildren();
        List<TreeTask<T>> tasks = new ArrayList<>();

        // initiate dependencies calculation
        for (int i = 0; i < children.length; i++) {
            TreeTask<T> element = new TreeTask<>(children[i], function, combiner);
            tasks.add(i, element);
            element.fork();
        }

        // collect results from dependencies
        T s = null;
        for (TreeTask<T> task : tasks) {
            s = combiner.apply(s, task.join());
        }

        // apply the function and return combined result
        return combiner.apply(s, function.apply(node));
    }

    public interface Node<P> {
        Node<P>[] getChildren();

        P getN();
    }
}
