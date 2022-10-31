package com.codedm.java.generate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 每条向MySQL数据库插入一条数据
 *
 * @author Dongming WU
 */
public class MySQLGenerateData {

    public static void main(String[] args) throws SQLException, InterruptedException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3308/db_order?useSSL=false&autoReconnect=true&failOverReadOnly=false&useServerPrepStmts=false&rewriteBatchedStatements=true",
                "root", "123456");
        connection.setAutoCommit(false);
        for (int i = 0; i < 100000; i++) {
            PreparedStatement cmd = connection
                    .prepareStatement("INSERT INTO `db_order`.`t_order` " +
                            "(`order_date`, `customer_name`, `price`, `product_id`, `order_status`) " +
                            "VALUES (now(), ?, 129.14200, ?, b'0');");
            cmd.setString(1, "客户" + i);
            cmd.setInt(2, i);
            cmd.addBatch();
            cmd.executeBatch();
            connection.commit();
            System.out.println(i);
            Thread.sleep(1000);
        }
    }
}
