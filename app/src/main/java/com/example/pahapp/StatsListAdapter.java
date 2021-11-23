package com.example.pahapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatsListAdapter extends RecyclerView.Adapter<StatsListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Run> mRuns; // Cached copy of words

    StatsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycleview_row, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mRuns != null) {
            Run current = mRuns.get(position);
            holder.imageView.setImageBitmap(TrackingUtility.bytesToBitmap(current.mImg));
            holder.timeTextView.setText("Date: " + TrackingUtility.getDate(current.mTimestamp,"dd/MM/yyyy hh:mm"));
            holder.durationTextView.setText("Duration: " + TrackingUtility.getFormattedStopWatchTime(current.mTimeInMillis));
            holder.calTextView.setText("Cal. Burned: " + current.mCaloriesBurned);
            holder.speedTextView.setText("Avg. Speed : " + current.mAvgSpeenInKMH);
            holder.distanceTextView.setText("Distance : " + String.format("%.3f", (current.mDistanceInMeters * 0.001)) + "m");
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

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mRuns != null)
            return mRuns.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeTextView;
        private final TextView calTextView;
        private final TextView speedTextView;
        private final TextView distanceTextView;
        private final TextView durationTextView;
        private final ImageView imageView;

        private WordViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.map_image);
            timeTextView = itemView.findViewById(R.id.date_time_row);
            calTextView = itemView.findViewById(R.id.calories_row);
            speedTextView = itemView.findViewById(R.id.speed_row);
            distanceTextView = itemView.findViewById(R.id.distance_row);
            durationTextView = itemView.findViewById(R.id.duration_row);
        }
    }
}