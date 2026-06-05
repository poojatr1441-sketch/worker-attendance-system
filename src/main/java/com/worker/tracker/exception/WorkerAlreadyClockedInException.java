package com.worker.tracker.exception;

public class WorkerAlreadyClockedInException extends RuntimeException {

    public WorkerAlreadyClockedInException(String message) {
        super(message);
    }
}