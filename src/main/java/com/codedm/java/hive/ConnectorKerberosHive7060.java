package com.codedm.java.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.*;

/**
 * @author Dongming WU
 */
public class ConnectorKerberosHive7060 {

    public static void con7060() {
        String url2 = "jdbc:hive2://192.168.70.60:10000/default;principal=hive/_HOST@DDP.COM";
//        String url2 = "jdbc:hive2://dn7058:2181,dn7059:2181,dn7060:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
        String krb5 = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/krb51.conf";
        String kerUser = "test@DDP.COM";
        String kerTab = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/test.keytab";


        Connection conn = null;
        PreparedStatement ps = null;
        try {

            // 使用hive用户登陆
            conn = ConnectorKerberosHive.loginUserFromKeytab(krb5, kerUser, kerTab, url2);;
            ps = conn.prepareStatement("use default");
            ps.execute();

            while (true) {
                System.out.println("7060--------------------------------------------");
                ResultSet rs = ps.executeQuery("show tables");
                // 处理结果集
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                    break;
                }
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ps.close();
            } catch (SQLException ex) {
            }
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }finally {
            try {
                ps.close();
            } catch (SQLException ex) {
            }
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }
}
