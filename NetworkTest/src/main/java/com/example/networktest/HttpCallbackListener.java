package com.example.networktest;

public interface HttpCallbackListener {
    void OnFinsh(String response);
    void OnError(Exception e);
}
