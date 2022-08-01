package com.codedm.java.jdbc.insert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Codedm
 */
public class BatchInsertAutoCommitFalse {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(
//            "jdbc:mysql://127.0.0.1:3306/kxh?useServerPrepStmts=false&rewriteBatchedStatements=true",
            "jdbc:mysql://192.168.90.95:3306/test_db?useSSL=false&autoReconnect=true&failOverReadOnly=false&useServerPrepStmts=false&rewriteBatchedStatements=true",
            "root", "123456");
        connection.setAutoCommit(false);
        PreparedStatement cmd = connection
            .prepareStatement("INSERT INTO `test_db`.`demo3` (`name`, `price`) VALUES (?, ?);");


        cmd.setString(1, "wdm1111");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm2222");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm3333");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm4444");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555wdm5555");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();

        cmd.setString(1, "wdm6666");
        cmd.setDouble(2, 0.001);
        cmd.addBatch();
        int[] ints = null;

        for (int i = 0; i < 4; i++) {
            try {
                ints = cmd.executeBatch();
            }catch (Exception e) {
                e.printStackTrace();
            }
            connection.commit();
        }

        System.out.println(ints);
    }

}
