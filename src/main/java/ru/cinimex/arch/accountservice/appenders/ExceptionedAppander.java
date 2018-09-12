package ru.cinimex.arch.accountservice.appenders;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;

public class ExceptionedAppander extends AppenderBase<ILoggingEvent> {

    private boolean guard = false;
    private int statusRepeatCount = 0;
    private int exceptionCount = 0;

    static final int ALLOWED_REPEATS = 5;

    @Override
    public synchronized void doAppend(ILoggingEvent eventObject) {
        // WARNING: The guard check MUST be the first statement in the
        // doAppend() method.

        // prevent re-entry.
        if (guard) {
            return;
        }

        try {
            guard = true;

            if (!this.started) {
                if (statusRepeatCount++ < ALLOWED_REPEATS) {
                    addStatus(new WarnStatus("Attempted to append to non started appender [" + name + "].", this));
                }
                return;
            }

            if (getFilterChainDecision(eventObject) == FilterReply.DENY) {
                return;
            }

            // ok, we now invoke derived class' implementation of append
            this.append(eventObject);

        } catch (Exception e) {
            if (exceptionCount++ < ALLOWED_REPEATS) {
                addError("Appender [" + name + "] failed to append.", e);
                throw e;
            }
        } finally {
            guard = false;
        }
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        throw new AppenderException("appender exception");
    }
}
