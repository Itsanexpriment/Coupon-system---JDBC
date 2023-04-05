package com.paulgougassian.db.dao.wrapper.functions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementFunction<T> {
    T apply(PreparedStatement ps) throws SQLException;
}
