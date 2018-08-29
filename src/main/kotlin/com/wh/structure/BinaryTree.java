package com.wh.structure;

public class BinaryTree<K, V> {

    private Node<K, V> root;

    private int size = 0;
    private int modCount = 0;

    static class Node<K, V> {
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        K key;
        V value;

        Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        Node<K, V> t = root;
        if (t == null) { //empty tree
            root = new Node<K, V>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        if (key == null) throw new NullPointerException("key is null");
        Node<K, V> parent;
        int cmp;
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp > 0) { // key > parent
                t = t.right;
            } else if (cmp < 0) {
                t = t.left;
            } else {
                final V old = t.value;
                t.value = value;
                return old;
            }
        } while (t != null);
        Node<K, V> entry = new Node<K, V>(key, value, parent);
        if (cmp > 0) { //放到右边
            parent.right = entry;
        } else {
            parent.left = entry;
        }
        size++;
        modCount++;
        return null;
    }

    public V get(K key) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Node<K, V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp > 0) {
                p = p.right;
            } else if (cmp < 0) {
                p = p.left;
            } else {
                return p.value;
            }
        }
        return null;
    }
}
