package com.chad.library.adapter.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

/**
 * 加载更多RecyclerView
 *
 * @author yuyang
 *         2018/4/18
 *         下午5:30
 */

public class LoadMoreRecyclerView extends RecyclerView {
  private LoadMoreView mLoadMoreView = LoadMoreView.getInstance();

  public LoadMoreRecyclerView(Context context) {
    this(context, null);
  }

  public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    addOnScrollListener(new RecyclerView.OnScrollListener() {

      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL
            && newState == RecyclerView.SCROLL_STATE_IDLE) {
          mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
          startLoadMore();
        }
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });
  }

  public void startLoadMore() {
    if (getAdapter() instanceof BaseQuickAdapter) {
      ((BaseQuickAdapter) getAdapter()).startLoadMore();
    }
  }
}
