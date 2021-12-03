package com.example.daysmatter;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hanks.htextview.evaporate.EvaporateTextView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private LinearLayout main_LL1;
    private LinearLayout main_LL2;
    private LinearLayout mainOwner_LL;
    private LinearLayout mainTime_LL;
    private TextClock mainDateToday_textView;
    private EvaporateTextView mainTimeHour_customTextView;
    private TextView mainTimeColon1_textView;
    private EvaporateTextView mainTimeMinute_customTextView;
    private TextView mainTimeColon2_textView;
    private EvaporateTextView mainTimeSecond_customTextView;
    private TextView mainPoweredBy_textView;
    private TextView mainOwnerName_textView;
    private CharSequence sysTimeHourStr;
    private CharSequence sysTimeMinuteStr;
    private CharSequence sysTimeSecondStr;
    private ImageView changeConfig_imageView;
    private TimeThread timeThread;

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        baseInit();
    }

    private void baseInit(){
        main_LL1 = findViewById(R.id.main_LL1);
        main_LL2 = findViewById(R.id.main_LL2);
        mainOwner_LL = findViewById(R.id.mainOwner_LL);
        mainTime_LL = findViewById(R.id.mainTime_LL);
        mainDateToday_textView = findViewById(R.id.mainDateToday_textView);
        mainTimeHour_customTextView = findViewById(R.id.mainTimeHour_customTextView);
        mainTimeColon1_textView = findViewById(R.id.mainTimeColon1_textView);
        mainTimeMinute_customTextView = findViewById(R.id.mainTimeMinute_customTextView);
        mainTimeColon2_textView = findViewById(R.id.mainTimeColon2_textView);
        mainTimeSecond_customTextView = findViewById(R.id.mainTimeSecond_customTextView);
        mainPoweredBy_textView = findViewById(R.id.mainPoweredBy_textView);
        mainOwnerName_textView = findViewById(R.id.mainOwnerName_textView);
        changeConfig_imageView = findViewById(R.id.changeConfig_imageView);

        adjustHeaderTexts();

        changeConfig_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    timeThread.pauseThread();
                    hideSystemUi();
                    setLandscapeAttr();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    timeThread.resumeThread();
                }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    timeThread.pauseThread();
                    setPortraitAttr();
                    getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    timeThread.resumeThread();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void adjustHeaderTexts(){
        mainTimeHour_customTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf"));
        mainTimeMinute_customTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf"));
        mainTimeSecond_customTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf"));
        mainPoweredBy_textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf"));
        mainOwnerName_textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf"));
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        long sysTime = System.currentTimeMillis();//获取系统时间
                        sysTimeHourStr = DateFormat.format("hh", sysTime);//时间显示格式
                        sysTimeMinuteStr = DateFormat.format("mm", sysTime);//时间显示格式
                        sysTimeSecondStr = DateFormat.format("ss", sysTime);//时间显示格式

                        mainTimeHour_customTextView.animateText(sysTimeHourStr); //更新时间
                        mainTimeMinute_customTextView.animateText(sysTimeMinuteStr); //更新时间
                        mainTimeSecond_customTextView.animateText(sysTimeSecondStr); //更新时间
                        break;
                    default:
                        break;

                }
            }
        };
        timeThread = new TimeThread();
        timeThread.start(); //启动新的线程

    }

    private class TimeThread extends Thread {

        private final Object lock = new Object();

        private boolean pause = false;

        void pauseThread(){
            pause = true;
        }

        void resumeThread(){
            pause =false;
            synchronized (lock){
                lock.notify();
            }
        }

        void onPause() {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            super.run();
            do {
                while (pause){
                    onPause();
                }
                try {
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    public void setLandscapeAttr(){
        // place the time information in the middle of the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayoutParams.gravity = Gravity.CENTER;
        main_LL1.setLayoutParams(frameLayoutParams);
        main_LL2.setVisibility(View.GONE);
        mainOwner_LL.setVisibility(View.GONE);
        mainTime_LL.setGravity(Gravity.CENTER);

        mainDateToday_textView.setVisibility(View.GONE);
        mainTimeHour_customTextView.setTextSize(100f);
        mainTimeMinute_customTextView.setTextSize(100f);
        mainTimeSecond_customTextView.setTextSize(100f);
        mainTimeColon1_textView.setTextSize(100f);
        mainTimeColon2_textView.setTextSize(100f);
    }

    public void setPortraitAttr(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayoutParams.gravity = Gravity.TOP;
        main_LL1.setLayoutParams(frameLayoutParams);
        main_LL2.setVisibility(View.VISIBLE);
        mainOwner_LL.setVisibility(View.VISIBLE);
        mainTime_LL.setGravity(Gravity.START);

        mainDateToday_textView.setVisibility(View.VISIBLE);
        mainTimeHour_customTextView.setTextSize(36f);
        mainTimeMinute_customTextView.setTextSize(36f);
        mainTimeSecond_customTextView.setTextSize(36f);
        mainTimeColon1_textView.setTextSize(36f);
        mainTimeColon2_textView.setTextSize(36f);
    }

    private void hideSystemUi(){
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }
}