package order;

import order.sender.Sender;

import java.util.Deque;

/**
 *
 */
public interface CommandIterator<S extends Sender> {

    void iterate(Command<S> command, Deque<Command<S>> stack);
}
