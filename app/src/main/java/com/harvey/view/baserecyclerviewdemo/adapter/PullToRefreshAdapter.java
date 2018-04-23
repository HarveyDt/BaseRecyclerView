package com.harvey.view.baserecyclerviewdemo.adapter;

import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.harvey.view.baserecyclerviewdemo.R;
import com.harvey.view.baserecyclerviewdemo.entity.Status;
import com.harvey.view.baserecyclerviewdemo.util.SpannableStringUtils;
import com.harvey.view.baserecyclerviewdemo.util.ToastUtils;
import com.harvey.view.baserecyclerviewdemo.util.Utils;

/**
 * 加载更多Adapter
 *
 * @author yuyang
 *         2018/4/18
 *         下午5:30
 */

public class PullToRefreshAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    public PullToRefreshAdapter() {
        super( R.layout.layout_animation, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        switch (helper.getLayoutPosition()%
                3){
            case 0:
                helper.setImageResource(R.id.img,R.mipmap.animation_img1);
                break;
            case 1:
                helper.setImageResource(R.id.img,R.mipmap.animation_img2);
                break;
            case 2:
                helper.setImageResource(R.id.img,R.mipmap.animation_img3);
                break;
        }
        helper.addOnClickListener(R.id.btn).addOnClickListener(R.id.btn2);
        helper.addOnLongClickListener(R.id.btn).addOnLongClickListener(R.id.btn2);
        helper.setText(R.id.tweetName,"Hoteis in Rio de Janeiro");
        String msg="\"He was one of Australia's most of distinguished artistes, renowned for his portraits\"";
        ( (TextView)helper.getView(R.id.tweetText)).setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
        ( (TextView)helper.getView(R.id.tweetText)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            ToastUtils.showShortToast("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Utils.getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };


}
