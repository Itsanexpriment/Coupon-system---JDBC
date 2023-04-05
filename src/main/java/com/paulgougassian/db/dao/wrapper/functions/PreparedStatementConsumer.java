package com.paulgougassian.db.dao.wrapper.functions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConsumer {
    void accept(PreparedStatement ps) throws SQLException;
}
