package com.scaleoutsoftware.digitaltwin.model;

public interface MessageSender {
    SendingResult sendMessage(String targetNamespace, String id, Message message);
}
