package org.mktree.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.mktree.impl.HashGenerator.generateHash;


public class MerkleTree {

    private Node root;
    private final List<Node> leaves;


    public MerkleTree(List<String> data) {
        this.leaves = getLeafNodes(data);
        this.root = generateTree(data);
    }

    public Node getRoot(){
        return root;
    }


    /**
     * The method takes the data as input and generates the Merkle tree for the data
     * @param dataList
     * @return root of the merkle tree
     */
    private Node generateTree(List<String> dataList) {
        List<Node> childNodes = getLeafNodes(dataList);
        return  buildTree(childNodes);
    }

    private static List<Node> getLeafNodes(List<String> dataList) {
        List<Node> childNodes = new ArrayList<>();

        for (String message : dataList) {
            childNodes.add(new Node(null, null, generateHash(message)));
        }
        return childNodes;
    }

    /**
     * Private method to build the tree that takes list of children Hashes and returns the root node after building the tree
     * @param children
     * @return
     */
    private Node buildTree(List<Node> children) {
        ArrayList<Node> parents = new ArrayList<>();

        while (children.size() != 1) {
            int index = 0, length = children.size();
            while (index < length) {
                Node leftChild = children.get(index);
                Node rightChild = null;

                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                } else {
                    rightChild = new Node(null, null, leftChild.getHash());
                }

                String parentHash = generateHash(leftChild.getHash() + rightChild.getHash());
                parents.add(new Node(leftChild, rightChild, parentHash));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    /**
     * Method to generate proof for the targetData using the root node
     * @param root
     * @param targetData
     * @return
     */
    public static List<String> generateProof(Node root, String targetData) {
        // Find the leaf node corresponding to the target data block
        String targetHash = generateHash(targetData);
        Node leafNode = findLeafNode(root, targetHash);

        if (leafNode == null) {
            throw new IllegalArgumentException("Leaf node for the target data block not found.");
        }

        // Generate the proof by traversing from the leaf node to the root
        List<String> proof = new ArrayList<>();
        Node currentNode = leafNode;
        //String currentHash = leafNode.getHash(); // Start with the hash of the leaf node

        while (currentNode != root) {
            Node parent = findParent(root, currentNode);
            if (parent == null) {
                throw new IllegalArgumentException("Parent not found. Invalid tree structure.");
            }

            // Add the sibling's hash to the proof and compute the next level hash
            if (parent.getLeft() == currentNode) {
                proof.add(parent.getRight().getHash());  // Add right sibling hash to proof
            } else {
                proof.add(parent.getLeft().getHash());   // Add left sibling hash to proof
            }
            currentNode = parent;
        }

        return proof;
    }

    private static Node findParent(Node root, Node child) {
        if (root == null || root.getLeft() == null && root.getRight() == null) {
            return null; // Base case: if we reach a leaf or null node
        }

        if (root.getLeft() == child || root.getRight() == child) {
            return root; // Found the parent node
        }

        // Recursively search in the left and right subtrees
        Node leftSearch = findParent(root.getLeft(), child);
        if (leftSearch != null) {
            return leftSearch; // Found in the left subtree
        }

        return findParent(root.getRight(), child); // Otherwise, search in the right subtree
    }

    private static Node findLeafNode(Node node, String targetHash) {
        // Base case: if the node has no children, it is a leaf node
        if (node.getLeft() == null && node.getRight() == null) {
            if (node.getHash().equals(targetHash)) {
                return node; // Leaf node matches the target hash
            } else {
                return null; // Leaf node does not match
            }
        }

        // Recursively check in the left subtree
        if (node.getLeft() != null) {
            Node foundInLeft = findLeafNode(node.getLeft(), targetHash);
            if (foundInLeft != null) {
                return foundInLeft; // If found in the left subtree, return it
            }
        }

        // Recursively check in the right subtree
        if (node.getRight() != null) {
            Node foundInRight = findLeafNode(node.getRight(), targetHash);
            if (foundInRight != null) {
                return foundInRight; // If found in the right subtree, return it
            }
        }

        return null; // Node not found in either subtree
    }

    /**
     * Method to verify the proof generated for the node
     * @param rootHash
     * @param dataBlock
     * @param proof
     * @return
     */
    public boolean verifyProof(String rootHash, String dataBlock, List<String> proof) {
        String currentHash = generateHash(dataBlock);
        int index = leaves.indexOf(dataBlock);
        // Recompute the root hash using the proof
        for (String siblingHash : proof) {
            if(index % 2 ==0){
                currentHash = generateHash(currentHash + siblingHash);
            }else{
                currentHash = generateHash(siblingHash + currentHash);
            }
            index = index/2;
        }

        // Compare the calculated hash with the actual root hash
        return currentHash.equals(rootHash);
    }

    public void printLevelOrderTraversal() {
        if (root == null) {
            return;
        }

        if ((root.getLeft() == null && root.getRight() == null)) {
            System.out.println(root.getHash());
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                System.out.println(node.getHash());
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }

            if (node != null && node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node != null && node.getRight() != null) {
                queue.add(node.getRight());
            }

        }

    }

    public void updateNode(String targetData, String newData) {
        String hashTargetData = generateHash(targetData);
        String hashNewData = generateHash(newData);
        int index = indexOfTargetDataHash(hashTargetData);
        leaves.set(index,new Node(null,null,hashNewData));
        root = buildTree(leaves);
    }


    private int indexOfTargetDataHash(String hashTargetData) {
        for (int i=0; i<leaves.size(); i++){
            if(leaves.get(i).getHash().equals(hashTargetData)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Method to insert new Data to the merkle tree
     * @param insertData
     */
    public void insertData(String insertData) {
        leaves.add(new Node(null,null,generateHash(insertData)));
        root = buildTree(leaves);
    }

    /**
     * Method to remove an existing data from the merkle tree
     * @param removeData
     */
    public void removeData(String removeData) {
        String removeDataHash = generateHash(removeData);
        int index = indexOfTargetDataHash(removeDataHash);
        leaves.remove(index);
        root = buildTree(leaves);
    }
}
