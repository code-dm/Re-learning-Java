package com.codedm.java.jdbc.query;

import java.sql.*;

/**
 * @author Codedm
 */
public class MySQLStreamQuery {

    public static void main(String[] args) throws SQLException {
        long start = System.currentTimeMillis();
        Connection connection = DriverManager.getConnection(
//            "jdbc:mysql://127.0.0.1:3306/kxh?useServerPrepStmts=false&rewriteBatchedStatements=true",
                "jdbc:mysql://127.0.0.1:3306/etl_data?useSSL=false&autoReconnect=true&failOverReadOnly=false&useServerPrepStmts=false&rewriteBatchedStatements=true",
                "root", "123456");
        connection.setAutoCommit(true);
        PreparedStatement pstm = connection.prepareStatement("select * from source10w;",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        pstm.setFetchSize(Integer.MIN_VALUE);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1));
        }

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }
}
