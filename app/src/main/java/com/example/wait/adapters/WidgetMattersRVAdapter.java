package com.example.wait.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wait.R;
import com.example.wait.models.Matter;
import com.example.wait.models.MatterList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WidgetMattersRVAdapter extends RecyclerView.Adapter<WidgetMattersRVAdapter.ViewHolder> {

    private MatterList matterList;

    private MyOnClickListener myOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardContentCardView;
        TextView cardContentTitle_textView;
        TextView cardContentTime_textView;
        TextView cardContentRest_textView;
        TextView cardContentDays_textView;
        ImageView cardContentBG_imageView;

        MyOnClickListener myOnClickListener;

        public ViewHolder(@NonNull View view, MyOnClickListener myOnClickListener) {
            super(view);
            cardContentCardView = view.findViewById(R.id.cardContentCardView);
            cardContentTitle_textView = view.findViewById(R.id.cardContentTitle_textView);
            cardContentTime_textView = view.findViewById(R.id.cardContentTime_textView);
            cardContentRest_textView = view.findViewById(R.id.cardContentRest_textView);
            cardContentDays_textView = view.findViewById(R.id.cardContentDays_textView);
            cardContentBG_imageView = view.findViewById(R.id.cardContentBG_imageView);

            this.myOnClickListener = myOnClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myOnClickListener.OnItemClickListener(v, getAdapterPosition());
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param matterList containing the data to populate views to be used
     * by RecyclerView.
     */
    public WidgetMattersRVAdapter(MatterList matterList, MyOnClickListener myOnClickListener) {
        this.matterList = matterList;
        this.myOnClickListener = myOnClickListener;
//        this.myOnLongClickListener = myOnLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_matter_cardview, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view, myOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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
    }

    public static interface MyOnClickListener{
        void OnItemClickListener(View view, int position);
    }

    @Override
    public int getItemCount() {
        return matterList.getCount();
    }

    public String convertDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm");
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
