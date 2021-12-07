package com.example.daysmatter.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.ChangeImageTransform;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.example.daysmatter.R;
import com.example.daysmatter.activities.AddNewEventActivity;
import com.example.daysmatter.models.Matter;
import com.example.daysmatter.models.MatterList;
import com.hanks.htextview.evaporate.EvaporateTextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import info.hoang8f.widget.FButton;

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
        TextView cardContentRest_textView;
        TextView cardContentDays_textView;
        ImageView cardContentBG_imageView;
        FButton cardContent_btn;
        LinearLayout cardTime_LL;

        MyOnLongClickListener myOnLongClickListener;
        MyOnClickListener myOnClickListener;

        static int oldHeight = 0;
        static int oldPosition;
        static View oldView;


        public ViewHolder(View view, MyOnClickListener myOnClickListener, MyOnLongClickListener myOnLongClickListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardContentCardView = view.findViewById(R.id.cardContentCardView);
            cardContentTitle_textView = view.findViewById(R.id.cardContentTitle_textView);
            cardContentTime_textView = view.findViewById(R.id.cardContentTime_textView);
            cardContentRest_textView = view.findViewById(R.id.cardContentRest_textView);
            cardContentDays_textView = view.findViewById(R.id.cardContentDays_textView);
            cardContentBG_imageView = view.findViewById(R.id.cardContentBG_imageView);
            cardContent_btn = view.findViewById(R.id.cardContent_btn);
            cardTime_LL = view.findViewById(R.id.cardTime_LL);

            cardContent_btn.setButtonColor(0xFFFFFFFF);
            cardContent_btn.setCornerRadius(30);

            this.myOnClickListener = myOnClickListener;
            this.myOnLongClickListener = myOnLongClickListener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (oldPosition != getAdapterPosition() && oldView != null){
                        TransitionManager.beginDelayedTransition(oldView.findViewById(R.id.cardContentCardView), new TransitionSet().addTransition(new ChangeImageTransform()));
                        oldView.findViewById(R.id.cardTime_LL).setVisibility(View.GONE);
                        ViewGroup.LayoutParams layoutParams = oldView.findViewById(R.id.cardContentBG_imageView).getLayoutParams();
                        layoutParams.height = oldHeight;
                        oldView.findViewById(R.id.cardContentBG_imageView).setLayoutParams(layoutParams);
                        TransitionManager.endTransitions(oldView.findViewById(R.id.cardContentCardView));
                    }
                    oldPosition = getAdapterPosition();
                    oldView = v;
                    setExpandedCards(v);
                }
            });
        }

        @Override
        public void onClick(View v) {
            myOnClickListener.OnItemClickListener(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            myOnLongClickListener.OnItemLongClickListener(v, getAdapterPosition());
            return true;
        }

        /**
         * set feature of expand or shrink cards
         * @param view
         */
        public void setExpandedCards(View view) {

            cardContentCardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ViewGroup.LayoutParams layoutParams = cardContentBG_imageView.getLayoutParams();
                    if ((bottom - top) > (oldBottom - oldTop)) {
                        layoutParams.height = bottom - top;
                        oldHeight = oldBottom - oldTop;
                        cardContentBG_imageView.setLayoutParams(layoutParams);
                    }
                }
            });

            if (cardTime_LL.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardContentCardView, new AutoTransition());
                cardTime_LL.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(cardContentCardView, new TransitionSet().addTransition(new ChangeImageTransform()));
                cardTime_LL.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = cardContentBG_imageView.getLayoutParams();
                layoutParams.height = oldHeight;
                cardContentBG_imageView.setLayoutParams(layoutParams);
            }
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

        return viewHolder;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        //根据position将每一个list里面的值绑定到viewholder
        Matter matter = matterList.getMatter(position);

        viewHolder.cardContentTitle_textView.setText(matter.getTitle());
        viewHolder.cardContentTime_textView.setText(convertDateToString(matter.getTargetDate()));
        Date date = new Date();
        if (date.getTime() > matter.getTargetDate().getTime()){
            viewHolder.cardContentRest_textView.setText("已经");
        }else{
            viewHolder.cardContentRest_textView.setText("还有");
        }
        viewHolder.cardContentDays_textView.setText(getRemainedDays(matter.getTargetDate()));
        try {
            File file = new File(matter.getImagePath());
            viewHolder.cardContentBG_imageView.setImageURI(Uri.fromFile(file));
        }catch (NullPointerException e){
            setDefaultImage(viewHolder);
        }

        viewHolder.cardContentCardView.post(new Runnable() {
            @Override
            public void run() {
                int height = viewHolder.cardContentCardView.getHeight();
                ViewGroup.LayoutParams layoutParams = viewHolder.cardContentBG_imageView.getLayoutParams();
                layoutParams.height = height;
                viewHolder.cardContentBG_imageView.setLayoutParams(layoutParams);
            }
        });

        viewHolder.cardContent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewHolder.itemView.getContext(), AddNewEventActivity.class);
                intent.putExtra("matter", matter);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
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
