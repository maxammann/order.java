package order;

/**
 * Represents a NaughtyTranslate
 */
public class NaughtyTranslate implements Translate {
    @Override
    public String getTranslation(String name) {
        return name;
    }
}
