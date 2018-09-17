package com.scaleoutsoftware.digitaltwin;

public interface MessageListFactory {
     <V> MessageList<V> getMessageList();
     <V> Iterable<V> getIncomingMessages();
}
