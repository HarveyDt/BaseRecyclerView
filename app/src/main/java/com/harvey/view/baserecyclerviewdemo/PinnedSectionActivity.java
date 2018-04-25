package com.harvey.view.baserecyclerviewdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.harvey.view.baserecyclerviewdemo.adapter.StockAdapter;
import com.harvey.view.baserecyclerviewdemo.entity.StockEntity;
import com.chad.library.adapter.base.util.PinnedHeaderItemDecoration;
import com.chad.library.adapter.base.listener.OnHeaderClickAdapter;
import com.harvey.view.baserecyclerviewdemo.util.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuyang
 *         2018/4/24
 *         下午3:20
 */

public class PinnedSectionActivity extends AppCompatActivity {

  private RecyclerView mRecyclerView;

  private StockAdapter mAdapter;
  private PinnedHeaderItemDecoration mHeaderItemDecoration;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pinned_section);
    mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    new AsyncTask<Void, Void, String>() {

      @Override protected void onPreExecute() {

        mRecyclerView.setLayoutManager(
            new LinearLayoutManager(PinnedSectionActivity.this, LinearLayoutManager.VERTICAL,
                false));
        OnHeaderClickAdapter clickAdapter = new OnHeaderClickAdapter() {

          @Override public void onHeaderClick(View view, int id, int position) {
            switch (id) {
              case R.id.fl:
                Toast.makeText(PinnedSectionActivity.this,
                    "click, tag: " + mAdapter.getData().get(position).pinnedHeaderName,
                    Toast.LENGTH_SHORT).show();
                break;
            }
          }
        };

        mHeaderItemDecoration =
            new PinnedHeaderItemDecoration.Builder(StockEntity.StockInfo.TYPE_HEADER).setDividerId(
                R.drawable.divider)
                .enableDivider(true)
                .setClickIds(R.id.fl)
                .disableHeaderClick(false)
                .setHeaderClickListener(clickAdapter)
                .create();
        mRecyclerView.addItemDecoration(mHeaderItemDecoration);
      }

      @Override protected String doInBackground(Void... voids) {
        return getStrFromAssets("rasking.json");
      }

      @Override protected void onPostExecute(String result) {

        Gson gson = new Gson();

        final StockEntity stockEntity = gson.fromJson(result, StockEntity.class);

        List<StockEntity.StockInfo> data = new ArrayList<>();

        data.add(new StockEntity.StockInfo(StockEntity.StockInfo.TYPE_HEADER, "涨幅榜"));
        for (StockEntity.StockInfo info : stockEntity.increase_list) {
          info.setItemType(StockEntity.StockInfo.TYPE_DATA);
          data.add(info);
        }

        data.add(new StockEntity.StockInfo(StockEntity.StockInfo.TYPE_HEADER, "跌幅榜"));
        for (StockEntity.StockInfo info : stockEntity.down_list) {
          info.setItemType(StockEntity.StockInfo.TYPE_DATA);
          data.add(info);
        }

        data.add(new StockEntity.StockInfo(StockEntity.StockInfo.TYPE_HEADER, "换手率"));
        for (StockEntity.StockInfo info : stockEntity.change_list) {
          info.setItemType(StockEntity.StockInfo.TYPE_DATA);
          data.add(info);
        }

        data.add(new StockEntity.StockInfo(StockEntity.StockInfo.TYPE_HEADER, "振幅榜"));
        for (StockEntity.StockInfo info : stockEntity.amplitude_list) {
          info.setItemType(StockEntity.StockInfo.TYPE_DATA);
          data.add(info);
        }

        mAdapter = new StockAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
          @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            StockEntity.StockInfo entity = mAdapter.getItem(position);
            switch (view.getId()) {
              case R.id.fl:
                Toast.makeText(PinnedSectionActivity.this, "click tag: " + entity.pinnedHeaderName, Toast.LENGTH_SHORT).show();
                break;
              case R.id.content:
                Toast.makeText(PinnedSectionActivity.this, entity.stock_name + ", position " + position + ", id " , Toast.LENGTH_SHORT).show();
                break;
            }
          }
        });
        mAdapter.addHeaderView(LayoutInflater.from(PinnedSectionActivity.this)
            .inflate(R.layout.item_data, mRecyclerView, false));
        // 因为添加了1个头部，他是不在clickAdapter.getData这个数据里面的，所以这里要设置数据的偏移值告知ItemDecoration真正的数据索引
        mHeaderItemDecoration.setDataPositionOffset(mAdapter.getHeaderLayoutCount());

      }
    }.execute();
  }

  private String getStrFromAssets(String name) {

    AssetManager assetManager = Utils.getContext().getAssets();
    try {
      InputStream is = assetManager.open(name);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String str;
      while ((str = br.readLine()) != null) {
        sb.append(str);
      }
      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
