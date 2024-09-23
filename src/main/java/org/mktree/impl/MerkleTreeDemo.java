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
        System.out.println("The Merkle Tree with initial data: ");
        tree.printLevelOrderTraversal();

        String targetData = "C++";

        // Generate proof for the target data block
        List<String> proof = MerkleTree.generateProof(root, targetData);

        boolean isValid = tree.verifyProof(root.getHash(), targetData, proof);
        System.out.println("Proof is valid: " + isValid);

        String newData = "SQL";
        tree.updateNode(targetData,newData);
        System.out.printf("The Merkle Tree after updating %s    TO      %s: \n",targetData,newData);
        tree.printLevelOrderTraversal();

        String insertData1 = "Rust";

        tree.insertData(insertData1);
        System.out.printf("The Merkle Tree after inserting %s: \n",insertData1);
        tree.printLevelOrderTraversal();

        String insertData2 = "Swift";
        tree.insertData(insertData2);
        System.out.printf("The Merkle Tree after inserting %s: \n",insertData2);
        tree.printLevelOrderTraversal();
        String rootHashAfterInsertData2 = tree.getRoot().getHash();
        String insertData3 = "Solidity";

        tree.insertData(insertData3);
        System.out.printf("The Merkle Tree after inserting %s: \n",insertData3);
        tree.printLevelOrderTraversal();

        tree.removeData(insertData3);
        String rootHashAfterRemovingInsertData3 = tree.getRoot().getHash();
        System.out.printf("The Merkle Tree after removing %s: \n",insertData3);
        tree.printLevelOrderTraversal();

        System.out.printf("Root hash before removing %s is %s \n",insertData3,rootHashAfterInsertData2);
        System.out.printf("Root hash after removing %s is %s \n",insertData3,rootHashAfterInsertData2);
        System.out.printf("Root hash before after are equal ? %s \n",rootHashAfterInsertData2.equals(rootHashAfterRemovingInsertData3));


    }
}
