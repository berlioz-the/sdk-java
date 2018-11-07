package com.berlioz;

import com.berlioz.msg.BaseEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Executor<TResult, TError extends Throwable> {
    private static Logger logger = LogManager.getLogger(Executor.class);

    private PolicyResolver policy = Berlioz.policy;
    private PeerAccessor.ISelector _peerSelector;
    private IAction<TResult, TError> _action;
    private int _tryCount = 0;
    private String _remoteName = "";
    private String _actionName = "";

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

    public Executor zipkin(String remoteName, String actionName)
    {
        this._remoteName = remoteName;
        this._actionName = actionName;
        return this;
    }

    public TResult run() throws TError {
        logger.info("Executing {}::{}...", this._remoteName, this._actionName);
        return this._try();
    }

    private TResult _try() throws TError {
        this._tryCount++;
        logger.debug("Trying {}::{} x{}...", this._remoteName, this._actionName, this._tryCount);
        Zipkin.Span span = Zipkin.getInstance().childSpan(this._remoteName, this._actionName);
//        logger.debug("Child Span: {}", span);

        try {
            BaseEndpoint peer = null;
            if (this._peerSelector != null) {
                peer = this._peerSelector.select();
            }
            TResult result = this._action.perform(peer, span);
            span.finish();
            return result;
        } catch(Throwable error) {
            span.finish();
            logger.warn("Trying {}::{} x{} failed.", this._remoteName, this._actionName, this._tryCount);
            if (this._tryCount >= this._resolvePolicyInt("retry-count")) {
                throw (TError)error;
            } else {
                return this._retry();
            }
        }
    }

    private TResult _retry() throws TError {
        this._retryWait();
        return this._try();
    }

    private void _retryWait() {
        int timeout = this._resolvePolicyInt("retry-initial-delay");
        timeout = timeout * (int)Math.pow(this._resolvePolicyInt("retry-delay-multiplier"), this._tryCount - 1);
        timeout = Math.min(timeout, this._resolvePolicyInt("retry-max-delay"));

        logger.debug("Sleeping {}ms...", timeout);
        if (timeout > 0) {
            Zipkin.Span span = Zipkin.getInstance().childSpan("sleep", "sleep");
            try {
                Thread.sleep(timeout);
            } catch(InterruptedException ex) {
                logger.error(ex);
            }
            span.finish();
        }
    }

    private boolean _resolvePolicyBool(String name)
    {
        return policy.resolveBool(name, ListHelper.Path());
    }

    private int _resolvePolicyInt(String name)
    {
        return policy.resolveInt(name, ListHelper.Path());
    }

    private String _resolvePolicyString(String name)
    {
        return policy.resolveString(name, ListHelper.Path());
    }

    public interface IAction<TResult, TError extends Throwable> {
        TResult perform(BaseEndpoint peer, Zipkin.Span span) throws TError;
    }
}
