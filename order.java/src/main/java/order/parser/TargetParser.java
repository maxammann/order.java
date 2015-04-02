package order.parser;

import com.google.common.base.Optional;
import order.CommandContext;
import order.ParsingException;
import order.sender.Sender;
import order.sender.SenderProvider;

import javax.inject.Inject;

/**
 * Represents a TargetParser
 */
public class TargetParser implements ArgumentParser<Sender> {

    private final SenderProvider senderProvider;

    @Inject
    public TargetParser(SenderProvider senderProvider) {
        this.senderProvider = senderProvider;
    }

    @Override
    public Sender parse(String input, CommandContext<?> ctx) throws ParsingException {
        Optional<Sender> sender = senderProvider.getSender(input);
        if (!sender.isPresent()) {
            throw new ParsingException("Sender not found!", ctx);
        }

        return sender.get();
    }
}
