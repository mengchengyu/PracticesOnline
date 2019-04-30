package net.lzzy.practicesonline.activities.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Pair;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
public class AppUtils extends Application {



    private static final String SP_SETTING = "spSetting";
    private static final String URL_IP = "urlIp";
    private static final String URL_PORT = "urlPort";

    private static List<Activity> activities = new LinkedList<>();
    private static WeakReference<Context> wContext;
    private  static String runningActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        wContext = new WeakReference<>(this);
    }

    public static Context getContext() {
        return wContext.get();
    }

    public static void AddActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);

    }

    public static void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }

        }
        System.exit(0);
    }

    public static Activity getRunningActivity() {
        for (Activity activity : activities) {
            String mame=activity.getLocalClassName();
            if (AppUtils.runningActivity.equals(mame)) {
                return activity;
            }
        }
        return null;
    }
    public static void setRunning(String runningActivity){
        AppUtils.runningActivity=runningActivity;
    }

    public  static void setStopped(String stoppedActivity){
        if (stoppedActivity.equals(AppUtils.runningActivity)){
            AppUtils.runningActivity="";
        }
    }
    public static boolean isNetworkAvailable(){
        ConnectivityManager manager =(ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =manager !=null? manager.getActiveNetworkInfo():null;
        return info!=null&& info.isConnected();
    }

    // region 创建线程池执法

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "thread#" + count.getAndIncrement());
        }
    };
    private static final BlockingDeque<Runnable> POOL_QUEUE = new LinkedBlockingDeque<>(128);

    public static ThreadPoolExecutor getExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, POOL_QUEUE, THREAD_FACTORY);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    // endregion

    public static void tryConnectServer( String address) throws IOException{
        URL uri = new URL(address);
        HttpURLConnection connection =(HttpURLConnection) uri.openConnection();
        connection.setConnectTimeout(5000);
        connection.getContent();
    }
    public static void saveServerSetting(String ip,String port,Context context){
        SharedPreferences spSetting = context.getSharedPreferences("spSetting",MODE_PRIVATE);
        spSetting .edit()
                .putString("urlIp",ip)
                .putString("urlPort",port)
                .apply();

    }
    public static Pair<String ,String> loadServerSetting(Context context){
        SharedPreferences spSetting =context.getSharedPreferences(SP_SETTING,MODE_PRIVATE);
        String ip =spSetting.getString(URL_IP,"10.88.91.103");
        String port=spSetting.getString(URL_PORT,"8888");
        return new Pair<>(ip,port);
    }

}
