package com.example.ai.customtreelist.utils;

import com.example.ai.customtreelist.R;
import com.example.ai.customtreelist.utils.annotation.TreeNodeId;
import com.example.ai.customtreelist.utils.annotation.TreeNodeLabel;
import com.example.ai.customtreelist.utils.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {

    /**
     * 将用户的数据转化为树形数据
     *
     * @param datas
     * @param <T>
     * @return
     */
    private static <T> List<Node> convertDatas2Nodes(List<T> datas) throws Exception {

        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        for (T t : datas) {
            int id = -1;
            int pid = -1;
            String label = null;
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    /**
                     * 如果字段是private,则设置为true，强制调用
                     */
                    field.setAccessible(true);
                    id = field.getInt(t);

                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    /**
                     * 如果字段是private,则设置为true，强制调用
                     */
                    field.setAccessible(true);
                    pid = field.getInt(t);

                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    /**
                     * 如果字段是private,则设置为true，强制调用
                     */
                    field.setAccessible(true);
                    label = (String) field.get(t);

                }

            }
            node = new Node(id, pid, label);
            nodes.add(node);
        }

        /**
         * 设置关联关系
         * 设置Node的节点关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : nodes) {
            setNodeIcon(n);
        }

        return nodes;
    }

    /**
     * 为Node设置图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChildren().size() > 0 && n.isExpand()) {
            n.setIcon(R.drawable.tree_ex);
        } else if (n.getChildren().size() > 0 && !n.isExpand()) {
            n.setIcon(R.drawable.tree_ec);
        } else {
            //没有图标
            n.setIcon(-1);
        }

    }

    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws Exception {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = convertDatas2Nodes(datas);
        /**
         * 得到树的根节点
         */
        List<Node> rootNodes = getRootNodes(nodes);
        for (Node node : rootNodes) {
            /**
             * 默认显示层级为0
             */
            addNode(result, node, defaultExpandLevel, 0);
        }
        return result;
    }

    /**
     * 把一个节点的所有孩子节点都放入result
     *
     * @param result
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }
        if (node.isLeaf()) {
            return;
        }

        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }

    }

    /**
     * 过滤出可见节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {

        List<Node> result=new ArrayList<>();
        for (Node node:nodes) {
            if (node.isRoot() || node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }

        }
        return result;
    }

    /**
     * 从所有节点中过滤出根节点
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {

        List<Node> root = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

}
