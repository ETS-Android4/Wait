package com.example.wait.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.example.wait.R;
import com.example.wait.adapters.MattersRVAdapter;
import com.example.wait.adapters.WidgetMattersRVAdapter;
import com.example.wait.models.Matter;
import com.example.wait.models.MatterList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class WaitAppWidgetConfigure extends AppCompatActivity implements WidgetMattersRVAdapter.MyOnClickListener{

    private LinearLayout wConfig_LL1;
    private LinearLayout wConfigNoMatter_LL;
    private RecyclerView wConfigMatters_recyclerView;
    private ImageView wConfigAddMatter_imageView;
    private WidgetMattersRVAdapter widgetMattersRVAdapter;

    private ArrayList<Matter> dbMatterList;
    private MatterList matterList;
    private int positionOnClick;
    private boolean isOnAddEventActivityEnd = false;

    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_wait_app_widget_configure);

        baseInit();

        setMattersView();

        wConfigAddMatter_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitAppWidgetConfigure.this, AddNewEventActivity.class);
                intent.putExtra("matterList", matterList);
                isOnAddEventActivityEnd = true;
                WaitAppWidgetConfigure.this.startActivity(intent);
            }
        });
    }

    private void baseInit(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        
        //deal with database
        SQLiteDatabase db = LitePal.getDatabase();
        dbMatterList = (ArrayList<Matter>) LitePal.order("targetDate").find(Matter.class);
        wConfig_LL1 = findViewById(R.id.wConfig_LL1);
        wConfigNoMatter_LL = findViewById(R.id.wConfigNoMatter_LL);
        wConfigMatters_recyclerView = findViewById(R.id.wConfigMatters_recyclerView);
        wConfigAddMatter_imageView = findViewById(R.id.wConfigAddMatter_imageView);
    }

    public void setMattersView(){
        matterList = new MatterList(dbMatterList);
        if (dbMatterList.size() > 0){
            wConfigNoMatter_LL.setVisibility(View.GONE);
        }else {
            wConfigNoMatter_LL.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
                return true;
            }
        };
        wConfigMatters_recyclerView.setLayoutManager(linearLayoutManager);
        widgetMattersRVAdapter = new WidgetMattersRVAdapter(matterList,this);
        wConfigMatters_recyclerView.setAdapter(widgetMattersRVAdapter);
    }

    // 通过点击的recycle view位置设置小部件内容
    @Override
    public void OnItemClickListener(View view, int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WaitAppWidgetConfigure.this);
        RemoteViews views = new RemoteViews(WaitAppWidgetConfigure.this.getPackageName(),
                R.layout.wait_app_widget);
        views.setTextViewText(R.id.appwidgetTitle_text, matterList.getMatter(position).getTitle());
        views.setTextViewText(R.id.appwidgetTargetDate_text, convertDateToString(matterList.getMatter(position).getTargetDate()));
        views.setTextViewText(R.id.appwidgetRemainedDays_text, getRemainedDays(matterList.getMatter(position).getTargetDate()));
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public String convertDateToString(Date date){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
        return sdf.format(date);
    }

    public String getRemainedDays(Date date){
        Date dateNow = new Date();
        long difference =  (date.getTime() - dateNow.getTime()) / 86400000;
        return String.valueOf(Math.abs(difference));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isOnAddEventActivityEnd) {
//      TODO: Make the Adapter notify more reasonable
            dbMatterList = (ArrayList<Matter>) LitePal.order("targetDate").find(Matter.class);
            matterList = new MatterList(dbMatterList);
            if (dbMatterList.size() > 0) {
                wConfigNoMatter_LL.setVisibility(View.GONE);
            } else {
                wConfigNoMatter_LL.setVisibility(View.VISIBLE);
            }
            widgetMattersRVAdapter = new WidgetMattersRVAdapter(matterList,this);
            wConfigMatters_recyclerView.setAdapter(widgetMattersRVAdapter);
            isOnAddEventActivityEnd = false;
        }
    }
}