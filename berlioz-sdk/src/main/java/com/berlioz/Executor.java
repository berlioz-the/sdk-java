package com.berlioz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Executor<TResult, TError extends Throwable> {
    private static Logger logger = LogManager.getLogger(Executor.class);

    private PeerAccessor.ISelector _peerSelector;
    private IAction<TResult, TError> _action;
    private int _tryCount = 0;

    public Executor()
    {

    }

    public Executor selector(PeerAccessor.ISelector value)
    {
        this._peerSelector = value;
        return this;
    }

    public Executor action(IAction<TResult, TError> value)
    {
        this._action = value;
        return this;
    }

    public TResult run() throws TError {
        logger.info("Executing ...");
        return this._try();
    }

    public TResult _try() throws TError {
        this._tryCount++;
        logger.debug("Trying x{}...", this._tryCount);
        try {
            return this._action.perform();
        } catch(Throwable error) {
            logger.warn("Trying x{} failed.", this._tryCount);
            if (this._tryCount >= 3) {
                throw (TError)error;
            } else {
                return this._retry();
            }
        }
    }

    public TResult _retry() throws TError{
        logger.debug("Sleeping ...");
        try {
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            logger.error(ex);
        }
        return this._try();
    }

    public interface IAction<TResult, TError extends Throwable> {
        TResult perform() throws TError;
    }
}
