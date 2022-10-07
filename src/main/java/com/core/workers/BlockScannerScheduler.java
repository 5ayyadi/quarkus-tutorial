package com.core.workers;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.web3j.protocol.Web3j;

import com.core.models.TrxReceipt;
import com.core.models.block.ScannedBlocks;
import com.core.network.BlockScanner;
import com.core.network.Network;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class BlockScannerScheduler {
    // @Inject
    // ScannedBlocks scannedBlocks;

    private AtomicInteger counter = new AtomicInteger();
    private AtomicBoolean isScannerRunning = new AtomicBoolean(false);

    public int get() {
        return counter.get();
    }

    // @Scheduled(every = "10s")
    // void increment() {
    // counter.incrementAndGet();
    // System.out.println(get());
    // }

    // @Scheduled(cron = "0 15 10 * * ?")
    // void cronJob(ScheduledExecution execution) {
    // counter.incrementAndGet();
    // System.out.println(execution.getScheduledFireTime());
    // }

    // @Scheduled(cron = "{blockScanner.BSC.ALL}")

    @Scheduled(every = "5m")
    @Transactional
    void cronJobWithExpressionInConfig() {
        if (isScannerRunning.get() == false) {
            isScannerRunning.set(true);
            try {
                Network network = Network.EthereumLocal;
                System.out.println("HEAVY TASK STARTED!");
                Web3j w3 = network.value.w3;
                Long currentBlockNumber = w3.ethBlockNumber().send().getBlockNumber().longValue();
                System.out.println(String.format("Current Block Number : %s", currentBlockNumber));
                // ScannedBlocks.list("*", "blockNumber", null)
                Long lastBlocknumber = ScannedBlocks.lastScannedBlock();
                System.out.println(String.format("Last Scanned Block Number : %s", lastBlocknumber));
                if (lastBlocknumber < currentBlockNumber) {
                    for (var entry : BlockScanner.fetchBlockNumbers(network, lastBlocknumber, currentBlockNumber)
                            .entrySet()) {
                        ScannedBlocks scannedBlock = new ScannedBlocks(entry.getKey(), network);
                        scannedBlock.persist();
                        for (TrxReceipt trxReceipt : entry.getValue()) {
                            trxReceipt.persist();
                        }
                        System.out.println(String.format("BlockNumber#%d: Safely Quried %d transactions!",
                                entry.getKey(), entry.getValue().size()));
                    }
                }
                counter.incrementAndGet();
                System.out.println("HEAVY TASK FINISHED!");
                System.out.println(counter.get());

            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            } finally {
                isScannerRunning.set(false);
            }
        }
    }
}
