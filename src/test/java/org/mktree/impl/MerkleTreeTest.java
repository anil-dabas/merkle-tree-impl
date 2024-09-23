package org.mktree.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class MerkleTreeTest {

    private MerkleTree merkleTree;

    @BeforeEach
    void setUp() {
        // Initializing the Merkle tree with sample data
        List<String> data = Arrays.asList("data1", "data2", "data3", "data4");
        merkleTree = new MerkleTree(data);
    }

    @Test
    void testTreeInitialization() {
        // Test that the tree initializes and the root is not null
        assertNotNull(merkleTree.getRoot(), "The root of the Merkle tree should not be null.");
    }

    @Test
    void testGenerateProof() {
        // Generate a proof for "data2" and verify that the proof is not empty
        String targetData = "data2";
        List<String> proof = MerkleTree.generateProof(merkleTree.getRoot(), targetData);
        assertFalse(proof.isEmpty(), "Proof should not be empty for valid data.");
    }

    @Test
    void testVerifyProof() {
        // Generate proof for "data2"
        String targetData = "data2";
        List<String> proof = MerkleTree.generateProof(merkleTree.getRoot(), targetData);
        String rootHash = merkleTree.getRoot().getHash();

        // Verify that the proof for "data2" is valid
        boolean isValid = merkleTree.verifyProof(rootHash, targetData, proof);
        assertTrue(isValid, "Proof verification should succeed for correct proof.");
    }

    @Test
    void testProofVerificationFailsWithIncorrectData() {
        // Generate proof for "data2" but verify against different data
        String targetData = "data2";
        List<String> proof = MerkleTree.generateProof(merkleTree.getRoot(), targetData);
        String rootHash = merkleTree.getRoot().getHash();

        // Attempt to verify with wrong data
        boolean isValid = merkleTree.verifyProof(rootHash, "incorrectData", proof);
        assertFalse(isValid, "Proof verification should fail for incorrect data.");
    }

    @Test
    void testUpdateNode() {
        // Update "data2" to "newData"
        String targetData = "data2";
        String newData = "newData";
        merkleTree.updateNode(targetData, newData);

        // Verify that the leaf node has the new data's hash
        List<String> proof = MerkleTree.generateProof(merkleTree.getRoot(), newData);
        assertFalse(proof.isEmpty(), "Proof should be generated for updated node.");

        // Verify that proof for the updated data is valid
        boolean isValid = merkleTree.verifyProof(merkleTree.getRoot().getHash(), newData, proof);
        assertTrue(isValid, "Proof verification should succeed for updated data.");
    }

    @Test
    void testInsertData() {
        // Insert new data "data5" and verify that the tree is updated
        String newData = "data5";
        merkleTree.insertData(newData);

        // Generate proof for the newly inserted data
        List<String> proof = MerkleTree.generateProof(merkleTree.getRoot(), newData);
        assertFalse(proof.isEmpty(), "Proof should be generated for newly inserted data.");

        // Verify the proof for the new data
        boolean isValid = merkleTree.verifyProof(merkleTree.getRoot().getHash(), newData, proof);
        assertTrue(isValid, "Proof verification should succeed for newly inserted data.");
    }

    @Test
    void testRemoveData() {
        // Remove "data3" and verify the tree structure
        String targetData = "data3";
        merkleTree.removeData(targetData);

        // Verify that proof for removed data throws an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MerkleTree.generateProof(merkleTree.getRoot(), targetData));
        assertEquals("Leaf node for the target data block not found.", exception.getMessage());
    }

    @Test
    void testPrintLevelOrderTraversal() {
        // Test the level order traversal printing (manually verified via console output)
        merkleTree.printLevelOrderTraversal();
    }
}
