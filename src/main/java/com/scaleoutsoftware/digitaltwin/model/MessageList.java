package com.scaleoutsoftware.digitaltwin.model;

import java.util.List;

public interface MessageList {

    void AddMessage(Message message);
    List<Message> getMessageList();
    void setListSize(int listSize);
}
