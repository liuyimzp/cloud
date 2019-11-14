package com.yinhai.sysframework.persistence.hibernate;

import org.hibernate.dialect.pagination.LegacyLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;

/**
 * @author MinusZero [hesh@yinhai.com]
 * @since 4.02
 */
public class InformixDialect extends org.hibernate.dialect.InformixDialect {
    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public String getLimitString(String querySelect, int offset, int limit) {
        return new StringBuffer(querySelect.length() + 8)
                .append(querySelect)
                .insert(querySelect.toLowerCase().indexOf("select") + 6, " skip " + offset + " first " + limit).toString();
    }

    @Override
    public LimitHandler getLimitHandler() {
        return new LegacyLimitHandler(this);
    }
}
