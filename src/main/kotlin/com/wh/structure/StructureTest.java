package com.wh.structure;

public class StructureTest {


    public static void main(String[] args) {
        initBinaryTree();

    }

    private static void initBinaryTree() {
        BinaryTree<String, String> tree = new BinaryTree<String, String>();
        tree.put("baobao", "nanbaobao");
        tree.put("wanghao", "wanghao");
        tree.put("wangzhi", "wanghao");
        String result = tree.put("baobao", "lalala");
        System.out.println(result);

        String value = tree.get("baobao");

        System.out.println(value);


        System.out.println("size = " + tree.getSize());
    }


}
