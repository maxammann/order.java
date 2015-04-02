package order.format.pagination;

import java.util.List;

/**
 * Represents a PaginateResult
 */
public class PaginateResult<T> {

    private final List<T> result;

    private final int page, maxPages;

    public PaginateResult(List<T> result, int page, int maxPages) {
        this.result = result;
        this.page = page;
        this.maxPages = maxPages;
    }

    public List<T> result() {
        return result;
    }

    public int page() {
        return page;
    }

    public int getMaxPages() {
        return maxPages;
    }
}
