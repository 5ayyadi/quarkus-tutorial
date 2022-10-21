package com.core.msg;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.google.inject.Inject;

@ApplicationScoped
public class StatusHandler {

    @Inject
    @Channel("blockStatus")
    Emitter<String> blockStatusEmitter;

}
