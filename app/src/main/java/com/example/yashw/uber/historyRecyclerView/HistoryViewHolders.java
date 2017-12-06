package com.example.yashw.uber.historyRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yashw.uber.R;

/**
 * Created by yashw on 06-12-2017.
 */

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView rideId;

    public HistoryViewHolders(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        rideId=(TextView)itemView.findViewById(R.id.rideId);
    }

    @Override
    public void onClick(View v) {

    }
}
