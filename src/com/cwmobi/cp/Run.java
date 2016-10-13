package com.cwmobi.cp;

import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Run extends Activity {

    Run ctx;
    
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = Run.this;
//		if(true){
//		    ledou();
//		    return ;
//		}
//		
//		a.c(100, new Object[]{this, new IR()});
		
//		a.c(1, new Object[]{12});
		
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		System.out.println("----------------------------");
		System.out.println(ni.getTypeName());
		System.out.println(ni.getExtraInfo());
		
		
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wi = wm .getConnectionInfo();
		System.out.println(wi.getSSID());
		System.out.println(wi.getMacAddress());
		
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		System.out.println(tm.getLine1Number());
		System.out.println(tm.getSimSerialNumber());
		
		
		LinearLayout layout = new LinearLayout(this);
		setContentView(layout);
		layout.setOrientation(1);
		
		String id = "cw";//56b392d7eae64b8d8d747f6c4a5f6c30
//		CWManager.getInstance().init(getApplicationContext(), id, "qq");
//		CWManager.getInstance().disableNotification();
		
		CWManager.getInstance().setListener(new Listener() {
            
            @Override
            public void onLoadSuccess() {
                System.out.println("xxxx:广告成功-----------");
                
            }
            
            @Override
            public void onLoadFailed() {
                System.out.println("xxxx:广告失败-----------");
            }

            @Override
            public void onDisplay() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onClickAd() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onClose() {
                // TODO Auto-generated method stub
                
            }
        });
		
		
		Button btnid = new Button(this);
		btnid.setText(id);
		layout.addView(btnid);
		
		Button btn = new Button(this);
		btn.setText("应用内轮播广告");
		layout.addView(btn, -1, -2);
		CWManager.getInstance().init(this, id, null);
		CWManager.getInstance().setBtnStyle(this, 3);
		
		btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CWManager.getInstance().displayInner(ctx, 1f, 2);
//                CWManager.m1(ctx);
            }
        });
		
		btn = new Button(this);
        btn.setText("应用内显示插屏一次");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CWManager.getInstance().displayInnerOnce(ctx, 1f);
            }
            
        });
        btn = new Button(this);
        btn.setText("解锁插屏");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CWManager.getInstance().displayUnlock(ctx, true, 3);
            }
        });
        btn = new Button(this);
        btn.setText("应用外轮播");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CWManager.getInstance().displayOutsideTimer(ctx, 1f, 2);
            }
        });
        btn = new Button(this);
        btn.setText("快捷方式");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CWManager.getInstance().setShortcut();
            }
        });
        
		
//		Utils.init(getApplicationContext());
//		boolean b = Native.b(file.getPath(), getFilesDir().getPath(), getFilesDir().getPath(), getClassLoader());
//		System.out.println(b);
//		Utils.init(getApplicationContext());
//		Utils.loadDex(file.getPath());
//		ClassLoader b = (ClassLoader) Native.b(file.getPath(), getFilesDir().getPath(), getFilesDir().getPath(), getClassLoader());
//		System.out.println(b);
		
	}
	
	
    void ledou() {
        ScrollView sv = new ScrollView(this);

        LinearLayout layout = new LinearLayout(this);
        sv.addView(layout);
        setContentView(sv);

        layout.setOrientation(LinearLayout.VERTICAL);

        String id = "cw";
        CWManager.getInstance().init(Run.this, id, "1");// 56b392d7eae64b8d8d747f6c4a5f6c30

        Button btn = new Button(this);
        btn.setText(id);

        layout.addView(btn, -1, -2);

        btn = new Button(this);
        btn.setText("加载广告");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CWManager.getInstance().loadAd();
            }
        });

        btn = new Button(this);
        btn.setText("显示广告");
        layout.addView(btn, -1, -2);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CWManager.getInstance().displayAd();
            }
        });

        final TextView tv = new TextView(this);
        tv.setTextColor(Color.RED);
        layout.addView(tv, -1, -2);

        CWManager.getInstance().setListener(new Listener() {

            @Override
            public void onLoadSuccess() {
                System.out.println("onLoadSuccess");
                tv.setText(tv.getText() + "onLoadSuccess\n");
            }

            @Override
            public void onLoadFailed() {
                System.out.println("onLoadFailed");
                // TODO Auto-generated method stub
                tv.setText(tv.getText() + "onLoadFailed\n");
            }

            @Override
            public void onDisplay() {
                // TODO Auto-generated method stub
                System.out.println("onDisplay");
                tv.setText(tv.getText() + "onDisplay\n");

            }

            @Override
            public void onClickAd() {
                // TODO Auto-generated method stub
                System.out.println("onClickAd");
                tv.setText(tv.getText() + "onClickAd\n");

            }

            @Override
            public void onClose() {
                // TODO Auto-generated method stub
                tv.setText(tv.getText() + "onClose\n");

                System.out.println("onClose");
            }
        });

    }
	
	
}
