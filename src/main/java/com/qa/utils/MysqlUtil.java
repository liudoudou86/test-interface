package com.qa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.sql.*;

/**
 * @Author Tesla.liu
 * @Date 2023/03/22
 * @Description
 */
public class MysqlUtil {
    public static Connection conn = null;
    public static PreparedStatement statement = null;
    public static ResultSet resultSet = null;
    public static String driver = null;
    public static String url = null;
    public static String user = null;
    public static  String pwd = null;

    /**
     * 加载配置文件&&初始化
     * @param propertiesName 配置文件名
     * @return 连接结果
     */
    public static Connection getConnection(String propertiesName){
        Connection connection = null;
        try {
            driver = String.valueOf(YamlUtil.INSTANCE.getValueByKey(propertiesName + ".driver"));
            url = String.valueOf(YamlUtil.INSTANCE.getValueByKey(propertiesName + ".url"));
            user = String.valueOf(YamlUtil.INSTANCE.getValueByKey(propertiesName + ".username"));
            pwd = String.valueOf(YamlUtil.INSTANCE.getValueByKey(propertiesName + ".password"));
            // 加载驱动
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 获取操作数据库的对象
     * @param sql sql语句
     * @param ob  参数可变
     * @return PreparedStatement
     */
    public static PreparedStatement getStatement(String propertiesName, String sql,Object...ob){
        //加载驱动
        try {
            //创建连接对象
            conn = getConnection(propertiesName);
            //创建执行对象
            statement = conn.prepareStatement(sql);
            //如果有参数  则添加参数
            if (ob.length>0){
                for(int i=0;i<ob.length;i++){
                    statement.setObject(i+1, ob[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statement;
    }

    /**
     * 对数据库的查询
     * @param sql sql语句
     * @param ob  可变参数
     * @return ResultSet 返回结果集合
     */
    public static ResultSet sqlSelect(String propertiesName, String sql,Object...ob){
        PreparedStatement statement = getStatement(propertiesName, sql, ob);
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * 对数据库的增、删、改
     * @param sql sql语句
     * @param ob  可变参数
     */
    public static void sqlUpdate(String propertiesName, String sql, Object...ob){
        PreparedStatement statement = getStatement(propertiesName, sql, ob);
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * 调用mybatis执行sql脚本
     * @param sqlPath sql脚本路径
     */
    public static void sqlScript(String propertiesName, String sqlPath) {
        Connection conn = MysqlUtil.getConnection(propertiesName);
        ScriptRunner runner = new ScriptRunner(conn);
        try {
            runner.runScript(Resources.getResourceAsReader(sqlPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * 关闭连接  释放资源
     */
    public static void closeAll(){
        // 关闭结果集对象
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭PreparedStatement对象
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭Connection 对象
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
