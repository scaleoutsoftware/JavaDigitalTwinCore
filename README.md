# ScaleOut DigitalTwin Builder&trade; for ScaleOut StreamServer&trade;

The Scaleout DigitalTwin Builder allows developers to simulate, communicate, and introspect on Edge devices with straight forward APIs built in Java.   

## Quickstart 

The following steps will help you setup the DigitalTwin classes. The following classes should be JSON serializable. Once the quick start has been completed, you can use the deployment library (located in the installation directory of ScaleOut StateServer) to deploy the DigitalTwin and start monitoring the state of your Edge devices. 

Additionally, you can hook in Kafka and Azure IoT datasources for sending and recieving messages. The datasource library (also located in the installation directory) details how to setup a Kafka data source and our .NET library documentation explains how to configure the execution environment with Azure IoT.  

1) Define a class representation of your Edge Device - a DigitalTwin:

```
public class MyDigitalTwin extends DigitalTwin {
	private final int MAX_INTEGER_STATE_THRESHOLD = 100;
    private String myStringPropertyState;
    private int myIntegerPropertyState;

    public MyDigitalTwin() {}
	
	public String getStringPropertyState() { return myStringPropertyState; }
	public int getIntegerPropertyState() { return myIntegerPropertyState; }
	
	public int getMaxIntegerStateThreshold() { return MAX_INTEGER_STATE_THRESHOLD; }
} 
```

2) Define a message that your Edge devices will send to the DigitalTwin:

```
public class MyMessage {
	private String myMessageType;
	private String incomingStringStateChange;
	private int incomingIntegerStateChange;
	
	public MyMessage() {}
	
	public String getIncomingStringStateChange() { return incomingStringStateChange; }
	public int getIncomingIntegerStateChange() { return incomingIntegerStateChange; }
}
```

3) Create a MessageProcessor which processes incoming messages from your Edge devices:

```
Public class HeartRateMessageProcessor extends MessageProcessor<MyDigitalTwin, MyMessage> {

    @Override
    public ProcessingResult processMessages(ProcessingContext processingContext, 
	                                        MyDigitalTwin digitalTwin, 
	                                        Iterable<MyMessage> incomingMessages) throws Exception {
		for(MyMessage message : incomingMessages) {
			if(message.getIncomingIntegerStateChange() > digitalTwin.getMaxIntegerStateThreshold()) {
				// if an incoming message exceeds a threshold, send a message back to the device
				processingContext.send(...); // send a JSON serializable message
			} else {
				// update our state 
				...
			}
		}
		return ProcessingResult.UpdateDigitalTwin;
	}
}
```



