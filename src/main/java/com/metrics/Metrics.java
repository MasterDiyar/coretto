package com.metrics;

public interface Metrics {
    void start();
    void stop();
    void inc(String key);
    long getTime();
    int get(String key);
}