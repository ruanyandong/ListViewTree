package com.example.ai.customtreelist.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.ai.customtreelist.utils.Node;
import com.example.ai.customtreelist.utils.TreeHelper;

import java.util.List;

public abstract class TreeListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;

    protected LayoutInflater mInflater;
    protected ListView mTreeView;

    private OnTreeNodeClickListener mListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener listener){
        this.mListener = listener;
    }

    public abstract void addExtraNode(int position, String s);

    public interface OnTreeNodeClickListener{
        void onClick(Node n,int position);
    }

    public TreeListViewAdapter(ListView treeView,Context context, List<T> datas,int defaultExpandLevel) throws Exception {
        this.mTreeView = treeView;
        this.mContext = context;
        this.mAllNodes = TreeHelper.getSortedNodes(datas,defaultExpandLevel);
        this.mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        mInflater = LayoutInflater.from(mContext);

        mTreeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);

                if (mListener != null){
                    mListener.onClick(mVisibleNodes.get(position),position);
                }
            }

        });

    }

    /**
     * 点击收缩或者展开
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node n = mVisibleNodes.get(position);
        if (n!=null){
            if (n.isLeaf()) return;
            n.setExpand(!n.isExpand());

            mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node,position,convertView,parent);
        /**
         * 设置padding
         */
        convertView.setPadding(node.getLevel()*30,3,3,3);
        return convertView;
    }

    public abstract View getConvertView(Node node,int position,View convertView,ViewGroup parent);

}
