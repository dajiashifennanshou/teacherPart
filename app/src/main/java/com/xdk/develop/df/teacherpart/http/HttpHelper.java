package com.xdk.develop.df.teacherpart.http;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import com.xdk.develop.df.teacherpart.MainActivity;
import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.data.CurrentUser;
import com.xdk.develop.df.teacherpart.data.HttpData;
import com.xdk.develop.df.teacherpart.http.wait.DialogPolicy;
import com.xdk.develop.df.teacherpart.ui.login.LoginActivity;
import com.xdk.develop.df.teacherpart.util.FileUtils;
import com.xdk.develop.df.teacherpart.util.ImageDownLoader;
import com.xdk.develop.df.teacherpart.util.SharedPreferenceHelper;
import com.xdk.develop.df.teacherpart.util.ThreadPoolUtils;
import com.xdk.develop.df.teacherpart.util.ToastUtils;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class HttpHelper {
    private final static String UserName = "sa";//用户名
    private final static String Password = "cinzn2055";//密码

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void changeDada(final Activity activity, final Handler loginHnadler, final String sql) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
        final DialogPolicy waitPolicy = new DialogPolicy(activity);
        waitPolicy.displayLoading();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, loginHnadler);
                if (con == null) {
                    waitPolicy.disappear();
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    int a = stmt.executeUpdate(sql);
                    Message message = new Message();
                    message.what = a;
                    loginHnadler.sendMessage(message);
                    stmt.close();
                } catch (final SQLException e) {
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
                    waitPolicy.disappear();
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }
    public static void insertInto(final Activity activity, final Handler loginHnadler, final String sql, final HttpCallBack callBack) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
        final DialogPolicy waitPolicy = new DialogPolicy(activity);
        waitPolicy.displayLoading();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, loginHnadler);
                if (con == null) {
                    waitPolicy.disappear();
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    final int a = stmt.executeUpdate(sql);
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onInsertRespose(a);
                        }
                    });
                    stmt.close();
                } catch (final SQLException e) {
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
                    waitPolicy.disappear();
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }
    public static Connection getConnectSQl(final Activity activity, Handler handler) {
        try { // 加载驱动程序
            Connection con = null;
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://120.24.227.222:2055/czn_syktwww ", UserName,
                    Password);
            return con;
        } catch (ClassNotFoundException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(activity, "加载驱动程序出错");
                }
            });
        } catch (final SQLException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(activity, e.getMessage().toString());
                }
            });
        } catch (final Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(activity, "未知异常" + e.getMessage().toString());
                }
            });
        }
        return null;
    }

    public static void userLogin(final Activity activity, final Handler loginHnadler, final String schoolCode, final String username, final String password) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
        final DialogPolicy waitPolicy = new DialogPolicy(activity);
        waitPolicy.displayLoading();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, loginHnadler);
                if (con == null) {
                    waitPolicy.disappear();
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    String sql = "SELECT * FROM AppUser where schoolcode = '" + schoolCode + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (!rs.next()) {
                        loginHnadler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(activity, R.string.str_login_wrong_school_code);
                            }
                        });
                    } else {
                        String sql2 = "SELECT * FROM AppUser where schoolcode = '" + schoolCode + "' and accountid = '" + username + "'";
                        rs = stmt.executeQuery(sql2);
                        if (!rs.next()) {
                            loginHnadler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort(activity, R.string.str_login_studentCode_notExist);
                                }
                            });
                        } else {
                            String sql3 = "SELECT * FROM AppUser where schoolcode = '" + schoolCode + "' and accountid = '" + username + "' and userpwd = '" + password + "'";
                            rs = stmt.executeQuery(sql3);
                            if (!rs.next()) {
                                loginHnadler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort(activity, R.string.str_login_password_wrong);
                                    }
                                });
                            } else {
                                int vip = compare_date(rs.getString("validdate"), new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                                if (vip == -1) {
                                    loginHnadler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showShort(activity, R.string.str_user_no_vip);
                                        }
                                    });
                                    return;
                                }
                               TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                                String phoneNumber = tm.getLine1Number();//手机号码

                                if(phoneNumber.startsWith("+86")){
                                    phoneNumber = phoneNumber.substring(3);
                                }
                                if(phoneNumber != null && !phoneNumber.equals("")){
                                    if(!rs.getString("telephone1").equals(phoneNumber)&&!rs.getString("telephone2").equals(phoneNumber)&&!rs.getString("telephone3").equals(phoneNumber)){
                                        loginHnadler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtils.showShort(activity,"请使用绑定的电话号码手机登录");
                                            }
                                        });
                                        return ;
                                    }
                                }
                                CurrentUser user = new CurrentUser();
                                user.setAutoid(rs.getInt("autoid"));
                                user.setAccountid(rs.getString("accountid"));
                                user.setApertment(rs.getString("apartment"));
                                user.setUserstatus(rs.getString("userstatus"));
                                user.setUserstate(rs.getString("userstate"));
                                user.setLoginnumber(rs.getInt("loginnumber"));
                                user.setLogintime(rs.getString("logintime"));
                                user.setCardmoney(rs.getDouble("cardmoney"));
                                user.setCardstate(rs.getString("cardstate"));
                                user.setGrade(rs.getInt("grade"));
                                user.setName(rs.getString("name"));
                                user.setProfessional(rs.getString("professional"));
                                user.setRoom(rs.getString("room"));
                                user.setValiddate(rs.getString("validdate"));
                                user.setUserpwd(rs.getString("userpwd"));
                                user.setTelephone3(rs.getString("telephone3"));
                                user.setTelephone2(rs.getString("telephone2"));
                                user.setTelephone1(rs.getString("telephone1"));
                                user.setStudentid(rs.getString("studentid"));
                                user.setSchool(rs.getString("school"));
                                user.setSchoolcode(rs.getString("schoolcode"));
                                user.setSclass(rs.getInt("class"));
                                user.setUserpwd(rs.getString("userpwd"));
                                user.setTea_course(rs.getString("tea_course"));
                                user.setTea_title(rs.getString("tea_title"));
                                user.setTea_manager(rs.getString("tea_manager"));
                                user.setTea_indate(rs.getString("tea_indate"));
                                Log.e("+++++++++",rs.getString("tea_course")+rs.getString("tea_title")+rs.getString("tea_indate"));
                                if (user.getUserstatus().equals("学生")) {
                                    loginHnadler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showShort(activity, "请登录学生端");
                                        }
                                    });
                                    return;
                                }
                                loginHnadler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort(activity, R.string.str_login_success);
                                    }
                                });
                                SharedPreferenceHelper.putCurrentUser(activity, user);
                                SharedPreferenceHelper.putLoginTime(activity, System.currentTimeMillis());
                                ((LoginActivity) activity).goActivity(MainActivity.class);
                                activity.finish();
                            }
                        }
                    }
                    rs.close();
                    stmt.close();
                } catch (final SQLException e) {
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
                    waitPolicy.disappear();
                    if (con != null)
                        try {
                            con.close();
                        } catch (SQLException e) {
                        }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }

    public static void getStudent(final Activity activity, final Handler loginHnadler, final int currentpage, final int pageLength, final HttpCallBack callBack) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
