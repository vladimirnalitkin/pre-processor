package com.van.processor.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSortRequest extends AbstractPageRequest {
    private static final Logger log = LoggerFactory.getLogger(AbstractSortRequest.class);

    private Sort sort;

    AbstractSortRequest(int page, int size, Sort sort) {
        super(page, size);
        this.sort = sort;
    }

    /**
     * Get sort.
     * @return Sort.
     */
    public Sort getSort() {
        return sort;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + super.hashCode();
        result = 31 * result + sort.hashCode();

        return result;
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

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractSortRequest other = (AbstractSortRequest) obj;
        return super.equals(other) && this.sort.equals(other.getSort());
    }
}
