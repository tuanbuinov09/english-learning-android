package com.myapp.jdbc;

import java.sql.Connection;

public class JDBCController {
    JDBCModel JdbcModel = new JDBCModel();
    public Connection ConnnectionData() {
        return JdbcModel.getConnectionOf();
    }
}
