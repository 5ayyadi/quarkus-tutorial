package com.core.wallet;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDPath;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import com.core.network.Network;

public class HDWallet {

    // Creation time of the key in seconds since the epoch, or zero if the key was
    // deserialized from a version that did
    // not have this field.
    private static final long CreationTimeStamp = 1409478661L;

    public static HDPath HDPathGenerator(int chainCoinType, int accountId, int accountIndex) {
        // m / purpose' / coin_type' / account' / change / address_index
        return HDPath.parsePath(String.format("m/44H/%dH/%dH/0/%d", chainCoinType, accountId, accountIndex));
    }

    private static String mnemonic = "science limit budget find another chair orient duty cost expire soap great";
    // TODO - read from .env file
    // private static String mnemonic = System.getenv("MNEMONIC_PHRASE");

    public static WalletKeyPair Create(Network network, int accountId) throws UnreadableWalletException {
        return Create("0x", network, mnemonic, accountId, 0);
    }

    public static WalletKeyPair Create(String walletPrefix, Network network, String mnemonic, int accountId,
            int accountIndex) throws UnreadableWalletException {
        DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", CreationTimeStamp);
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey addrKey = chain.getKeyByPath(
                HDWallet.HDPathGenerator(
                        network.value.HDPathCoinType,
                        accountId,
                        accountIndex),
                true);
        String privKey = addrKey.getPrivKey().toString(16);
        // String privKey =
        // Sign.publicKeyFromPrivate(addrKey.getPrivKey()).toString(16);
        // addrKey.getPublicKeyAsHex();
        // addrKey.decompress().getPublicKeyAsHex();
        // addrKey.decompress().getPublicKeyAsHex().substring(2);
        String pubKey = Keys.getAddress(addrKey.decompress().getPublicKeyAsHex().substring(2));
        return new WalletKeyPair(privKey, pubKey);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
