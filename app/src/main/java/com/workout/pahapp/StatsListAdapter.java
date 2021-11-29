package com.workout.pahapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatsListAdapter extends RecyclerView.Adapter<StatsListAdapter.RunViewHolder>  {

    private final LayoutInflater mInflater;
    private List<Run> mRuns;
    private RunViewModel mRunViewModel;
    private Context context;
    private RecyclerView recyclerView;
    StatsListAdapter(Context context) { mInflater = LayoutInflater.from(context); this.context = context;}

    @Override
    public RunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycleview_row, parent, false);
        mRunViewModel = ViewModelProviders.of((FragmentActivity)context).get(RunViewModel.class);
        return new RunViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RunViewHolder holder, int position) {
        if (mRuns != null) {
            Run current = mRuns.get(position);
            holder.imageView.setImageBitmap(TrackingUtility.bytesToBitmap(current.mImg));
            holder.timeTextView.setText("Date: " + TrackingUtility.getDate(current.mTimestamp,"dd/MM/yyyy hh:mm"));
            holder.durationTextView.setText("Duration: " + TrackingUtility.getFormattedStopWatchTime(current.mTimeInMillis));
            holder.calTextView.setText("Cal. Burned: " + current.mCaloriesBurned);
            holder.speedTextView.setText("Avg. Speed : " + current.mAvgSpeenInKMH);
            holder.distanceTextView.setText("Distance : " + String.format("%.3f", (current.mDistanceInMeters * 0.001)) + " km");
            holder.ratingBar.setRating(current.mRating);
            
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putByteArray("image",current.mImg);
//                    bundle.putString("time", "Date: " + TrackingUtility.getDate(current.mTimestamp,"dd/MM/yyyy hh:mm"));
//                    bundle.putString("duration", "Duration: " + TrackingUtility.getFormattedStopWatchTime(current.mTimeInMillis));
//                    bundle.putString("calories", "Calories Burned: " + current.mCaloriesBurned);
//                    bundle.putString("avg_speed","Average Speed : " + current.mAvgSpeenInKMH);
//                    bundle.putString("distance", "Distance : " + String.format("%.3f", (current.mDistanceInMeters * 0.001)) + " km");
//                    bundle.putFloat("rating",current.mRating);
//                    DetailsFragment detailsFragment = new DetailsFragment();
//                    detailsFragment.setArguments(bundle);
//                    FragmentActivity activity = (FragmentActivity) view.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment).commit();
//                }
//            });
        } else {
            // Covers the case of data not being ready yet.
            holder.imageView.setImageResource(R.drawable.google_map_logo);
            holder.timeTextView.setText(" ");
            holder.calTextView.setText(" ");
            holder.speedTextView.setText(" ");
            holder.distanceTextView.setText(" ");
        }
    }

    void setRuns(List<Run> runs){
        mRuns = runs;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }


    @Override
    public int getItemCount() {
        if (mRuns != null)
            return mRuns.size();
        else return 0;
    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Run current = mRuns.get(viewHolder.getAdapterPosition());
            mRunViewModel.deleteRun(current.getRunId());
            notifyDataSetChanged();
        }
    };
    class RunViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeTextView;
        private final TextView calTextView;
        private final TextView speedTextView;
        private final TextView distanceTextView;
        private final TextView durationTextView;
        private final ImageView imageView;
        private final RatingBar ratingBar;

        private RunViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.map_image);
            timeTextView = itemView.findViewById(R.id.date_time_row);
            calTextView = itemView.findViewById(R.id.calories_row);
            speedTextView = itemView.findViewById(R.id.speed_row);
            distanceTextView = itemView.findViewById(R.id.distance_row);
            durationTextView = itemView.findViewById(R.id.duration_row);
            ratingBar = itemView.findViewById(R.id.ratingUser);
        }

    }
}