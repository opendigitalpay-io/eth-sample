package org.web3j.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Optional;

public class TestEth {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new TestEth().runWeb3j();
    }

    private void runWeb3j() throws Exception {

        //connect to eth main-net
        Web3j web3 = Web3j.build(new HttpService("https://mainnet.infura.io/v3/0a9027bb7971401fa3bff3ca2510dada"));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info("Connected to Ethereum client version: " + clientVersion);

        // go to https://etherscan.io/txsPending to get hash for a pending txn
        Optional<TransactionReceipt> transactionReceipt= web3.ethGetTransactionReceipt("0x3e8a29631df286b6120244d707e0be699e56f8a7fdc70296b96bab97f4dda364").send().getTransactionReceipt();
        if(transactionReceipt.isPresent()) {

            //0X1 --success
            //0x0 --pending
            log.info(transactionReceipt.get().getStatus());
            log.info( transactionReceipt.get().getFrom());
        }
    }
}
