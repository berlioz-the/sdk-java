package com.berlioz.agent;

import com.berlioz.Processor;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.DeploymentException;
import javax.websocket.*;

// https://www.baeldung.com/websockets-api-java-spring-client

public class Client {

    private static Logger logger = LogManager.getLogger(Client.class);

    class RetryTimerTask extends TimerTask {
        Client _owner;

        RetryTimerTask(Client owner) {
            this._owner = owner;
        }

        @Override
        public void run() {
            this._owner._connect();
        }
    }

    class AgentEndpoint extends Endpoint {

        Client _owner;

        AgentEndpoint(Client owner) {
            this._owner = owner;
        }

        @Override
        public void onOpen(Session session, EndpointConfig config) {
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    logger.trace("Received message: {}", message);
                    try {
                        _owner._processor.accept(message);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onError(Session session, Throwable exception) {
            logger.error("On Error:: {}", exception);
            this._owner._schedule();
        }
    }

    private boolean _isScheduled = false;

    private final ClientEndpointConfig _cec = ClientEndpointConfig.Builder.create().build();
    private ClientManager _wsClientManager = ClientManager.createClient();
    private AgentEndpoint _endpoint = new AgentEndpoint(this);
    private Session _currentSession;
    private Processor _processor;

    public Client(Processor processor)
    {
        _processor = processor;
    }

    public void run() {
        this._connect();
    }

    void _schedule()
    {
        if (this._isScheduled) {
            return;
        }
        this._isScheduled = true;

        RetryTimerTask retryTask = new RetryTimerTask(this);
        Timer timer = new Timer("BerliozWSConnectTimer");
        timer.schedule(retryTask, 2000L);
    }

    void _connect()
    {
        this._isScheduled = false;
        try
        {
            this._tryConnect();
        }
        catch(DeploymentException ex)
        {
            logger.warn("Disconnected...");
            this._schedule();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            this._schedule();
        }
    }

    void _tryConnect() throws URISyntaxException, DeploymentException, IOException
    {
        logger.info("Connecting to: {}.", System.getenv("BERLIOZ_AGENT_PATH"));

        if (this._currentSession != null) {
            this._currentSession.close();
        }

        this._currentSession = this._wsClientManager.connectToServer(
                this._endpoint,
                this._cec,
                new URI(System.getenv("BERLIOZ_AGENT_PATH")));
    }
}
