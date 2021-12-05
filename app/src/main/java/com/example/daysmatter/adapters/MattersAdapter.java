package com.example.daysmatter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.daysmatter.R;

public class MattersAdapter extends RecyclerView.Adapter<MattersAdapter.ViewHolder> {

    private ViewPager2[] viewPager2Set;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewPager2 matter_viewPager2;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            matter_viewPager2 = (ViewPager2) view.findViewById(R.id.matter_viewPager2);
        }

        public ViewPager2 getViewPager2() {
            return matter_viewPager2;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param viewPager2Set ViewPager2[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MattersAdapter(ViewPager2[] viewPager2Set) {
        this.viewPager2Set = viewPager2Set;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_matters_recycler_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.getViewPager2().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return viewPager2Set.length;
    }
}
