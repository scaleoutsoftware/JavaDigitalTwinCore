package com.scaleoutsoftware.digitaltwin.core;

public interface MessageListFactory {
     <V> MessageList<V> getMessageList();
     <V> Iterable<V> getIncomingMessages();
}
