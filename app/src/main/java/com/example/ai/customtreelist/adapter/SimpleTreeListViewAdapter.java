package com.example.ai.customtreelist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ai.customtreelist.R;
import com.example.ai.customtreelist.utils.Node;
import com.example.ai.customtreelist.utils.TreeHelper;
import com.example.ai.customtreelist.utils.adapter.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeListViewAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeListViewAdapter(ListView treeView, Context context, List<T> datas, int defaultExpandLevel) throws Exception {
        super(treeView, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            /**
             * false返回R.layout.list_item对应的View，true返回parent
             */
            convertView = mInflater.inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.mIcon = convertView.findViewById(R.id.id_item_icon);
            holder.mText = convertView.findViewById(R.id.id_item_text);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        /**
         * 叶节点，不需要Icon
         */
        if (node.getIcon() == -1){
            holder.mIcon.setVisibility(View.INVISIBLE);
        }else {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(node.getIcon());
        }

        holder.mText.setText(node.getName());

        return convertView;
    }

    private class ViewHolder{
        ImageView mIcon;
        TextView mText;
    }

    /**
     * 动态插入节点
     * @param position
     * @param s
     */
    @Override
    public void addExtraNode(int position, String s) {
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);
        Node extralNode = new Node(-1,node.getId(),s);
        extralNode.setParent(node);
        node.getChildren().add(extralNode);
        mAllNodes.add(indexOf+1,extralNode);
        /**
         * 加完数据后默认展开
         */
        node.setExpand(true);
        extralNode.setExpand(true);

        mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);

        notifyDataSetChanged();



    }
}
