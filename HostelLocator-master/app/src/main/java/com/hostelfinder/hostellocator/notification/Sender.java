package com.hostelfinder.hostellocator.notification;

public class Sender {

    private Data data;
    public String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
