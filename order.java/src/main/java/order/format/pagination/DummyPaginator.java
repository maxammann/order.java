package order.format.pagination;


import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Represents a DummyPaginator
 */
public class DummyPaginator implements Paginator {

    private int maxPages;

    @Inject
    public DummyPaginator(@Named("entires-per-page") int maxPages) {
        this.maxPages = maxPages;
    }

    public DummyPaginator() {
        this.maxPages = 1;
    }

    @Override
    public <T> PaginateResult<T> paginate(List<T> input, int page) {
        return new PaginateResult<T>(input, page, page / maxPages);
    }
}
