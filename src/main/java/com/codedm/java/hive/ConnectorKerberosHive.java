//package com.codedm.java.hive;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.security.UserGroupInformation;
//
//import java.io.IOException;
//import java.sql.*;
//
///**
// * @author Dongming WU
// */
//public class ConnectorKerberosHive {
//
//    public static synchronized Connection loginUserFromKeytab(String krb5, String kerUser, String kerTab, String url){
//        System.setProperty("java.security.krb5.conf", krb5);
//        Configuration conf = new Configuration();
//        conf.set("hadoop.security.authentication", "Kerberos");
//        UserGroupInformation.setConfiguration(conf);
//        try {
//            UserGroupInformation.loginUserFromKeytab(kerUser, kerTab);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            return DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void con210() {
//        String krb5 = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/krb5.conf";
//        String kerUser = "kafka@DDP.COM";
//        String kerTab = "/Users/wudongming/Documents/datatom/code/learnning-java/hexo/source/_posts/src/main/resources/kafka.keytab";
//
//
//        String url2 = "jdbc:hive2://dn90210:2181,dn90211:2181,dn90212:2181,dn90213:2181,dn90214:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
//        Connection conn = null;
//        PreparedStatement ps = null;
//        try {
//            // 使用hive用户登陆
//            conn = loginUserFromKeytab(krb5, kerUser, kerTab, url2);
//            ps = conn.prepareStatement("use yyq");
//            ps.execute();
//            while (true) {
//                System.out.println("90210=======================================");
//                ResultSet rs = ps.executeQuery("show tables");
//                // 处理结果集
//                while (rs.next()) {
//                    System.out.println(rs.getString(1));
//                    break;
//                }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            try {
//                ps.close();
//            } catch (SQLException ex) {
//            }
//            try {
//                conn.close();
//            } catch (SQLException ex) {
//            }
//        }finally {
//            try {
//                ps.close();
//            } catch (SQLException ex) {
//            }
//            try {
//                conn.close();
//            } catch (SQLException ex) {
//            }
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//
//        new Thread(ConnectorKerberosHive::con210).start();
//
//        Thread.sleep(1000);
//
//        new Thread(ConnectorKerberosHive7060::con7060).start();
//
//        System.out.println("线程之后");
//    }
//}
