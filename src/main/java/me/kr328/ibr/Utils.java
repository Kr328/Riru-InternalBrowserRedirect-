package me.kr328.ibr;

import android.content.Context;
import android.os.ServiceManager;
import com.android.internal.statusbar.IStatusBarService;

public class Utils {
    public static void main(String[] args) {
        IStatusBarService statusBarService = ServiceManager.getService(Context.STATUS_BAR_SERVICE);

        statusBarService.setIconVisibility("vpn");
    }
}
