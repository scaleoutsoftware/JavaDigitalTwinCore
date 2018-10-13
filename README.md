# ScaleOut DigitalTwin Builder&trade; for ScaleOut StreamServer&trade;

The ScaleOut Digital Twin Builder™ lets developers build digital twin models of data sources for stateful stream-processing using ScaleOut StreamServer™. These models enable applications to associate parameters and dynamic state for each data source that generates messages; this information provides additional context for introspecting on incoming messages. A digital twin model consists of a state object and a message processor, both defined using base classes provided by the API library. Additional APIs enable deployment of digital twin models to ScaleOut StreamServer for message processing.

Instances of state objects are serialized using JSON and stored within ScaleOutStateServer’s in-memory data grid for fast access. Incoming messages from data sources are delivered to instances of digital twin models, each identified by a digital twin model name (string) and an instance identifier (string). During message processing, outbound messages can be sent back to data sources and/or to other instances of digital twins.

For the concepts behind ScaleOut Digital Twin Builder, please read the ScaleOut Digital Twin Builder User Guide, which you can find in ScaleOut StreamServer's installation folder.

## Quickstart 

The following example illustrates the steps you should follow to define a digital twin model using the DigitalTwin and MessageProcessor classes. These classes need to be JSON serializable since the library uses JSON serialization for all user defined types. Once these steps have been completed, you can use the deployment library (located in ScaleOut StreamServer’s installation directory) to deploy a digital twin within ScaleOut StreamServer and start handling incoming messages from its data sources.

Additionally, you can connect ScaleOutStreamServer to Kafka and Azure IoT data sources to receive messages from data sources and send outbound messages back to these data sources. The data source library (also located in the installation directory) explains how to set up a connection to Kafka-based data sources, and our .NET library lets you set up connections to Azure IoT Hub.
 

1) Define a class which represents a state object for your digital twin model. This class should reflect the state and behavior of the data source you need to model and inherit from the DigitalTwin abstract class. Note that each instance of a state object is created when the first incoming message arrives from its corresponding data source:

```
public class MyDigitalTwin extends DigitalTwinBase {
    private final int MAX_INTEGER_STATE_THRESHOLD = 100;
    private String myStringPropertyState;
    private int myIntegerPropertyState;
	private List<MyMessage> myMessageList; 

    public MyDigitalTwin() {}
    
    public String getStringPropertyState() { return myStringPropertyState; }
    public int getIntegerPropertyState() { return myIntegerPropertyState; }
    
    public int getMaxIntegerStateThreshold() { return MAX_INTEGER_STATE_THRESHOLD; }
	public void addMessage(MyMessage message); 
} 
```

2) Define a message that your data sources will send to instances of this digital twin model (one instance for each data source) deployed within ScaleOut StreamServer. The message can be a simple Java object:

```
public class MyMessage {
    private String myMessageType;
    private String incomingStringStateChange;
    private int incomingIntegerStateChange;
	private long timestamp;
    
    public MyMessage() {}
    
    public String getIncomingStringStateChange() { return incomingStringStateChange; }
    public int getIncomingIntegerStateChange() { return incomingIntegerStateChange; }
	public long getTimestamp() { return timestamp; }
}
```

3) Create a MessageProcessor to process incoming messages from each data source to the corresponding instance of a digital twin model. The message processor is defined as a subclass of the MessageProcessor abstract class. The processMessages method will be called by ScaleOut StreamServer to process incoming messages for each instance of a digital twin object.

```
Public class HeartRateMessageProcessor extends MessageProcessor<MyDigitalTwin, MyMessage> {

    @Override
    public ProcessingResult processMessages(ProcessingContext processingContext, 
                                            MyDigitalTwin digitalTwin, 
                                            Iterable<MyMessage> incomingMessages) throws Exception {
        for(MyMessage message : incomingMessages) {
            if(message.getIncomingIntegerStateChange() > digitalTwin.getMaxIntegerStateThreshold()) {
                // if an incoming message exceeds a threshold, send a message back to the device
                processingContext.sendToDataSource(...); // send a JSON serializable message
            } else {
                // update our state 
                digitalTwin.addMessage(message);
            }
        }
        return ProcessingResult.UpdateDigitalTwin;
    }
}
```



