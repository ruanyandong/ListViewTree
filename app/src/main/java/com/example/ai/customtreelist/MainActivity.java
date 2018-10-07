package com.example.ai.customtreelist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ai.customtreelist.adapter.SimpleTreeListViewAdapter;
import com.example.ai.customtreelist.bean.FileBean;
import com.example.ai.customtreelist.bean.OrgBean;
import com.example.ai.customtreelist.utils.Node;
import com.example.ai.customtreelist.utils.adapter.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、原理ListView's Item+paddingLeft(left)+expend include
 * 2、系统中的数据Bean -> Node
 * 3、反射＋注解(注解可以被命名规范所代替)
 * 4、List<T> -> List<Node>
 *     将用户的数据转化为我们的树节点Node
 * 5、设置节点间的关联关系，
 * 6、以及排序
 * 7、过滤出需要现实的数据
 */
public class MainActivity extends AppCompatActivity {

    private ListView mTreeView;
    private SimpleTreeListViewAdapter<?>  mAdapter;
    private List<FileBean> mDatas = new ArrayList<>();

    private List<OrgBean> mDatas2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTreeView = findViewById(R.id.listView);

        initDatas();
        try {
            mAdapter = new SimpleTreeListViewAdapter<>(mTreeView,this,mDatas,1);
            mTreeView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initEvent();
    }

    private void initEvent() {
        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node n, int position) {
                if (n.isLeaf()){
                    Toast.makeText(MainActivity.this, n.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTreeView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final EditText et;
                et = new EditText(MainActivity.this);
                new AlertDialog.
                        Builder(MainActivity.this).
                        setTitle("Add Node").
                        setView(et).
                        setPositiveButton("sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(et.getText().toString())){
                                    return;
                                }
                                mAdapter.addExtraNode(position,et.getText().toString());
                            }
                        }).setNegativeButton("cancel",null)
                        .create().show();
                /**
                 * true就不会触发onItemClick，false会两个都触发，true就把事件消耗了
                 */
                return true;
            }
        });
    }

    private void initDatas() {
        FileBean fileBean= new FileBean(1,0,"根目录1");
        mDatas.add(fileBean);
        fileBean= new FileBean(2,0,"根目录2");
        mDatas.add(fileBean);
        fileBean= new FileBean(3,0,"根目录3");
        mDatas.add(fileBean);
        fileBean= new FileBean(4,1,"根目录1-1");
        mDatas.add(fileBean);
        fileBean = new FileBean(5,1,"根目录1-2");
        mDatas.add(fileBean);
        fileBean = new FileBean(6,5,"根目录1-2-1");
        mDatas.add(fileBean);

        fileBean= new FileBean(7,3,"根目录3-1");
        mDatas.add(fileBean);
        fileBean= new FileBean(8,3,"根目录3-2");
        mDatas.add(fileBean);

        //init mDatas2
        OrgBean orgBean= new OrgBean(1,0,"根目录1");
        mDatas2.add(orgBean);
        orgBean= new OrgBean(2,0,"根目录2");
        mDatas2.add(orgBean);
        orgBean= new OrgBean(3,0,"根目录3");
        mDatas2.add(orgBean);
        orgBean= new OrgBean(4,1,"根目录1-1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(5,1,"根目录1-2");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(6,5,"根目录1-2-1");
        mDatas2.add(orgBean);

        orgBean = new OrgBean(7,3,"根目录3-1");
        mDatas2.add(orgBean);
        orgBean = new OrgBean(8,3,"根目录3-2");
        mDatas2.add(orgBean);
    }

}
