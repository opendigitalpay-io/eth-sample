package org.web3j.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * A simple web3j application that demonstrates a number of core features of web3j:
 *
 * <ol>
 *     <li>Connecting to a node on the Ethereum network</li>
 *     <li>Loading an Ethereum wallet file</li>
 *     <li>Sending Ether from one address to another</li>
 *     <li>Deploying a smart contract to the network</li>
 *     <li>Reading a value from the deployed smart contract</li>
 *     <li>Updating a value in the deployed smart contract</li>
 *     <li>Viewing an event logged by the smart contract</li>
 * </ol>
 *
 * <p>To run this demo, you will need to provide:
 *
 * <ol>
 *     <li>Ethereum client (or node) endpoint. The simplest thing to do is
 *     <a href="https://infura.io/register.html">request a free access token from Infura</a></li>
 *     <li>A wallet file. This can be generated using the web3j
 *     <a href="https://docs.web3j.io/command_line.html">command line tools</a></li>
 *     <li>Some Ether. This can be requested from the
 *     <a href="https://www.rinkeby.io/#faucet">Rinkeby Faucet</a></li>
 * </ol>
 *
 * <p>For further background information, refer to the project README.
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new Application().runWeb3j();
    }

    private void runWeb3j() throws Exception {

        /*String webServer = "http://192.168.1.157:8545";
        String sourceCredentials = "329bbdc57b698dbc96fe8f60a12007841dd1a90524f3c3b16d01b09f25a4f13f";
        String targetAddress = "0xBC35b0939e521D26900f79A5aa8b624fbfD492c7";*/

        String webServer = "https://ropsten.infura.io/v3/a2e27f7731e845198230881c0f010ef3";
        String sourceCredentials = "a982bcad304626c16b1fdadaa9c8b09d74a0cf48c96015f839835674bb51f416";
        String targetAddress = "0xAED01C776d98303eE080D25A21f0a42D94a86D9c";


        //connect to ganache
        Web3j web3 = Web3j.build(new HttpService(webServer));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info("Connected to Ethereum client version: " + clientVersion);


        // ************ — — — — — — create account by seed phrase ******************
//        System.out.println("Creating New Account");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Enter New Password");
//        String walletPassword = br.readLine();
//        /* Define Wallet File Location */
//        File walletDirectory = new File("/Users/siyuanhe/crypto/");
//        Bip39Wallet walletName = WalletUtils.generateBip39Wallet(walletPassword, walletDirectory);
//        System.out.println("wallet location: " + walletDirectory + "/" + walletName);
//        Credentials credentials = WalletUtils.loadBip39Credentials(walletPassword, walletName.getMnemonic());
//        String accountAddress = credentials.getAddress();
//        System.out.println("Account address: " + credentials.getAddress());
//        ECKeyPair privateKey = credentials.getEcKeyPair();
//        String seedPhrase = walletName.getMnemonic();
//        System.out.println("Account Details:");
//        System.out.println("Your New Account : " + credentials.getAddress());
//        System.out.println("Mneminic Code: " + walletName.getMnemonic());
//        System.out.println("Private Key: " + privateKey.getPrivateKey().toString(16));
//        System.out.println("Public Key:  "+ privateKey.getPublicKey().toString(16));

//        String seedCode = "lrange genre joy gold diamond monitor film young cheese author march chapter";

        Credentials credentials = Credentials.create(sourceCredentials);
        System.out.println(credentials.getAddress());
        
        // Send Funds Sync
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3, credentials,
                targetAddress,  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI) //Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, " + transferReceipt.getTransactionHash());


    }

    private void run() throws Exception {

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        Web3j web3j = Web3j.build(new HttpService(
                "https://rinkeby.infura.io/<your token>"));  // FIXME: Enter your Infura token here;
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "<password>",
                        "/path/to/<walletfile>");

        log.info("Credentials loaded");

        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash());

        // Now lets deploy a smart contract
        log.info("Deploying smart contract");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();

    }
}
