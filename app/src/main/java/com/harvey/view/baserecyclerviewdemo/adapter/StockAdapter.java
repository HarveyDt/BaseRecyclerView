package com.harvey.view.baserecyclerviewdemo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.harvey.view.baserecyclerviewdemo.PinnedSectionActivity;
import com.harvey.view.baserecyclerviewdemo.R;
import com.harvey.view.baserecyclerviewdemo.entity.StockEntity;
import com.chad.library.adapter.base.util.FullSpanUtil;
import java.util.List;


/**
 * 继承BaseMultiItemQuickAdapter的一个适配器基类
 *
 * @author yuyang
 *         2018/4/25
 *         上午9:14
 */
public  class StockAdapter
    extends BaseMultiItemQuickAdapter<StockEntity.StockInfo, BaseViewHolder> {

  public static final int TYPE_HEADER = 1;
  public static final int TYPE_DATA = 2;

  public StockAdapter(List<StockEntity.StockInfo> data) {
    super(data);
    addItemType(StockEntity.StockInfo.TYPE_HEADER, R.layout.item_stock_header);
    addItemType(StockEntity.StockInfo.TYPE_DATA, R.layout.item_stock_data);
  }
  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_HEADER);
  }

  @Override public void onViewAttachedToWindow(BaseViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_HEADER);
  }

  @Override protected void convert(BaseViewHolder holder, StockEntity.StockInfo item) {
    switch (holder.getItemViewType()) {

      case StockEntity.StockInfo.TYPE_HEADER:
        holder.setText(R.id.tv_stock_name, item.pinnedHeaderName)
            .addOnClickListener(R.id.checkbox)
            .setChecked(R.id.checkbox, item.check);
        break;

      case StockEntity.StockInfo.TYPE_DATA:

        final String stockNameAndCode = item.stock_name + "\n" + item.stock_code;
        SpannableStringBuilder ssb = new SpannableStringBuilder(stockNameAndCode);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#a4a4a7")), item.stock_name.length(),
            stockNameAndCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(
            new AbsoluteSizeSpan(PinnedSectionActivity.dip2px(holder.itemView.getContext(), 13)),
            item.stock_name.length(), stockNameAndCode.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.setText(R.id.tv_stock_name_code, ssb)
            .setText(R.id.tv_current_price, item.current_price)
            .setText(R.id.tv_rate, (item.rate < 0 ? String.format("%.2f", item.rate)
                : "+" + String.format("%.2f", item.rate)) + "%");
        break;
    }
  }
}
