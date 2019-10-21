
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.security.*;

import static org.junit.Assert.assertThat;


/**
* BlockChain Tester. 
* 
* @author <Liu Anyi>
* @since <pre>10ÔÂ 20, 2019</pre> 
* @version 1.0 
*/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



public class BlockChainTest {

    private Transaction TransactionSpendingCoinBase(Block block, KeyPair Miner, KeyPair receiver) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx = new Transaction();
        tx.addInput(block.getCoinbase().getHash(), 0);
        tx.addOutput(25.0D, receiver.getPublic());
        Signature signature = this.TxSignature(tx, Miner);
        tx.addSignature(signature.sign(), 0);
        tx.finalize();
        return tx;
    }


    private Signature TxSignature(Transaction singleInputTx, KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(singleInputTx.getRawDataToSign(0));
        return signature;
    }

    private KeyPair KeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keypair = KeyPairGenerator.getInstance("RSA");
        keypair.initialize(2048, new SecureRandom());
        return keypair.generateKeyPair();
    }

    //Create the genesis block
    @Test
    public void test1() throws Exception {
        KeyPair pair1 = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, pair1.getPublic());
        BlockChain blockchain = new BlockChain(genesisBlock);
        Block maxHeightBlock = blockchain.getMaxHeightBlock();
        Transaction CoinbaseTx = new Transaction(25.0D, pair1.getPublic());

        assertThat(maxHeightBlock.getHash(), CoreMatchers.equalTo((Object)null));
        assertThat(maxHeightBlock.getCoinbase(), CoreMatchers.equalTo(CoinbaseTx));
        assertThat(blockchain.getMaxHeightUTXOPool().getAllUTXO().size(), CoreMatchers.equalTo(1));
        assertThat((UTXO)blockchain.getMaxHeightUTXOPool().getAllUTXO().get(0), CoreMatchers.equalTo(new UTXO(CoinbaseTx.getHash(), 0)));
    }

    //Add a new block Block1 on the genesis block, with a transaction spending all coins produced by CoinBase.
    @Test
    public void test2() throws Exception {
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);

        KeyPair keyPair = this.KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), keyPair.getPublic());
        Transaction transaction1 = this.TransactionSpendingCoinBase(genesisBlock, genesisPair, keyPair);
        Block1.addTransaction(transaction1);
        Block1.finalize();

        assertThat(blockChain.addBlock(Block1), CoreMatchers.equalTo(true));
        assertThat(blockChain.getMaxHeightBlock().getHash(), CoreMatchers.equalTo(Block1.getHash()));
        assertThat(blockChain.getMaxHeightBlock().getCoinbase(), CoreMatchers.equalTo(new Transaction(25.0D, keyPair.getPublic())));
        assertThat(blockChain.getMaxHeightUTXOPool().getAllUTXO().size(), CoreMatchers.equalTo(2));
    }

    //Verifying that  a coinbase transaction of a block is available to be spent in the next block mined on top of it.
    @Test
    public void test3() throws Exception {
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);

        KeyPair KeyPair1 = this.KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();

        KeyPair KeyPair2 = this.KeyPair();
        Block Block2 = new Block(Block1.getHash(), KeyPair2.getPublic());
        Transaction transaction2 = this.TransactionSpendingCoinBase(Block1, KeyPair1, KeyPair2);
        Block2.addTransaction(transaction2);
        Block2.finalize();

        assertThat(blockChain.addBlock(Block1), CoreMatchers.equalTo(true));
        assertThat(blockChain.addBlock(Block2), CoreMatchers.equalTo(true));
        assertThat(blockChain.getMaxHeightBlock().getHash(), CoreMatchers.equalTo(Block2.getHash()));
        assertThat(blockChain.getMaxHeightBlock().getCoinbase(), CoreMatchers.equalTo(new Transaction(25.0D, KeyPair2.getPublic())));
        assertThat(blockChain.getMaxHeightUTXOPool().getAllUTXO().size(), CoreMatchers.equalTo(3));
    }



    //Verifying that a transaction will be removed from the TransactionPool after being added in a new block
    @Test
    public void test4() throws Exception {
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);
        Transaction tx = this.TransactionSpendingCoinBase(genesisBlock, genesisPair, this.KeyPair());
        blockChain.addTransaction(tx);
        KeyPair miner = this.KeyPair();
        Block block = new Block(genesisBlock.getHash(), miner.getPublic());
        block.addTransaction(tx);
        block.finalize();

        assertThat(blockChain.addBlock(block), CoreMatchers.equalTo(true));
        assertThat(blockChain.getTransactionPool().getTransactions().isEmpty(), CoreMatchers.equalTo(true));
    }

    //Verifying that getMaxHeightBlock() function can return the current BlockNode after adding a new block to the BlockChain
    @Test
    public void test5() throws Exception {
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);

        KeyPair KeyPair1 = this.KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();

        KeyPair KeyPair2 = this.KeyPair();
        Block Block2 = new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        Block2.finalize();

        KeyPair KeyPair3 = this.KeyPair();
        Block Block3 = new Block(Block2.getHash(), KeyPair3.getPublic());
        Block3.finalize();

        assertThat(blockchain.addBlock(Block1), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block2), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block3), CoreMatchers.equalTo(true));
        assertThat(blockchain.getMaxHeightBlock().getHash(), CoreMatchers.equalTo(Block3.getHash()));
        assertThat(blockchain.getMaxHeightBlock().getCoinbase(), CoreMatchers.equalTo(new Transaction(25.0D, KeyPair3.getPublic())));
    }

    //Verifying that If there are multiple blocks at the same height, return the oldest block in getMaxHeightBlock() function.
    @Test
    public void test6() throws Exception {
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = this.KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        KeyPair KeyPair2 = this.KeyPair();
        Block Block2 = new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        assertThat(blockchain.addBlock(Block1), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block2), CoreMatchers.equalTo(true));
        assertThat(blockchain.getMaxHeightBlock().getHash(), CoreMatchers.equalTo(Block1.getHash()));
        assertThat(blockchain.getMaxHeightBlock().getCoinbase(), CoreMatchers.equalTo(new Transaction(25.0D, KeyPair1.getPublic())));
    }

    //
    @Test
    public void test7() throws Exception {
        //verifying that a genesis block cannot be added to an existing BlockChian
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = this.KeyPair();
        Block Block1 = new Block((byte[])null, KeyPair1.getPublic());
        Block1.finalize();

        //verifying that a block with invalid transactions cannot be added to a BlockChain
        KeyPair KeyPair2 = this.KeyPair();
        Block Block2 = new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        KeyPair KeyPair_2 = this.KeyPair();
        Transaction tx = new Transaction(44.0D, KeyPair_2.getPublic());
        blockchain.addTransaction(tx);
        Block2.addTransaction(tx);
        Block2.finalize();

        //verifying that a block with wrong previous hash cannot be added to a BlockChain
        KeyPair keypair = this.KeyPair();
        Block block = new Block((byte[])null, keypair.getPublic());
        KeyPair KeyPair3 = this.KeyPair();
        Block Block3 = new Block(block.getHash(), KeyPair3.getPublic());
        Block2.finalize();

        assertThat(blockchain.addBlock(Block1), CoreMatchers.equalTo(false));
        assertThat(blockchain.addBlock(Block2), CoreMatchers.equalTo(false));
        assertThat(blockchain.addBlock(Block2), CoreMatchers.equalTo(false));
    }



    //Verifying that only the valid transaction whose {@code height > (maxHeight - CUT_OFF_AGE)} can be added to the BlockChain
    @Test
    public void test8() throws Exception {
        BlockChain.CUT_OFF_AGE = 2;
        KeyPair genesisPair = this.KeyPair();
        Block genesisBlock = new Block((byte[])null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = this.KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        KeyPair KeyPair2 = this.KeyPair();
        Block Block2 = new Block(Block1.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        KeyPair KeyPair3 = this.KeyPair();
        Block Block3 = new Block(Block2.getHash(), KeyPair3.getPublic());
        Block3.finalize();
        KeyPair KeyPair4 = this.KeyPair();
        Block Block4 = new Block(genesisBlock.getHash(), KeyPair4.getPublic());
        Block4.finalize();
        assertThat(blockchain.addBlock(Block1), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block2), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block3), CoreMatchers.equalTo(true));
        assertThat(blockchain.addBlock(Block4), CoreMatchers.equalTo(false));
    }


}
