package com.example.vocabulary;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements AbsListView.OnScrollListener {
    private LinearLayout mLoadLayout;
    private ListView mListView;
    private ListViewAdapter mListViewAdapter = new ListViewAdapter();
    private int mLastItem = 0;
    private int mCount = 41;
    private final Handler mHandler = new Handler();
    private final LinearLayout.LayoutParams mProgressBarLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * "加载项"布局，此布局被添加到ListView的Footer中。
         */
        mLoadLayout = new LinearLayout(this.getActivity());
        mLoadLayout.setMinimumHeight(60);
        mLoadLayout.setGravity(Gravity.CENTER);
        mLoadLayout.setOrientation(LinearLayout.HORIZONTAL);
        /**
         * 向"加载项"布局中添加一个圆型进度条。
         */
        ProgressBar mProgressBar = new ProgressBar(this.getActivity());
        mProgressBar.setPadding(0, 0, 15, 0);
        mLoadLayout.addView(mProgressBar, mProgressBarLayoutParams);
        /**
         * 向"加载项"布局中添加提示信息。
         */
        TextView mTipContent = new TextView(this.getActivity());
        mTipContent.setText("加载中...");
        mLoadLayout.addView(mTipContent, mTipContentLayoutParams);
        /**
         * 获取ListView组件，并将"加载项"布局添加到ListView组件的Footer中。
         */
        mListView = view.findViewById(R.id.list);
        mListView.addFooterView(mLoadLayout);

        List<HashMap<String,String>> data = new ArrayList<>();
        for(int i = 1; i <=41; i++) {    //创建 18  个 map 数据对象 ，每个map 对象 有两个键值数据
            //创建HashMap 对象,添加键值数据
            HashMap<String,String> map = new HashMap<>();
            //向map 对象添加两组键值对数据
            map.put("key_one","dataOne_" + i);
            map.put("key_two","dataTwo_" + i);
            map.put("key_three","dataThree_" + i);
            //将 map 对象添加到data  集合
            data.add(map);
        }
        /**
         * 组ListView组件设置Adapter,并设置滑动监听事件。
         */
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnScrollListener(this);


    }

    public void onScroll(AbsListView view, int mFirstVisibleItem,
                         int mVisibleItemCount, int mTotalItemCount) {
        mLastItem = mFirstVisibleItem + mVisibleItemCount - 1;
        if (mListViewAdapter.count > mCount) {
            mListView.removeFooterView(mLoadLayout);
        }
    }
    public void onScrollStateChanged(AbsListView view, int mScrollState) {

        /**
         * 当ListView滑动到最后一条记录时这时，我们会看到已经被添加到ListView的"加载项"布局， 这时应该加载剩余数据。
         */
        if (mLastItem == mListViewAdapter.count
                && mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (mListViewAdapter.count <= mCount) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListViewAdapter.count += 10;
                        mListViewAdapter.notifyDataSetChanged();
                        mListView.setSelection(mLastItem);
                    }
                }, 1000);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }


    class ListViewAdapter extends BaseAdapter {
        int count = 10;
        public int getCount() {
            return count;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View view, ViewGroup parent) {
            TextView mTextView;
            if (view == null) {
                mTextView = new TextView(getActivity());
            } else {
                mTextView = (TextView) view;
            }
            mTextView.setText("Item " + position);
            mTextView.setTextSize(20f);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setHeight(60);
            return mTextView;
        }
    }
}
