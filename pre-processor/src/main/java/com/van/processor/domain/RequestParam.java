package com.van.processor.domain;

import com.van.processor.common.exeption.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParam extends AbstractFilteredRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestParam.class);

    private RequestParam(int page, int size, Filter filter, Sort sort) {
        super(page, size, filter, sort);
    }

    /**
     * Creates a new unfiltered {@link RequestParam}.
     *
     * @since 2.0
     */
    public static RequestParam of() {
        return of(DEFAULT_PAGE, DEFAULT_SIZE, Filter.unfiltered(), Sort.unsorted());
    }

    /**
     * Creates a new unfilterd {@link RequestParam}.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @since 2.0
     */
    public static RequestParam of(int page, int size) {
        return of(page, size, Filter.unfiltered(), Sort.unsorted());
    }

    /**
     * Creates a new {@link RequestParam} with filter and sort parameters applied.
     *
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @param sort must not be {@literal null}.
     * @since 2.0
     */
    public static RequestParam of(int page, int size, Filter filter, Sort sort) {
        return new RequestParam(page, size, filter, sort);
    }

    /**
     * Creates a new {@link RequestParam} with sort direction and properties applied.
     *
     * @param page       zero-based page index.
     * @param size       the size of the page to be returned.
     * @param direction  must not be {@literal null}.
     * @param properties must not be {@literal null}.
     * @since 2.0
     */
    public static RequestParam of(int page, int size, Sort.Direction direction, String... properties) {
        return of(page, size, Filter.unfiltered(), Sort.by(direction, properties));
    }

    /**
     * Creates a new {@link RequestParam} with sort direction and properties applied.
     *
     * @param page       zero-based page index.
     * @param size       the size of the page to be returned.
     * @param direction  must not be {@literal null}.
     * @param properties must not be {@literal null}.
     * @since 2.0
     */
    public static RequestParam of(int page, int size, String filter, Sort.Direction direction, String... properties) throws ParseException {
        return of(page, size, Filter.by(filter), Sort.by(direction, properties));
    }

    /**
     * Create new filtered , sorted request.
     * @param filter string
     * @return RequestParam
     */
    public static RequestParam of(String filter, String sort) throws ParseException{
        return of(DEFAULT_PAGE, DEFAULT_SIZE, Filter.by(filter), Sort.by(sort));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    public Pageable next() {
        return new RequestParam(getPageNumber() + 1, getPageSize(), getFilter(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.AbstractPageRequest#previous()
     */
    public RequestParam previous() {
        return getPageNumber() == 0 ? this : new RequestParam(getPageNumber() - 1, getPageSize(), getFilter(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    public Pageable first() {
        return new RequestParam(0, getPageSize(), getFilter(), getSort());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof RequestParam)) {
            return false;
        }

        RequestParam that = (RequestParam) obj;

        return super.equals(that) && this.getSort().equals(that.getSort()) && this.getFilter().equals(that.getFilter());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + getFilter().hashCode() + getSort().hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Page request [number: %d, size: %d, filter: %s, sort: %s]", getPageNumber(), getPageSize(), getFilter(), getSort());
    }
}
