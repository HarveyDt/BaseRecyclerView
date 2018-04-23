package com.harvey.view.baserecyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.harvey.view.baserecyclerviewdemo.adapter.MultipleItemQuickAdapter;
import com.harvey.view.baserecyclerviewdemo.data.DataServer;
import com.harvey.view.baserecyclerviewdemo.entity.MultipleItem;
import java.util.List;

/**
 * @author yuyang
 *         2018/4/20
 *         上午10:21
 */

public class MultiplelItemActivity extends AppCompatActivity {
  private RecyclerView mRecyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_multiple_item);
    mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
    List<MultipleItem> data = DataServer.getMultipleItemData();
    MultipleItemQuickAdapter multipleItemAdapter = new MultipleItemQuickAdapter(this, data);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(multipleItemAdapter);
  }
}
