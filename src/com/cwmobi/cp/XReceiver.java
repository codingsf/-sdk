package com.cwmobi.cp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class XReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Constants.D) System.out.println(intent.getAction());
       
        Utils.onReceiver(context, intent);
        
    }

}
