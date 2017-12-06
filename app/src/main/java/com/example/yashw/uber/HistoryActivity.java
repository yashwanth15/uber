package com.example.yashw.uber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.yashw.uber.historyRecyclerView.HistoryAdapter;
import com.example.yashw.uber.historyRecyclerView.HistoryObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView=(RecyclerView)findViewById(R.id.historyRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        mHistoryLayoutManager=new LinearLayoutManager(HistoryActivity.this);
        recyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter=new HistoryAdapter(getDataSetHistory(),HistoryActivity.this);
        recyclerView.setAdapter(mHistoryAdapter);


        for (int i=0;i<100;i++){
            HistoryObject object=new HistoryObject(Integer.toString(i));
            resutlHistory.add(object);
        }

        mHistoryAdapter.notifyDataSetChanged();
    }

    private ArrayList resutlHistory=new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory() {
        return resutlHistory;
    }
}
