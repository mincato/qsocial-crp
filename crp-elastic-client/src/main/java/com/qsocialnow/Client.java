package com.qsocialnow;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.elasticsearch.configuration.ClientProcessor;

public class Client {

    private final static String[] COMMANDS = { "add" };

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        if (args.length > 0) {
            if (validateArgs(args[0])) {
                ClientProcessor.executeCommand(args);
            } else {
                log.error("Invalid command");
            }
        } else {
            log.error("Invalid command");
        }
    }

    private static boolean validateArgs(String args) {
        return Arrays.asList(COMMANDS).contains(args.toLowerCase());
    }

}