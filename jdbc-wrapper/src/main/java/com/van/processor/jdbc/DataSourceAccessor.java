package com.van.processor.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public abstract class DataSourceAccessor {
    /**
     * Logger available to subclasses.
     */
    public final Logger log = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    private boolean lazyInit = true;

    /**
     * Set the JDBC DataSource to obtain connections from.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Return the DataSource used by this template.
     */

    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Obtain the DataSource for actual use.
     *
     * @return the DataSource (never {@code null})
     * @throws IllegalStateException in case of no DataSource set
     * @since 5.0
     */
    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        assert dataSource != null : "No DataSource set";
        return dataSource;
    }

    /**
     * Set whether to lazily initialize the SQLExceptionTranslator for this accessor,
     * on first encounter of a SQLException. Default is "true"; can be switched to
     * "false" for initialization on startup.
     * <p>Early initialization just applies if {@code afterPropertiesSet()} is called.
     *
     * @see #afterPropertiesSet()
     */
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Return whether to lazily initialize the SQLExceptionTranslator for this accessor.
     */
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    /**
     * Eagerly initialize the exception translator, if demanded,
     * creating a default one for the specified DataSource if none set.
     */
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
    }

}

