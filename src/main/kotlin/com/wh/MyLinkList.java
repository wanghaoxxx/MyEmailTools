package com.wh;

public class MyLinkList<E> {

    private LinkNode<E> header = null;
    private int size;

    static class LinkNode<E> {
        LinkNode<E> prev;
        LinkNode<E> next;
        E e;

        @Override
        public String toString() {
            return "prev = " + (prev != null ? prev.hashCode() : "null") + " next = " + (next != null ? next.hashCode() : "null") + " value = " + e;
        }
    }

    public void add(E e) {
        if (e == null) return;
        LinkNode<E> node = new LinkNode<E>();
        node.e = e;
        node.next = header;
        if (header != null) {
            header.prev = node;
        }
        header = node;
        size++;
    }

    public void remove(E e) {
        for (LinkNode<E> node = header; node != null; node = node.next) {
            if (node.e.equals(e)) {
                LinkNode<E> prev = node.prev;
                LinkNode<E> next = node.next;

                if (prev == null) {
                    header = next;
                } else {
                    prev.next = next;
                }

                if (next != null) {
                    next.prev = prev;
                }
                node.next = null;
                node.prev = null;
                node.e = null;
                size--;
            }
        }
    }

    public int size() {
        return size;
    }

    private LinkNode<E> findNode(E e) {

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (LinkNode node = header; node != null; node = node.next) {
            sb.append(node.toString());
        }
        sb.append(" size = ");
        sb.append(size);
        return sb.toString();
    }
}
