## Merkle Tree Implementation 

### Technology used 
- Java 17 
- JUnit 5 test cases 

### Implementation details 

- The MerkleTree class has 2 properties 
  1. Node root 
  2. List of Nodes - leaves for the tree

- Methods Implemented 

  1. generateTree - Method to generate tree this is called when the object of the merkle tree is created
  2. generateProof - Method to generate proof for a data
  3. validateProof - Method to validate the proof generated for the data
  4. updateData - Method to update the data and regenerate the tree
  5. insertData - Method to insert new data in the tree 
  6. removeData - Method to remove data from the tree