package com.scaleoutsoftware.digitaltwin.model;

public class Message {
    private byte[] _payload;

    public Message (byte[] payload) {
        _payload = payload;
    }

    /**
     * use this to get at your message payload
     * @return
     */
    public byte[] getPayload() {
        return _payload;
    }
}
