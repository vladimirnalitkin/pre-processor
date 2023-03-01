package com.van.processor.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFilteredRequest extends AbstractSortRequest {
    private static final Logger log = LoggerFactory.getLogger(AbstractFilteredRequest.class);

    private Filter filter;

    AbstractFilteredRequest(int page, int size, Filter filter, Sort sort) {
        super(page, size, sort);
        this.filter = filter;
    }

    /**
     * Get filter.
     * @return filter
     */
    public Filter getFilter() {
        return filter;
    }

    public boolean isFiltered(){
     return !Filter.unfiltered().equals(filter);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + super.hashCode();
        result = 31 * result + filter.hashCode();

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

        AbstractFilteredRequest other = (AbstractFilteredRequest) obj;
        return super.equals(other) && this.filter.equals(other.getFilter());
    }
}
