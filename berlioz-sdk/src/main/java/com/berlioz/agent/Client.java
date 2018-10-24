package com.berlioz.agent;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;

// https://www.baeldung.com/websockets-api-java-spring-client

public class Client {

    private Logger logger = LogManager.getLogger(Client.class);

    public Client()
    {

    }

    public void run() {
        try
        {
            this._tryConnect();
            new Scanner(System.in).nextLine(); // Don't close immediately.
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    void _tryConnect() throws URISyntaxException, DeploymentException, IOException
    {
        logger.info("Initiating connect to: {}.", System.getenv("BERLIOZ_AGENT_PATH"));

        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
        ClientManager client = ClientManager.createClient();
        client.connectToServer(new Endpoint() {

            @Override
            public void onOpen(Session session, EndpointConfig config) {
                session.addMessageHandler(new MessageHandler.Whole<String>() {

                    @Override
                    public void onMessage(String message) {
                        logger.trace("Received message: {}", message);
                    }
                });
            }
        }, cec, new URI(System.getenv("BERLIOZ_AGENT_PATH")));
    }
}
