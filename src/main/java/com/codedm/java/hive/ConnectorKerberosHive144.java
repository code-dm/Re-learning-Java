package com.codedm.java.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.*;

/**
 * @author Dongming WU
 */
public class ConnectorKerberosHive144 {

    public static void main(String[] args) {
//        String url2 = "jdbc:hive2://192.168.70.60:10000/default";
        String url2 = "jdbc:hive2://192.168.90.144:10000/default;principal=hive/dn144@DDP.COM";
        String krb5 = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/krb5144.conf";
        String kerUser = "hive/dn144@DDP.COM";
        String kerTab = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/test.keytab";

        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        System.setProperty("java.security.krb5.conf", krb5);

        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab(kerUser, kerTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // 使用hive用户登陆
            conn = DriverManager.getConnection(url2);
            ps = conn.prepareStatement("use default");
            ps.execute();

            ResultSet rs = ps.executeQuery("show tables");
            // 处理结果集
            while (rs.next()) {
                System.out.println(rs.getString(1));
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
        }
    }
}
