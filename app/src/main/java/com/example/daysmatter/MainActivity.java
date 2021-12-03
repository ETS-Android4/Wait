package com.example.daysmatter;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextClock;
import android.widget.TextView;

import com.yy.mobile.rollingtextview.CharOrder;
import com.yy.mobile.rollingtextview.RollingTextView;
import com.yy.mobile.rollingtextview.strategy.Strategy;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private TextClock mainDateToday_textView;
    private RollingTextView mainTimeNow_rollingTextView;
    private Animation animation_time;
    private CharSequence sysTimeStr;

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
        mainDateToday_textView = findViewById(R.id.mainDateToday_textView);
        mainTimeNow_rollingTextView = findViewById(R.id.mainTimeNow_rollingTextView);

        emphasisCurrentTimeChange();
    }

    @SuppressLint("HandlerLeak")
    private void emphasisCurrentTimeChange(){
        mainTimeNow_rollingTextView.setAnimationDuration(900L);
        mainTimeNow_rollingTextView.setCharStrategy(Strategy.NormalAnimation());
        mainTimeNow_rollingTextView.addCharOrder(CharOrder.Number);
        mainTimeNow_rollingTextView.setAnimationInterpolator(new AccelerateDecelerateInterpolator());
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        long sysTime = System.currentTimeMillis();//获取系统时间
                        sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);//时间显示格式
                        mainTimeNow_rollingTextView.setText(sysTimeStr); //更新时间
                        break;
                    default:
                        break;

                }
            }
        };
        new TimeThread().start(); //启动新的线程

    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
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
}