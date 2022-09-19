package com.core;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDPath;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

;

public class WalletUtils {

    public static KeyPair<String,String> Create(int walletId) throws UnreadableWalletException {
            String mnemonic = "science limit budget find another chair orient duty cost expire soap great";
            DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", 1409478661L);
            DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
            // m / purpose' / coin_type' / account' / change / address_index
//            TODO - Make this bit dynamic :)))
            DeterministicKey addrKey = chain.getKeyByPath(
                    HDPath.parsePath(String.format("M/44H/60H/0H/0/%d", walletId)),
                    true);
            String privKey = Sign.publicKeyFromPrivate(addrKey.getPrivKey()).toString(16);
            addrKey.getPublicKeyAsHex();
            addrKey.decompress().getPublicKeyAsHex();
            addrKey.decompress().getPublicKeyAsHex().substring(2);
            String walletAddress = Keys.getAddress(addrKey.decompress().getPublicKeyAsHex().substring(2));
            return new KeyPair(privKey, String.format("0x%s",walletAddress));
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
