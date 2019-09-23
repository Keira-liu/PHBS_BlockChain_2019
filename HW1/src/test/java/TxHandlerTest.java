

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.security.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

/** 
* TxHandler Tester. 
* 
* @author <Áõ°²ÒÙ>
* @since <pre>9ÔÂ 22, 2019</pre> 
* @version 1.0 
*/ 
public class TxHandlerTest {

    TxHandler Tx_Handler;
    KeyPair keyA;
    KeyPair keyB;
    KeyPair keyC;
    KeyPair keyD;
    byte[] tx_hashA;
    byte[] tx_hashB;
    byte[] tx_hashC;
    byte[] tx_hashD;

    @Before
    public void before() throws Exception {
        keyA = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        keyB = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        keyC = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        keyD = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction.Output outA = new Transaction().new Output(10, keyA.getPublic());
        Transaction.Output outB = new Transaction().new Output(10, keyB.getPublic());
        Transaction.Output outC = new Transaction().new Output(10, keyC.getPublic());
        Transaction.Output outD = new Transaction().new Output(10, keyD.getPublic());
        tx_hashA = new String("tx_hashA").getBytes();
        tx_hashB = new String("tx_hashB").getBytes();
        tx_hashC = new String("tx_hashC").getBytes();
        tx_hashD = new String("tx_hashD").getBytes();
        UTXOPool pool = new UTXOPool();
        pool.addUTXO(new UTXO(tx_hashA, 0), outA);
        pool.addUTXO(new UTXO(tx_hashB, 0), outB);
        pool.addUTXO(new UTXO(tx_hashC, 0), outC);
        pool.addUTXO(new UTXO(tx_hashD, 0), outD);
        this.Tx_Handler = new TxHandler(pool);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * verify that a normal transaction is allowed
     */
    @Test
    public void testIsValidTx1() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_AB_to_CD = new Transaction();
        tx_AB_to_CD.addInput(this.tx_hashA, 0);
        tx_AB_to_CD.addInput(this.tx_hashB, 0);
        tx_AB_to_CD.addOutput(10.0D, this.keyC.getPublic());
        tx_AB_to_CD.addOutput(10.0D, this.keyD.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_AB_to_CD.getRawDataToSign(0));
        byte[] signa = sig.sign();
        tx_AB_to_CD.addSignature(signa, 0);
        sig.initSign(this.keyB.getPrivate());
        sig.update(tx_AB_to_CD.getRawDataToSign(1));
        signa = sig.sign();
        tx_AB_to_CD.addSignature(signa, 1);
        tx_AB_to_CD.finalize();
        Transaction[] tx = {tx_AB_to_CD};
        this.Tx_Handler.handleTxs(tx);
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 0)), Matchers.is(false));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashB, 0)), Matchers.is(false));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_AB_to_CD.getHash(), 0)), Matchers.is(true));

    }

    /**
     * verify a transaction with a input that is not in the current URXOpool will be rejected
     */
    @Test
    public void testIsValidTx2() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_A_to_B = new Transaction();
        tx_A_to_B.addInput(this.tx_hashA, 1);
        tx_A_to_B.addOutput(10.0D, this.keyB.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_A_to_B.getRawDataToSign(0));
        byte[] signa = sig.sign();
        tx_A_to_B.addSignature(signa, 0);
        tx_A_to_B.finalize();
        Transaction[] tx = {tx_A_to_B};
        this.Tx_Handler.handleTxs(tx);
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 1)), Matchers.is(false));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_B.getHash(), 0)), Matchers.is(false));
    }

    /**
     * verify the a transaction with a invalid signature will be rejected
     */
    @Test
    public void testIsValidTx3() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_A_to_B = new Transaction();
        tx_A_to_B.addInput(this.tx_hashA, 0);
        tx_A_to_B.addOutput(10.0D, this.keyB.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyC.getPrivate());
        sig.update(tx_A_to_B.getRawDataToSign(0));
        byte[] signa = sig.sign();
        tx_A_to_B.addSignature(signa, 0);
        tx_A_to_B.finalize();
        Transaction[] tx = {tx_A_to_B};
        this.Tx_Handler.handleTxs(tx);
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 0)), Matchers.is(true));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_B.getHash(), 0)), Matchers.is(false));

    }

    /**
     * verify that no UTXO can be claimed multiple times by tx
     */
    @Test
    public void testIsValidTx4() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_A_to_B = new Transaction();
        tx_A_to_B.addInput(this.tx_hashA, 0);
        tx_A_to_B.addOutput(10.0D, this.keyB.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_A_to_B.getRawDataToSign(0));
        byte[] sign1 = sig.sign();
        tx_A_to_B.addSignature(sign1, 0);
        tx_A_to_B.finalize();

        Transaction tx_A_to_C = new Transaction();
        tx_A_to_C.addInput(this.tx_hashA, 0);
        tx_A_to_C.addOutput(10.0D, this.keyC.getPublic());
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_A_to_C.getRawDataToSign(0));
        byte[] sign2 = sig.sign();
        tx_A_to_C.addSignature(sign2, 0);
        tx_A_to_C.finalize();

        Transaction[] tx = {tx_A_to_B,tx_A_to_C};
        Transaction[] first = {tx_A_to_B};
        Assert.assertThat(this.Tx_Handler.handleTxs(tx),Matchers.is(first));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 0)), Matchers.is(false));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_B.getHash(), 0)), Matchers.is(true));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_C.getHash(), 0)), Matchers.is(false));

    }

    /**
     * verify that a transaction with a negative output will be rejected
     */
    @Test
    public void testIsValidTx5() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_A_to_B = new Transaction();
        tx_A_to_B.addInput(this.tx_hashA, 0);
        tx_A_to_B.addOutput(-1, this.keyB.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_A_to_B.getRawDataToSign(0));
        byte[] signa = sig.sign();
        tx_A_to_B.addSignature(signa, 0);
        tx_A_to_B.finalize();
        Transaction[] tx = {tx_A_to_B};
        this.Tx_Handler.handleTxs(tx);
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 0)), Matchers.is(true));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_B.getHash(), 0)), Matchers.is(false));
    }

    /**
     * verify that a transaction whose sum of input values is smaller than the sum of
     * its output values will be rejected
     */
    @Test
    public void testIsValidTx6() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Transaction tx_A_to_B = new Transaction();
        tx_A_to_B.addInput(this.tx_hashA, 0);
        tx_A_to_B.addOutput(11, this.keyB.getPublic());
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(this.keyA.getPrivate());
        sig.update(tx_A_to_B.getRawDataToSign(0));
        byte[] signa = sig.sign();
        tx_A_to_B.addSignature(signa, 0);
        tx_A_to_B.finalize();
        Transaction[] tx = {tx_A_to_B};
        this.Tx_Handler.handleTxs(tx);
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(this.tx_hashA, 0)), Matchers.is(true));
        Assert.assertThat(this.Tx_Handler.assetverify(new UTXO(tx_A_to_B.getHash(), 0)), Matchers.is(false));
    }


}


