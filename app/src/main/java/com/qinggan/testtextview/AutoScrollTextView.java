package com.qinggan.testtextview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * Description: 一个可以向上滚动展示文本的控件
 * Author: Seven
 * Mail: huangyawen.happy@gmail.com
 * Date: 17-9-7.
 *
 */

public class AutoScrollTextView extends LinearLayout
{
    private static final String TAG = "test";
    private static final int UPDATE_CONTENT = 0x1003;
    private Context context;
    private List<String> contentList;
    private TextView contentTv;
    private TextView nextContentTv;
    boolean isRunFlag;
    private int contentWidth;
    private int index;
    private int scrollInterval;
    private String content;
    private Animation contentAnimation;
    private Animation nextContentAnimation;


    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_CONTENT:
                    updateContent();
                    break;
            }
        }
    };

    private void updateContent()
    {
        if(isRunFlag)
        {
            contentTv.setText(contentList.get(index));
            contentTv.startAnimation(contentAnimation);
            nextContentTv.startAnimation(nextContentAnimation);
        }
    }

    public AutoScrollTextView(Context context)
    {
        this(context, null);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
        initView();
        initAnimation();

    }

    private void initAnimation()
    {
        contentAnimation = AnimationUtils.loadAnimation(context,
                R.anim.content_translate);
        contentAnimation.setFillAfter(true);


        nextContentAnimation = AnimationUtils.loadAnimation(context,
                R.anim.content_next_translate);
        nextContentAnimation.setFillAfter(true);
        nextContentAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                Log.d(TAG, "onAnimationStart");
                nextContentTv.setText(contentList.get(index + 1));
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                Log.d(TAG, "onAnimationEnd");
                index++;
                if(index < contentList.size() - 1)
                {
                    mHandler.sendEmptyMessageDelayed(UPDATE_CONTENT, scrollInterval);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });


    }

    private void init(Context context)
    {
        this.context = context;
        contentList = new ArrayList<>();
        scrollInterval = 2000;
    }

    private void initView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.auto_scroll_item, this, true);
        contentTv = (TextView) view.findViewById(R.id.tv_content);
        nextContentTv = (TextView) view.findViewById(R.id.tv_next_content);
    }


    private void resetAnimation()
    {
        contentAnimation.reset();
        nextContentAnimation.reset();
        mHandler.removeMessages(UPDATE_CONTENT);
    }

    private void spliteContent()
    {
        StaticLayout myStaticLayout = new StaticLayout(content, contentTv.getPaint(), contentWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int count = myStaticLayout.getLineCount();
        for(int i = 0; i < count; i++)
        {
            int start = myStaticLayout.getLineStart(i);
            int end = myStaticLayout.getLineEnd(i);
            String line = content.substring(start, end);
            contentList.add(line);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentTv.getMeasuredWidth();
    }

    /**
     * 停止滚动
     */
    public void cancel()
    {
        isRunFlag = false;
        contentAnimation.cancel();
        nextContentAnimation.cancel();
        mHandler.removeMessages(UPDATE_CONTENT);
    }

    /**
     * 设置文本
     * @param text 文本
     */
    public void setText(String text)
    {
        if(!TextUtils.isEmpty(text))
        {
            content = text;
            spliteContent();
            index = 0;
            isRunFlag = true;
            if (contentList.size() > 1)
            {
                resetAnimation();
                mHandler.sendEmptyMessage(UPDATE_CONTENT);
            }
            else
            {
                contentTv.setText(contentList.get(0));
            }
        }
    }

    /**
     * 设置滚动间隔，单位毫秒
     * @param interval 滚动间隔
     */
    public void setScrollInterval(int interval)
    {
        scrollInterval = interval;
    }
}
