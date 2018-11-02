package com.berlioz;

import brave.Span;
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
        logger.info("Executing ...");
        return this._try();
    }

    private TResult _try() throws TError {
        this._tryCount++;
        logger.debug("Trying x{}...", this._tryCount);
        Span span = Zipkin.getInstance().childSpan(this._remoteName, this._actionName).annotate("cs");
        try {
            BaseEndpoint peer = this._peerSelector.select();
            TResult result = this._action.perform(peer, span);
            span.annotate("cr");
            return result;
        } catch(Throwable error) {
            span.annotate("cr");
            logger.warn("Trying x{} failed.", this._tryCount);
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
            Span span = Zipkin.getInstance().childSpan("sleep", "sleep").annotate("cs");
            try {
                Thread.sleep(timeout);
            } catch(InterruptedException ex) {
                logger.error(ex);
            }
            span.annotate("cr");
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
        TResult perform(BaseEndpoint peer, brave.Span span) throws TError;
    }
}
