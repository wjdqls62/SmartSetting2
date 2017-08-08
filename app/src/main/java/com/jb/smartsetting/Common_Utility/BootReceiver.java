package com.jb.smartsetting.Common_Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jb.smartsetting.GPS_Utility.ProximityLocationService;

/**
 * Created by jeongbin.son on 2017-08-08.
 * 단말 리부팅,Power Off로 위치 근접알람 서비스를 재시작 하기위한 Receiver이다.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, ProximityLocationService.class));
        }
    }
}
