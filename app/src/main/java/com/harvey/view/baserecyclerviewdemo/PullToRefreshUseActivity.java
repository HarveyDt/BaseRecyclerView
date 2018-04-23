package com.harvey.view.baserecyclerviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.harvey.view.baserecyclerviewdemo.adapter.PullToRefreshAdapter;
import com.harvey.view.baserecyclerviewdemo.data.DataServer;
import com.harvey.view.baserecyclerviewdemo.entity.Status;
import java.util.List;

interface RequestCallBack {
  void success(List<Status> data);

  void fail(Exception e);
}

class Request extends Thread {
  private static final int PAGE_SIZE = 6;
  private int mPage;
  private RequestCallBack mCallBack;
  private Handler mHandler;

  private static boolean mFirstPageNoMore;
  private static boolean mFirstError = true;

  public Request(int page, RequestCallBack callBack) {
    mPage = page;
    mCallBack = callBack;
    mHandler = new Handler(Looper.getMainLooper());
  }

  @Override public void run() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
    }

    if (mPage == 2 && mFirstError) {
      mFirstError = false;
      mHandler.post(new Runnable() {
        @Override public void run() {
          mCallBack.fail(new RuntimeException("fail"));
        }
      });
    } else {
      int size = PAGE_SIZE;
      if (mPage == 1) {
        if (mFirstPageNoMore) {
          size = 1;
        }
        mFirstPageNoMore = !mFirstPageNoMore;
        if (!mFirstError) {
          mFirstError = true;
        }
      } else if (mPage == 4) {
        size = 1;
      }

      final int dataSize = size;
      mHandler.post(new Runnable() {
        @Override public void run() {
          mCallBack.success(DataServer.getSampleData(dataSize));
        }
      });
    }
  }
}

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends Activity {
  private static String TAG = "PullToRefreshUseActivity";
  private static final int PAGE_SIZE = 6;

  private RecyclerView mRecyclerView;
  private SwipeRefreshLayout mSwipeRefreshLayout;
  private PullToRefreshAdapter mAdapter;
  private View errorView;
  private int mNextRequestPage = 1;
  private boolean mError = true;
  private boolean mNoData = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pull_to_refresh);
    mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
    mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
    findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        mError = true;
        mNoData = true;
        mAdapter.setNewData(null);
        refresh();
      }
    });
    //mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    errorView =
        getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(),
            false);

    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onEmptyViewRefresh();
      }
    });
    initAdapter();
    addHeadView();
    initRefreshLayout();
    mSwipeRefreshLayout.setRefreshing(true);
    refresh();
  }

  private void initAdapter() {
    mAdapter = new PullToRefreshAdapter();
    mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
      @Override public void onLoadMoreRequested() {
        loadMore();
      }
    },mRecyclerView);
    //mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);  //设置动画
    //mAdapter.setPreLoadNumber(3);   //设置后几个item 执行加载更多 默认最一个
    mRecyclerView.setAdapter(mAdapter);
    //mAdapter.setLoadMoreView(new CustomLoadMoreView());  //设置自定义 LoadMoreView

    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemClick: ");
        Toast.makeText(PullToRefreshUseActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT)
            .show();
      }
    });
    mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
      @Override public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemLongClick: ");
        Toast.makeText(PullToRefreshUseActivity.this, "onItemLongClick" + position,
            Toast.LENGTH_SHORT).show();
        return true;
      }
    });
    mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemChildClick: ");
        Toast.makeText(PullToRefreshUseActivity.this, "onItemChildClick" + position,
            Toast.LENGTH_SHORT).show();
      }
    });
    mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
      @Override
      public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        Log.d(TAG, "onItemChildLongClick: ");
        Toast.makeText(PullToRefreshUseActivity.this, "onItemChildLongClick" + position,
            Toast.LENGTH_SHORT).show();
        return true;
      }
    });
  }

  private void addHeadView() {
    View headView =
        getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(),
            false);
    headView.findViewById(R.id.iv).setVisibility(View.GONE);
    ((TextView) headView.findViewById(R.id.tv)).setText("change load view");
    headView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mAdapter.setNewData(null);
        onEmptyViewRefresh();
      }
    });
    mAdapter.addHeaderView(headView);
  }

  /**
   * EmtyView刷新
   */
  private void onEmptyViewRefresh() {
    mAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        if (mError) {
          mAdapter.setEmptyView(errorView);
          mError = false;
        } else {
          if (mNoData) {
            mAdapter.setEmptyView(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent());
            mNoData = false;
          } else {
            mAdapter.setNewData(DataServer.getSampleData(10));
          }
        }
      }
    }, 1000);
  }

  private void initRefreshLayout() {
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        refresh();
      }
    });
  }

  private void refresh() {
    mNextRequestPage = 1;
    mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
    new Request(mNextRequestPage, new RequestCallBack() {
      @Override public void success(List<Status> data) {
        setData(true, data);
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
      }

      @Override public void fail(Exception e) {
        Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG)
            .show();
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setRefreshing(false);
      }
    }).start();
  }

  private void loadMore() {
    new Request(mNextRequestPage, new RequestCallBack() {
      @Override public void success(List<Status> data) {
        setData(false, data);
      }

      @Override public void fail(Exception e) {
        mAdapter.loadMoreFail();
        Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG)
            .show();
      }
    }).start();
  }

  private void setData(boolean isRefresh, List data) {
    mNextRequestPage++;
    final int size = data == null ? 0 : data.size();
    if (isRefresh) {
      mAdapter.setNewData(data);
    } else {
      if (size > 0) {
        mAdapter.addData(data);
      }
    }
    if (size < PAGE_SIZE) {
      //第一页如果不够一页就不显示没有更多数据布局
      mAdapter.loadMoreEnd(isRefresh);
      Toast.makeText(this, "no more data", Toast.LENGTH_SHORT).show();
    } else {
      mAdapter.loadMoreComplete();
    }
  }
}