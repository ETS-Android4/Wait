package com.example.daysmatter.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.daysmatter.R;
import com.example.daysmatter.models.Matter;
import com.example.daysmatter.models.MatterList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MattersRVAdapter extends RecyclerView.Adapter<MattersRVAdapter.ViewHolder> {

    private MatterList matterList;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        View cardContentCardView;
        TextView cardContentTitle_textView;
        TextView cardContentTime_textView;
        TextView cardContentDays_textView;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            cardContentCardView = view.findViewById(R.id.cardContentCardView);
            cardContentTitle_textView = view.findViewById(R.id.cardContentTitle_textView);
            cardContentTime_textView = view.findViewById(R.id.cardContentTime_textView);
            cardContentDays_textView = view.findViewById(R.id.cardContentDays_textView);

            cardContentCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int p = getAdapterPosition();
                    Log.d("ADAPTER", String.valueOf(p) + " item onClick");
                }
            });
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param matterList ViewPager2[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MattersRVAdapter(MatterList matterList) {
        this.matterList = matterList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_matter_cardview, viewGroup, false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardContentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewHolder.getAdapterPosition()将会返回被点击的item的index
                int position = viewHolder.getAdapterPosition();
                Matter matter = matterList.getMatter(position);
                Toast.makeText(v.getContext(),"Click: "+matter.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //根据position将每一个list里面的值绑定到viewholder
        Matter matter = matterList.getMatter(position);
        viewHolder.cardContentTitle_textView.setText(matter.getTitle());
        viewHolder.cardContentTime_textView.setText(convertDateToString(matter.getTargetDate()));
        viewHolder.cardContentDays_textView.setText(getRemainedDays(matter.getTargetDate()));

        //设置点击事件
        if (mOnItemClickListener != null) {
            viewHolder.cardContentCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(viewHolder.itemView, pos);
                }
            });
        }

        //设置长按事件
        if (mOnItemLongClickListener != null) {
            viewHolder.cardContentCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.OnItemLongClick(v, pos);
                    return true;
                }
            });
        }
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

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


}
