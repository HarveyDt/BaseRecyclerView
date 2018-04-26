
因满足业务需求 RecyclerViewBaseAdapter 是对 BaseRecyclerViewAdapterHelper开源代码的二次封装。

## 扩展库
[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)(强烈推荐使用此适配器，可大大减少工作量。当前demo使用的是v2.1.0。)

首先在项目中增加如下依赖：

```groovy

dependencies {
    compile 'com.github.HarveyDt:BaseRecyclerView:1.0'
}

```
上拉加载：

如果加载失败需要滑动加载功能 需要引用LoadMoreRecyclerView，LoadMoreRecyclerView监听了滑动事件实现了加载失败上拉重新加载

分组吸顶效果：
```
     mRecyclerView.addItemDecoration(
             // 设置粘性标签对应的类型
             new PinnedHeaderItemDecoration.Builder(StockEntity.StockInfo.TYPE_HEADER)
             // 设置分隔线资源ID
             .setDividerId(R.drawable.divider)
             // 开启绘制分隔线，默认关闭
             .enableDivider(true)
             // 通过传入包括标签和其内部的子控件的ID设置其对应的点击事件
             .setClickIds(R.id.iv_more)
             // 是否关闭标签点击事件，默认开启
             .disableHeaderClick(false)
             // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
             .setHeaderClickListener(clickAdapter)
             .create());

```

Adapter需要重写这两个方法，用于处理GridLayoutManager和StaggeredGridLayoutManager模式下的头部使之占满一行
```
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, StockEntity.StockInfo.TYPE_HEADER);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, StockEntity.StockInfo.TYPE_HEADER);
    }
```




