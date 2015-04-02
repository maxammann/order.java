package order.format.pagination;

import java.util.List;

/**
 *
 */
public interface Paginator {

    <T> PaginateResult<T> paginate(List<T> input, int page);

}
