概述

因满足业务需求 RecyclerViewBaseAdapter 是对 BaseRecyclerViewAdapterHelper开源代码的二次封装。

封装后demo地址 https://github.com/HarveyDt/BaseRecyclerView

源码参考地址 https://github.com/CymChad/BaseRecyclerViewAdapterHelper
他能做什么？

    优化Adapter代码（减少百分之70%代码）

    添加点击item点击、长按事件、以及item子控件的点击事件

    添加加载动画（一行代码轻松切换5种默认动画）

    添加头部、尾部、下拉刷新、上拉加载（感觉又回到ListView时代）

    设置自定义的加载更多布局

    添加分组（随心定义分组头部）

    自定义不同的item类型（简单配置、无需重写额外方法）

    设置空布局（比Listview的setEmptyView还要好用！）

如何使用他创建Adapter?


public class QuickAdapter extends BaseQuickAdapter<Status> {
    public QuickAdapter() {
        super(R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
                 Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));
    }
}

 
使用

首先需要继承BaseQuickAdapter,然后BaseQuickAdapter<Status, BaseViewHolder>第一个泛型Status是数据实体类型，第二个BaseViewHolder是ViewHolder其目的是为了支持扩展ViewHolder。
赋值

可以直接使用viewHolder对象点相关方法通过传入viewId和数据进行，方法支持链式调用。如果是加载网络图片或自定义view可以通过viewHolder.getView(viewId)获取该控件。
Item的点击事件

 mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

  @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    Log.d(TAG, "onItemClick: ");

  }

});

 
Item的长按事件

mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {

  @Override public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    Log.d(TAG, "onItemLongClick: ");

    return true;

  }

});

 
Item子控件的点击事件

首先在adapter的convert方法里面通过viewHolder.addOnClickListener绑定一下的控件id

@Override
protected void convert(BaseViewHolder helper, Status item) {
 
    helper.addOnClickListener(R.id.btn).addOnClickListener(R.id.btn);

}

 

然后在设置

mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

 

  @Override public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    Log.d(TAG, "onItemChildClick: ");

  }

});

mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {

  @Override

 public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    Log.d(TAG, "onItemChildLongClick: ");

    return true;

  }

});

 
开启动画(默认为渐显效果)

adapter.openLoadAnimation();
默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）
添加头部、尾部
添加

mQuickAdapter.addHeaderView(getView()); mQuickAdapter.addFooterView(getView());
删除指定view

mQuickAdapter.removeHeaderView(getView);
mQuickAdapter.removeFooterView(getView);

删除所有

mQuickAdapter.removeAllHeaderView();
mQuickAdapter.removeAllFooterView();
 

上拉加载

 mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {

  @Override public void onLoadMoreRequested() {

   

  }

},mRecyclerView);

如果加载失败需要滑动加载功能 需要引用LoadMoreRecyclerView，LoadMoreRecyclerView监听了滑动事件实现了加载失败上拉重新加载

<com.chad.library.adapter.base.LoadMoreRecyclerView

 android:id="@+id/rv_list"

 android:layout_width="match_parent"

 android:layout_height="match_parent"/>

加载完成（注意不是加载结束，而是本次数据加载结束并且还有下页数据）

mQuickAdapter.loadMoreComplete();

加载失败

mQuickAdapter.loadMoreFail();

 

加载结束

mQuickAdapter.loadMoreEnd();
 

注意：如果上拉结束后，下拉刷新需要再次开启上拉监听，需要使用setNewData方法填充数据。


打开或关闭加载（一般用于下拉的时候做处理，因为上拉下拉不能同时操作）

mQuickAdapter.setEnableLoadMore(boolean);
 

预加载

// 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
mQuickAdapter.setPreLoadNumber(int);
 

设置自定义加载布局

mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());

多布局

 
实体类必须实现MultiItemEntity，在设置数据的时候，需要给每一个数据设置itemType

public class MultipleItem implements MultiItemEntity {
  public static final int TEXT = 1;
  public static final int IMG = 2;
  private int itemType;

  public MultipleItem(int itemType) {
    this.itemType = itemType;
  }

  @Override public int getItemType() {
    return itemType;
  }
}


在构造里面addItemType绑定type和layout的关系

public class MultipleItemQuickAdapter
    extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {
  public MultipleItemQuickAdapter(List data) {
    super(data);
    addItemType(MultipleItem.TEXT, R.layout.text_view);
    addItemType(MultipleItem.IMG, R.layout.image_view);
  }

  @Override protected void convert(BaseViewHolder helper, MultipleItem item) {
    switch (helper.getItemViewType()) {
      case MultipleItem.TEXT:
        helper.setImageUrl(R.id.tv, item.getContent());
        break;
      case MultipleItem.IMG:
        helper.setImageUrl(R.id.iv, item.getContent());
        break;
    }
  }
}
分组吸顶 悬浮效果

RecyclerView只需要添加一个PinnedHeaderItemDecoration


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
 .setHeaderClickListener(clickAdapter).create());


Adapter需要重写这两个方法，用于处理GridLayoutManager和StaggeredGridLayoutManager模式下的头部使之占满一行

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD);
    }

 