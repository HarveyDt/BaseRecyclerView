package com.harvey.view.baserecyclerviewdemo.loadmore;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.harvey.view.baserecyclerviewdemo.R;

/**
 * 自定义加载更多View
 *
 * @author yuyang
 *         2018/4/18
 *         下午5:30
 */


public final class CustomLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