//        final DialogPolicy waitPolicy = new DialogPolicy(activity);
//        waitPolicy.displayLoading();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, loginHnadler);
                if (con == null) {
//                    waitPolicy.disappear();
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    CurrentUser cuser = SharedPreferenceHelper.getCurrentUser(activity);
                    String sql = "SELECT * FROM ClassTeacher where teacherid = '" + cuser.getAccountid() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    boolean a = false;
                    StringBuilder builder = new StringBuilder();
                    StringBuilder builder1 = new StringBuilder();
                    builder.append("select  top " + pageLength + " * from AppUser where (");
                    while (rs.next()) {
                        if (a) {
                            builder.append("or (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+ "')");
                            builder1.append("or (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+ "')");
                        } else {
                            builder.append(" (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+  "')");
                            builder1.append(" (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+  "')");
                        }
                        if (!a) {
                            a = true;
                        }
                    }
                    if (!a) {
                        loginHnadler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(activity, "ClassTeacher 里面没数据");
                            }
                        });
                    } else {
                        builder.append(") and autoid not in (select top " + pageLength * currentpage + " autoid from AppUser where "+builder1.toString()+" order by autoid) order by autoid");
                         Log.e("sqlsqlsql",builder.toString());
                        rs = null;
                        String aa = "select  top 10 * from AppUser where ()  "+"and autoid not in (select top " + 1 + " autoid from AppUser order by autoid) order by autoid";
                        rs = stmt.executeQuery(builder.toString());
                        boolean flag = false;
                        List<CurrentUser> userList = new ArrayList<CurrentUser>();
                        while (rs.next()) {
                            if (!flag) {
                                flag = true;
                            }
                            CurrentUser user = new CurrentUser();
                            user.setAutoid(rs.getInt("autoid"));
                            user.setAccountid(rs.getString("accountid"));
                            user.setApertment(rs.getString("apartment"));
                            user.setUserstatus(rs.getString("userstatus"));
                            user.setUserstate(rs.getString("userstate"));
                            user.setLoginnumber(rs.getInt("loginnumber"));
                            user.setLogintime(rs.getString("logintime"));
                            user.setCardmoney(rs.getDouble("cardmoney"));
                            user.setCardstate(rs.getString("cardstate"));
                            user.setGrade(rs.getInt("grade"));
                            user.setName(rs.getString("name"));
                            user.setProfessional(rs.getString("professional"));
                            user.setRoom(rs.getString("room"));
                            user.setValiddate(rs.getString("validdate"));
                            user.setUserpwd(rs.getString("userpwd"));
                            user.setTelephone3(rs.getString("telephone3"));
                            user.setTelephone2(rs.getString("telephone2"));
                            user.setTelephone1(rs.getString("telephone1"));
                            user.setStudentid(rs.getString("studentid"));
                            user.setSchool(rs.getString("school"));
                            user.setSchoolcode(rs.getString("schoolcode"));
                            user.setSclass(rs.getInt("class"));
                            user.setUserpwd(rs.getString("userpwd"));
                            user.setTea_course(rs.getString("tea_course"));
                            user.setTea_title(rs.getString("tea_title"));
                            user.setTea_manager(rs.getString("tea_manager"));
                            user.setTea_indate(rs.getString("tea_indate"));
                            userList.add(user);
                        }
                        callBack.onRespose(userList);
                    }
                    rs.close();
                    stmt.close();
                } catch (final SQLException e) {
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
//                    waitPolicy.disappear();
                    if (con != null)
                        try {
                            con.close();
                        } catch (SQLException e) {
                        }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }
    public static void getStudentSelect(final Activity activity, final Handler loginHnadler, final int currentpage, final String like, final int pageLength, final HttpCallBack callBack) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
//        final DialogPolicy waitPolicy = new DialogPolicy(activity);
//        waitPolicy.displayLoading();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, loginHnadler);
                if (con == null) {
//                    waitPolicy.disappear();
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    CurrentUser cuser = SharedPreferenceHelper.getCurrentUser(activity);
                    String sql = "SELECT * FROM ClassTeacher where teacherid = '" + cuser.getAccountid() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    boolean a = false;
                    StringBuilder builder = new StringBuilder();
                    StringBuilder builder1 = new StringBuilder();
                    builder.append("select top  "+pageLength+" * from AppUser where (");
                    while (rs.next()) {
                        if (a) {
                            builder.append("or (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+ "')");
                            builder1.append("or (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+ "')");
                        } else {
                            builder.append(" (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+  "')");
                            builder1.append(" (schoolcode = '" + rs.getString("schoolcode") + "'and school = '" + rs.getString("school") +"' and professional = '"+rs.getString("professional")+"'and grade = '"+rs.getString("grade")+"'and class = '"+rs.getString("class")+  "')");
                        }
                        if (!a) {
                            a = true;
                        }
                    }
                    if (!a) {
                        loginHnadler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(activity, "ClassTeacher 里面没数据");
                            }
                        });
                    } else {
                        builder.append(") and name like '%"+like+"%'");
                        builder.append(" and autoid not in (select top " + pageLength * currentpage + " autoid from AppUser where "+builder1.toString()+" order by autoid) order by autoid");
                        Log.e("bulider++++++++++",builder.toString());
                        rs = null;
                        rs = stmt.executeQuery(builder.toString());
                        boolean flag = false;
                        final List<CurrentUser> userList = new ArrayList<CurrentUser>();
                        while (rs.next()) {
                            if (!flag) {
                                flag = true;
                            }
                            CurrentUser user = new CurrentUser();
                            user.setAutoid(rs.getInt("autoid"));
                            user.setAccountid(rs.getString("accountid"));
                            user.setApertment(rs.getString("apartment"));
                            user.setUserstatus(rs.getString("userstatus"));
                            user.setUserstate(rs.getString("userstate"));
                            user.setLoginnumber(rs.getInt("loginnumber"));
                            user.setLogintime(rs.getString("logintime"));
                            user.setCardmoney(rs.getDouble("cardmoney"));
                            user.setCardstate(rs.getString("cardstate"));
                            user.setGrade(rs.getInt("grade"));
                            user.setName(rs.getString("name"));
                            user.setProfessional(rs.getString("professional"));
                            user.setRoom(rs.getString("room"));
                            user.setValiddate(rs.getString("validdate"));
                            user.setUserpwd(rs.getString("userpwd"));
                            user.setTelephone3(rs.getString("telephone3"));
                            user.setTelephone2(rs.getString("telephone2"));
                            user.setTelephone1(rs.getString("telephone1"));
                            user.setStudentid(rs.getString("studentid"));
                            user.setSchool(rs.getString("school"));
                            user.setSchoolcode(rs.getString("schoolcode"));
                            user.setSclass(rs.getInt("class"));
                            user.setUserpwd(rs.getString("userpwd"));
                            user.setTea_course(rs.getString("tea_course"));
                            user.setTea_title(rs.getString("tea_title"));
                            user.setTea_manager(rs.getString("tea_manager"));
                            user.setTea_indate(rs.getString("tea_indate"));
                            userList.add(user);
                        }
                        loginHnadler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onRespose(userList);
                            }
                        });
                    }
                    rs.close();
                    stmt.close();
                } catch (final SQLException e) {
                    loginHnadler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
//                    waitPolicy.disappear();
                    if (con != null)
                        try {
                            con.close();
                        } catch (SQLException e) {
                        }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }
    public interface HttpCallBack {
        abstract void onRespose(List<CurrentUser> users);
        abstract void onInsertRespose(int result);
    }

    public static byte[] blob2ByteArr(Blob blob) throws Exception {
        byte[] b = null;
        try {
            if (blob != null) {
                long in = 1;
                b = blob.getBytes(in, (int) (blob.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("fault");
        }
        return b;
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static Bitmap getCurrentUserPhoto(final Activity activity, final Handler handler, final ImageView imageView) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return null;
        }
        final DialogPolicy waitPolicy = new DialogPolicy(activity);
        waitPolicy.displayLoading();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Connection con = getConnectSQl(activity, handler);
                if (con == null) {
                    return;
                }
                try {
                    Statement stmt = con.createStatement();
                    String sql = "SELECT * FROM UserPicture where picid = '" + SharedPreferenceHelper.getCurrentUser(activity).getAccountid() + "'and schoolcode = '" + SharedPreferenceHelper.getCurrentUser(activity).getSchoolcode() + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        byte[] data = blob2ByteArr(rs.getBlob("picture"));
                        Message message = new Message();
                        message.obj = data;
                        ImageDownLoader loader = new ImageDownLoader(activity);
                        loader.addBitmapToMemoryCache(sql, BitmapFactory.decodeByteArray(data, 0, data.length));
                        FileUtils utils = new FileUtils(activity);
                        if (!utils.isFileExists(sql)) {
                            utils.savaBitmap(sql, BitmapFactory.decodeByteArray(data, 0, data.length));
                        }
                        handler.sendMessage(message);
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(activity, "没有头像照片");
                            }
                        });
                    }
                    rs.close();
                    stmt.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    waitPolicy.disappear();
                    if (con != null)
                        try {
                            con.close();
                        } catch (SQLException e) {
                        }
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
        return null;
    }
    public static void getResult(final Activity activity, final Handler handler, final String sql, final HttpCallBackResult callBack) {
        if (!isNetworkAvailable(activity)) {
            ToastUtils.showShort(activity, R.string.str_no_net);
            return;
        }
//        final DialogPolicy waitPolicy = new DialogPolicy(activity);
//        waitPolicy.displayLoading();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Connection con = getConnectSQl(activity, handler);
                if (con == null) {
//                    waitPolicy.disappear();
                    return;
                }
                try {
                    final Statement stmt = con.createStatement();
                    final ResultSet rs = stmt.executeQuery(sql);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HttpData data = new HttpData();
                                data.setResultSet(rs);
                                data.setStatement(stmt);
                                data.setConnection(con);
                                callBack.onResponse(data);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (final SQLException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort(activity, e.getMessage());
                        }
                    });
                    e.printStackTrace();
                } finally {
//                    waitPolicy.disappear();
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }
    public interface HttpCallBackResult {
        void onResponse(HttpData data) throws SQLException;
    }
}
