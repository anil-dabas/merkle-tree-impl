package org.mktree.impl;

public class Node {

    private Node left;
    private Node right;
    private String hash;

    public Node(Node left, Node right, String hash) {
        this.left = left;
        this.right = right;
        this.hash = hash;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getHash() {
        return hash;
    }

}
