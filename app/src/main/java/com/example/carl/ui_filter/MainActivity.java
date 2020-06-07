package com.example.carl.ui_filter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ListDataFilterView mListDataFilterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListDataFilterView=findViewById(R.id.list_data_filter_view);
        mListDataFilterView.setAdapter(new ListFilterMenuAdapter(this));
    }
}
