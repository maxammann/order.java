package order.token;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a SpaceDelimiter
 */
public class SpaceDelimiter implements Delimiter {

    private static final Pattern pattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");


    @Override
    public String[] delimit(String input) {
        List<String> output = new ArrayList<String>();
        Matcher m = pattern.matcher(input);

        while (m.find()) {
            output.add(m.group(1).replace("\"", ""));
        }

        return output.toArray(new String[output.size()]);
    }
}
