package com.xsm.mybatis.util;

import java.sql.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author xsm
 * @Date 2021/1/17 18:14
 */
public class DbUtil {

    //连接对象
    //Statement 命令对象
    //打开连接
    //关闭连接
    //得到一个连接对象
    //查询（有参，无参）
    //修改（有参，无参）

    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement pstmt=null;

    //驱动，服务器地址，登录用户名，密码
    static String DBDRIVER;
    static String DBURL;
    static String DBUID;
    static String DBPWD;

    static {
        DBDRIVER = "com.mysql.jdbc.Driver";
        DBURL = "jdbc:mysql://localhost:3306/mybatis";
        DBUID = "root";
        //DBPWD = "@zdm168168...";
        DBPWD = "admin";//打开连接
    }

    public static void open() {
        //加载驱动
        try {
            Class.forName(DBDRIVER);
            conn= DriverManager.getConnection(DBURL,DBUID,DBPWD);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //关闭连接
    public static void close() {
        try {
            if(stmt!=null)
                stmt.close();
            if(conn!=null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //得到一个连接对象，当用户使用DBUtil无法解决个性问题时
    //可以通过本方法获得连接对象
    public static Connection getConnection() {
        try {
            if(conn==null ||conn.isClosed())
                open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //executeQuery
    //executeUpdate
    //execute
    //获得查询的数据集
    //select * from student where name='' and sex=''
    public static ResultSet executeQuery(String sql) {
        try {
            open();//保证连接是成功的
            stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //修改表格内容
    public static int executeUpdate(String sql) {
        int result = 0;
        try {
            open();//保证连接是成功的
            stmt = conn.createStatement();
            result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }
    //如果执行的查询或存储过程，会返回多个数据集，或多个执行成功记录数
    //可以调用本方法，返回的结果，
    //是一个List<ResultSet>或List<Integer>集合
    public static Object execute(String sql) {
        boolean b=false;
        try {
            open();//保证连接是成功的
            stmt = conn.createStatement();
            b = stmt.execute(sql);
            //true,执行的是一个查询语句，我们可以得到一个数据集
            //false,执行的是一个修改语句，我们可以得到一个执行成功的记录数
            if(b){
                return stmt.getResultSet();
            }
            else {
                return stmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(!b) {
                close();
            }
        }
        return null;
    }

    //
    //select * from student where name=? and sex=?
    public static ResultSet executeQuery(String sql,Object[] in) {
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            stmt = pst;//只是为了关闭命令对象pst
            return pst.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static int executeUpdate(String sql,Object[] in) {
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            stmt = pst;//只是为了关闭命令对象pst
            return pst.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            close();
        }
        return 0;
    }

    public static PreparedStatement pstmt(String sql){
        open();
        try {
            return pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }

    public static Object execute(String sql,Object[] in) {
        boolean b=false;
        try {
            open();//保证连接是成功的
            PreparedStatement pst = conn.prepareStatement(sql);
            for(int i=0;i<in.length;i++)
                pst.setObject(i+1, in[i]);
            b = pst.execute();
            //true,执行的是一个查询语句，我们可以得到一个数据集
            //false,执行的是一个修改语句，我们可以得到一个执行成功的记录数
            if(b){
                System.out.println("----");
                /*List<ResultSet> list = new ArrayList<ResultSet>();
                list.add(pst.getResultSet());
                while(pst.getMoreResults()) {
                    list.add(pst.getResultSet());
                }*/
                return pst.getResultSet();
            }
            else {
                System.out.println("****");
                List<Integer> list = new ArrayList<Integer>();
                list.add(pst.getUpdateCount());
                while(pst.getMoreResults()) {
                    list.add(pst.getUpdateCount());
                }
                return list;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(!b) {
                System.out.println("====");
                close();
            }
        }
        return null;
    }
    //调用存储过程  proc_Insert(?,?,?)
    public static Object executeProcedure(String procName,Object[] in) {
        open();
        try {
            procName = "{call "+procName+"(";
            String link="";
            for(int i=0;i<in.length;i++) {
                procName+=link+"?";
                link=",";
            }
            procName+=")}";
            CallableStatement cstmt = conn.prepareCall(procName);
            for(int i=0;i<in.length;i++) {
                cstmt.setObject(i+1, in[i]);
            }
            if(cstmt.execute())
            {
                return cstmt.getResultSet();
            }
            else {
                return cstmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    /*
     * 调用存储过程，并有输出参数
     * @procName ，存储过程名称：proc_Insert(?,?)
     * @in ,输入参数集合
     * @output,输出参数集合
     * @type,输出参数类型集合
     * */
    public static Object executeOutputProcedure(String procName,
                                                Object[] in,Object[] output,int[] type){
        Object result = null;
        try {
            CallableStatement cstmt = conn.prepareCall("{call "+procName+"}");
            //设置存储过程的参数值
            int i=0;
            for(;i<in.length;i++){//设置输入参数
                cstmt.setObject(i+1, in[i]);
                //print(i+1);
            }
            int len = output.length+i;
            for(;i<len;i++){//设置输出参数
                cstmt.registerOutParameter(i+1,type[i-in.length]);
                //print(i+1);
            }
            boolean b = cstmt.execute();
            //获取输出参数的值
            for(i=in.length;i<output.length+in.length;i++)
                output[i-in.length] = cstmt.getObject(i+1);
            if(b) {
                result = cstmt.getResultSet();
            }
            else {
                result = cstmt.getUpdateCount();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    //时间转换
    public static Timestamp date(String date_str) {
        try {
            Calendar cal = Calendar.getInstance();//日期类
            Timestamp timestampnow = new Timestamp(cal.getTimeInMillis());//转换成正常的日期格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//改为需要的东西
            ParsePosition pos = new ParsePosition(0);
            java.util.Date current = formatter.parse(date_str, pos);
            timestampnow = new Timestamp(current.getTime());
            return timestampnow;
        }
        catch (NullPointerException e) {
            return null;
        }
    };
}
