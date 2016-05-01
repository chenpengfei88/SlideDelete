package com.qingcong.slidedelete;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by chenpengfei on 2016/4/7.
 */
public class PosApplication extends Application {

    public static int screenWidth;
    public static float screenDensity;//屏幕密度
    public static int screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        initScreeenInfomation();
    }

    /**
     *  初始化屏幕信息
     */
    private void initScreeenInfomation() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenDensity = dm.density;
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
