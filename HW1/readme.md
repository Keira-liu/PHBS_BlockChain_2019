# PHBS_BlockChain_2019
Name:刘安屹  <br>
Student ID:1801212888



  - Summary
  - Testing Part
 


## Summary
### TxHandler is a class to handler the trasnaction of Scrooge Coin, the main function of which is to Verify the validness of the transaction and process the transaction to generate a ledger record.
### I designed 3 methods
- *public boolean isValidTx(Transaction tx)* is to verify the transaction, returning true if the tx meet the 5 required rules.
- *public Transaction[] handleTxs(Transaction[] possibleTxs)* is to remove teh utxo that has been redeemed in the trasnactions as well as the new utxo produced by the transactions.
- *public boolean assetverify(UTXO utxo)* is to assert that the utxoPool contains the input utxo, making preparations for the testing code.



## Testing part

### In order to test whether the TxHandler has satisfied the targets:
>(1)all outputs claimed by tx are in the current UTXO pool  <br>
>(2)the signatures on each input of tx are valid  <br>
>(3)no UTXO is claimed multiple times by tx    <br>
>(4)all of tx’s output values are non-negative  <br>
>(5)the sum of tx’s input values is greater than or equal to the sum of its output values; and false otherwise

1.I build a class to generate keys of four users A,B,C,D as well as four arrays to store the hashes of transactions;  <br>
2.In the @Before function, I make four transactions to give users A,B,C,D 10 coins respectively and add those utxos into the UTXOPool for testing in the next steps;  <br>

3.I have designed following 6 senarios to test:
>(1)public void testIsValidTx1()  <br>
    verify that a normal transaction is allowed  <br>
>(2)public void testIsValidTx2()  <br>
   verify a transaction with a input that is not in the current UTXOpool will be rejected  <br>
>(3)public void testIsValidTx3()  <br>
   verify that a transaction with a invalid signature will be rejected   <br>
>(4)public void testIsValidTx4()  <br>
   verify that no UTXO can be claimed multiple times by tx  <br>
>(5)public void testIsValidTx5() <br>
   verify that a transaction with a negative output will be rejected  <br>
>(6)public void testIsValidTx6()  <br>
   verify that a transaction whose sum of input values is smaller than the sum of its output values will be rejected  <br>

4.Results of testing:The functions pass all the tests above, with expected results returning:
![](https://github.com/Keira-liu/PHBS_BlockChain_2019/raw/master/Testing%20Results.png)
