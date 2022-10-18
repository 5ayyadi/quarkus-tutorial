package com.core.msg;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.core.models.massages.Quote;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * A bean consuming data from the "request" RabbitMQ queue and giving out a
 * random quote.
 * The result is pushed to the "quotes" RabbitMQ exchange.
 */
@ApplicationScoped
public class QuoteProcessor {

    private Random random = new Random();

    @Incoming("requests")
    @Outgoing("quotes")
    @Blocking
    public Quote process(String quoteRequest) throws InterruptedException {
        // simulate some hard working task
        // Thread.sleep(200);
        System.out.println("RECEIVED ...");
        System.out.println(quoteRequest);
        return new Quote(quoteRequest, random.nextInt(100));
    }

    @Incoming("blockStatusREAD")
    @Blocking
    public void processStatus(String quoteRequest) throws InterruptedException {
        // simulate some hard working task
        // Thread.sleep(200);
        // Log.infof("RECEIVED ...%s", quoteRequest);

        // return new Quote(quoteRequest, random.nextInt(100));
    }
}
