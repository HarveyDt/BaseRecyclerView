package com.chad.library.adapter.base.listener;

import android.view.View;

/**
 * 顶部标签监听
 *
 * @author yuyang
 *         2018/4/25
 *         上午9:14
 */

public interface OnHeaderClickListener {

  void onHeaderClick(View view, int id, int position);

  void onHeaderLongClick(View view, int id, int position);

  void onHeaderDoubleClick(View view, int id, int position);
}
