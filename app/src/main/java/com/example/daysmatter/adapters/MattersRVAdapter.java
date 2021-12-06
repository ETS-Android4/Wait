package com.example.daysmatter.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daysmatter.R;
import com.example.daysmatter.models.Matter;
import com.example.daysmatter.models.MatterList;
import com.hanks.htextview.evaporate.EvaporateTextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MattersRVAdapter extends RecyclerView.Adapter<MattersRVAdapter.ViewHolder> {

    private MatterList matterList;

    private MyOnLongClickListener myOnLongClickListener;
    private MyOnClickListener myOnClickListener;
    private Handler mHandler;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        CardView cardContentCardView;
        TextView cardContentTitle_textView;
        TextView cardContentTime_textView;
        TextView cardContentDays_textView;
        ImageView cardContentBG_imageView;

        MyOnLongClickListener myOnLongClickListener;
        MyOnClickListener myOnClickListener;


        public ViewHolder(View view, MyOnClickListener myOnClickListener, MyOnLongClickListener myOnLongClickListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardContentCardView = view.findViewById(R.id.cardContentCardView);
            cardContentTitle_textView = view.findViewById(R.id.cardContentTitle_textView);
            cardContentTime_textView = view.findViewById(R.id.cardContentTime_textView);
            cardContentDays_textView = view.findViewById(R.id.cardContentDays_textView);
            cardContentBG_imageView = view.findViewById(R.id.cardContentBG_imageView);

            this.myOnClickListener = myOnClickListener;
            this.myOnLongClickListener = myOnLongClickListener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public View getCardContentCardView() {
            return cardContentCardView;
        }

        public TextView getCardContentTitle_textView() {
            return cardContentTitle_textView;
        }

        public TextView getCardContentTime_textView() {
            return cardContentTime_textView;
        }

        public TextView getCardContentDays_textView() {
            return cardContentDays_textView;
        }

        @Override
        public void onClick(View v) {
            myOnClickListener.OnItemClickListener(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            myOnLongClickListener.OnItemLongClickListener(v, getAdapterPosition());
            return false;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param matterList ViewPager2[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MattersRVAdapter(MatterList matterList, MyOnClickListener myOnClickListener, MyOnLongClickListener myOnLongClickListener) {
        this.matterList = matterList;
        this.myOnClickListener = myOnClickListener;
        this.myOnLongClickListener = myOnLongClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_matter_cardview, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view, myOnClickListener, myOnLongClickListener);

        viewHolder.cardContentCardView.post(new Runnable() {
            @Override
            public void run() {
                int height = viewHolder.cardContentCardView.getHeight();
                ViewGroup.LayoutParams layoutParams = viewHolder.cardContentBG_imageView.getLayoutParams();
                layoutParams.height = height;
                viewHolder.cardContentBG_imageView.setLayoutParams(layoutParams);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //根据position将每一个list里面的值绑定到viewholder
        Matter matter = matterList.getMatter(position);

//        String title = matter.getTitle();
//        StringBuffer titleAltered = new StringBuffer();
//        for (int i = 0; i < title.length(); i++){
//            titleAltered.append(title.charAt(i));
//            if (i != title.length()-1)
//                titleAltered.append(" ");
//        }
        viewHolder.cardContentTitle_textView.setText(matter.getTitle());
        viewHolder.cardContentTime_textView.setText(convertDateToString(matter.getTargetDate()));
        viewHolder.cardContentDays_textView.setText(getRemainedDays(matter.getTargetDate()));
        try {
            File file=new File(matter.getImagePath());
            viewHolder.cardContentBG_imageView.setImageURI(Uri.fromFile(file));
        }catch (NullPointerException e){
            setDefaultImage(viewHolder);
        }
    }

    public static interface MyOnClickListener{
        void OnItemClickListener(View view, int position);
    }
    public static interface MyOnLongClickListener {
        void OnItemLongClickListener(View view, int position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return matterList.getCount();
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

    public String convertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public String getRemainedDays(Date date){
        Date dateNow = new Date();
        long difference =  (date.getTime() - dateNow.getTime()) / 86400000;
        return String.valueOf(Math.abs(difference));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setDefaultImage(ViewHolder viewHolder){
        int start=1;
        int end=10;
        int rand = new Random().nextInt(end - start + 1) + start;
        switch (rand){
            case 1:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_1));
                break;
            case 2:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_2));
                break;
            case 3:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_3));
                break;
            case 4:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_4));
                break;
            case 5:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_5));
                break;
            case 6:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_6));
                break;
            case 7:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_7));
                break;
            case 8:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_8));
                break;
            case 9:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_9));
                break;
            case 10:
                viewHolder.cardContentBG_imageView.setImageDrawable(viewHolder.itemView.getContext().getResources().getDrawable(R.drawable.wallhaven_10));
                break;
        }
    }
}
