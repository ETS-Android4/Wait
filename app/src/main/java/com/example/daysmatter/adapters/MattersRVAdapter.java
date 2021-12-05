package com.example.daysmatter.adapters;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.daysmatter.R;
import com.example.daysmatter.models.Matter;
import com.example.daysmatter.models.MatterList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MattersRVAdapter extends RecyclerView.Adapter<MattersRVAdapter.ViewHolder> {

    private MatterList matterList;

    private MyOnLongClickListener myOnLongClickListener;
    private MyOnClickListener myOnClickListener;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        View cardContentCardView;
        TextView cardContentTitle_textView;
        TextView cardContentTime_textView;
        TextView cardContentDays_textView;

        MyOnLongClickListener myOnLongClickListener;
        MyOnClickListener myOnClickListener;

        public ViewHolder(View view, MyOnClickListener myOnClickListener, MyOnLongClickListener myOnLongClickListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardContentCardView = view.findViewById(R.id.cardContentCardView);
            cardContentTitle_textView = view.findViewById(R.id.cardContentTitle_textView);
            cardContentTime_textView = view.findViewById(R.id.cardContentTime_textView);
            cardContentDays_textView = view.findViewById(R.id.cardContentDays_textView);

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

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //根据position将每一个list里面的值绑定到viewholder
        Matter matter = matterList.getMatter(position);
        viewHolder.cardContentTitle_textView.setText(matter.getTitle());
        viewHolder.cardContentTime_textView.setText(convertDateToString(matter.getTargetDate()));
        viewHolder.cardContentDays_textView.setText(getRemainedDays(matter.getTargetDate()));
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
}
