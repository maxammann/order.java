package order.sender;

import com.google.common.base.Optional;

/**
 * Represents a SenderProvider
 */
public interface SenderProvider {

    Optional<Sender> getSender(String name);
}
