package ru.cinimex.arch.accountservice.appenders;

public class AppenderException extends RuntimeException {
    public AppenderException(String appender_exception) {
        super((appender_exception));
    }
}
