package org.mktree.impl;

import java.util.ArrayList;
import java.util.List;


public class MerkleTreeDemo {

    public static void main(String[] args) {
        ArrayList<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("Java");
        dataBlocks.add("Python");
        dataBlocks.add("Go-Lang");
        dataBlocks.add("Typescript");
        dataBlocks.add("C++");

        MerkleTree tree = new MerkleTree(dataBlocks);

        Node root = tree.getRoot();
        tree.printLevelOrderTraversal();

        String targetData = "C++";

        // Generate proof for the target data block
        List<String> proof = MerkleTree.generateProof(root, targetData);

        boolean isValid = tree.verifyProof(root.getHash(), targetData, proof);
        System.out.println("Proof is valid: " + isValid);

        String newData = "SQL";
        tree.updateNode(targetData,newData);

        tree.printLevelOrderTraversal();

        String insertData1 = "Rust";

        tree.insertData(insertData1);
        tree.printLevelOrderTraversal();

        String insertData2 = "Swift";
        tree.insertData(insertData2);
        tree.printLevelOrderTraversal();

        String insertData3 = "Solidity";

        tree.insertData(insertData3);
        tree.printLevelOrderTraversal();

        String removeData = "Solidity";
        tree.removeData(removeData);
        tree.printLevelOrderTraversal();
    }
}
