


# Homework 2: Block Chain


Name:刘安屹  <br>
Student ID:1801212888



  - Summary
  - Testing Part

## Summary
#### BlockChain is a class to receive incoming transactions and blocks and maintain and updated block chain with the new block created in the class BlockHandler.
##### I designed a class BlockNode to store the block and its height,utxopool, transactionpool as well as its creating time
##### I designed 7 main methods:
- *public void updateMaxHeightNode()*  is to update the maxHeightNode with the biggest height.If there are multiple blocks at the same height, return return the oldest block.
- *public BlockChain(Block genesisBlock)*  is to create an empty block chain with just a genesis block.
- *public Block getMaxHeightBlock()*  is toet the maximum height block.
- *public UTXOPool getMaxHeightUTXOPool()*  is to get the UTXOPool for mining a new block on top of max height block.
- *public TransactionPool getTransactionPool()*  is to get the transaction pool to mine a new block.
- *public boolean addBlock(Block block)*  is to add a new valid block to the Block Chain.
- *public void addTransaction(Transaction tx)*  is to Add a transaction to the transaction pool.




## Testing part

#### In order to test whether the BlockChain has satisfied the targets:
>(1)A geneis block can be created and added to a new blockchain while can not be added to an exsiting blockchian.<br>
>(2)If there are multiple blocks at the same height, return the oldest block in getMaxHeightBlock() function <br>
>(3)A coinbase transaction of a block is available to be spent in the next block mined on top of it. <br>
>(4)Transactions will be removed from the Transaction Pool once it have been included in a new valid block. <br>
>(5)a block with invalid transactions or wrong previous hash cannot be added to a BlockChain. <br>
>(6)only the valid transaction whose {height > (maxHeight - CUT_OFF_AGE)} can be added to the BlockChain.

1. I define a transaction that spend all 25 coins from the CoinBase of the given block. <br>
2. I also define a signature to sign transaction as well as a keypair generator to generate random keypair.<br>

3. I have designed following 8 senarios to test:
>(1)public void test1()  <br>
   Create a gensisBlock and adding it to a new BlockChain.  <br>
>(2)public void test2()  <br>
   Add a new block Block1 on the genesis block, with a transaction spending all coins produced by CoinBase.<br> 
>(3)public void test3()  <br>
   Verifying that  a coinbase transaction of a block is available to be spent in the next block mined on top of it   <br>
>(4)public void test4()  <br>
   Verifying that a transaction will be removed from the TransactionPool after being added in a new block <br>
>(5)public void test5() <br>
   verify that a transaction with a negative output will be rejected  <br>
>(6)public void test6() <br>
   Verifying that if there are multiple blocks at the same height, upadate the currentMaxHeightNode with the oldest block. <br>
>(7)public void test7() <br>
  * Verifying that a genesis block cannot be added to an existing BlockChian. <br>
  * verifying that a block with invalid transactions cannot be added to a BlockChain. <br>
  * verifying that a block with wrong previous hash cannot be added to a BlockChain. <br>
  
>(8)public void test8() <br>
   In order to save memory, only the valid transaction whose {@code height > (maxHeight - CUT_OFF_AGE)} can be added to the BlockChain.
   

4.Results of testing:The functions pass all the tests above, with expected results returning:
![image](https://github.com/Keira-liu/PHBS_BlockChain_2019/blob/master/HW2/TestResult.png)
