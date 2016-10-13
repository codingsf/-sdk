package com.cwmobi.cp;

import android.app.Activity;
import android.content.Context;

/**
 * @author fyq
 * @date 2016-5-17 下午2:58:48
 * 
 */
public class CWManager {

    
    public static final float CAP_MAX = 1;
    public static final float CAP_OVER_HALF = 0.8f;
    public static final float CAP_HALF = 0.5f;    
    
    private static CWManager instance;
    
    private CWManager(){
    }
    
    public static CWManager getInstance(){
        if(instance==null){
            instance = new CWManager();
        }
        return instance;
    }   
    
    
    public void init(Context ctx, String id, String chid){
        Utils.init(ctx, id, chid);
    }
    
    
    public void displayInner(Context ctx, float size, int times){
        a.c(1, new Object[]{ctx, size, times});
    }
    public void displayInnerOnce(Context ctx, float size){
        a.c(2, new Object[]{ctx, size});
    }
    public void displayUnlock(Context ctx, boolean b, int num){
        a.c(3, new Object[]{ctx, b, num});
    }
    
    public void displayOutsideTimer(Context ctx, float size, int num)
    {
        a.c(4, new Object[]{ctx, size, num});
    }
    
    public void setListener(Object listener){
        a.c(6, new Object[]{listener});
    }
    
    public void setShortcut(){
        a.c(Constants.METHOD_DISPLAY_SHORTCUT  , new Object[0]);
    }
    
     
    //displayInner
    public static void m1(Context ctx){
        //id apktooscooid
        //chid apktooscid
        
        try {
            CWManager mgr = getInstance();
            try{
//            mgr.init(ctx, "apktooscooid", "apktooscid");
            mgr.init(ctx, "cw", "apktooscid");
            }catch (Exception exception){}
            int times = 1;
            try {
                times = Integer.valueOf("_timesforinnercp");
            } catch (Exception e) {
            }
            setbtnsize(ctx);
            a.c(1, new Object[]{ctx, 1f, times});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   //displayInnerOnce
    public static void m2(Context ctx){
            CWManager mgr = getInstance();
            try{
            mgr.init(ctx, "apktooscooid", "apktooscid");
            }catch (Exception exception){}
            setbtnsize(ctx);
        a.c(2, new Object[]{ctx, 1f});
    }
    
    //diplayOutsideTimer
    public static void m3(Context ctx){
        CWManager mgr = getInstance();
        try{
        mgr.init(ctx, "apktooscooid", "apktooscid");
        }catch (Exception exception){}
        setbtnsize(ctx);
        a.c(4, new Object[]{ctx, 1f, 1});
    }
    
    //displayUnlock
    public static void m4(Context ctx){
            CWManager mgr = getInstance();
            try{
                mgr.init(ctx, "apktooscooid", "apktooscid");
            }catch (Exception exception){}
            setbtnsize(ctx);
        a.c(3, new Object[]{ctx, true, 2});
    }
    
    private static void setbtnsize(Context ctx){
        int close = 0;
        try {
            close = Integer.valueOf("_closebuttonsize");
        } catch (Exception e) {
        }
        a.c(5, new Object[]{ctx, close});
    }
    
    public static void m5(Context ctx) {
        CWManager mgr = getInstance();
        mgr.init(ctx, "apktooscooid", "apktooscid");
        mgr.setShortcut();
    }
    
    
    public void setBtnStyle(Context ctx, int size){
        a.c(5, new Object[]{ctx, size});
    }
    
    public void disableNotification(){
        a.c(Constants.METHOD_DISABLE_NOTIFICATION, new Object[0]);
    }
    
    public void loadAd(){
        a.c(Constants.METHOD_LOAD_AD, new Object[0]);
    }
    
    public void displayAd(){
        a.c(Constants.METHOD_DISPLAY_AD, new Object[0]);
    }

}
