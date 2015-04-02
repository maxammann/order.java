package order.format.pagination;


import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Represents a Paginator
 */
public class DefaultPaginator implements Paginator {

    private final int perPage;

    @Inject
    public DefaultPaginator(@Named("entries-per-page") int perPage) {
        this.perPage = perPage;
    }

    @Override
    public <T> PaginateResult<T> paginate(List<T> input, int page) {
        --page;

        int maxPages = input.size() / perPage;

        // If the content divides perfectly, eg (18 entries, and 9 per page)
        // we end up with a blank page this handles this case
        if (input.size() % perPage == 0) {
            maxPages--;
        }

        page = Math.max(0, Math.min(page, maxPages));

        List<T> result = input.subList(perPage * page, Math.min(perPage * page + perPage, input.size()));

        return new PaginateResult<T>(result, ++page, ++maxPages);
    }
}
