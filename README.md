# ScaleOut DigitalTwin Builder&trade; for ScaleOut StreamServer&trade;

The ScaleOut Digital Twin Builder™ lets developers build real-time digital twin models of data sources for stateful stream-processing using ScaleOut StreamServer™. These models enable applications to associate parameters and dynamic state for each data source that generates messages; this information provides additional context for introspecting on incoming messages. A real-time digital twin model consists of a state object and a message processor, both defined using base classes provided by the API library. Additional APIs enable deployment of real-time digital twin models to ScaleOut StreamServer for real-time message processing.

Instances of digital twins are serialized using JSON and stored within ScaleOut StateServer’s in-memory data grid for fast access. Incoming messages from data sources are delivered to instances of the real-time digital twin, each identified by a model name (string) and an instance identifier (string). During message processing, outbound messages can be sent back to data sources and/or to other instances of real-time digital twins.

For the concepts behind the ScaleOut Digital Twin Builder, please read the ScaleOut Digital Twin Builder User Guide, which you can find in ScaleOut StreamServer's installation folder.

## Example 

The following example illustrates the steps you should follow to define a real-time digital twin model using the DigitalTwin, Message, and MessageProcessor classes. Both the DigitalTwin and Message classes need to be JSON serializable since the library uses JSON serialization for all user defined types. Once these steps have been completed, you can use the deployment library (located in ScaleOut StreamServer’s installation directory as digitaltwin-deployment-VERSION.jar) to deploy a real-time digital twin within ScaleOut StreamServer and start handling incoming messages from data sources.

Additionally, you can connect ScaleOut StreamServer to AWS IoT core, Kafka clusters, Azure Kafka-enabled Event hubs, and Azure IoT data sources to receive messages and send outbound messages back to these data sources. The data source library (also located in the installation directory as digitaltwin-datasource-0.5-BETA.jar) explains how to set up a connection to AWS IoT core and Kafka-based data sources, and our .NET library lets you set up connections to Azure IoT Hub.
 

1) Define a class which represents a state object for your real-time digital twin model. This class should reflect the state and behavior of the data source you need to model and inherit from the DigitalTwin abstract class. Note that each instance of a state object is created when the first incoming message arrives from its corresponding data source:

```
import com.scaleoutsoftware.digitaltwin.core.DigitalTwinBase;
import java.util.LinkedList;
import java.util.List;
public class MyDigitalTwin extends DigitalTwinBase {
    private final int MAX_INTEGER_STATE_THRESHOLD = 100;
    private String myStringPropertyState = "";
    private int myIntegerPropertyState = 0;
    private List<MyMessage> myMessageList = new LinkedList<MyMessage>(); 

    public MyDigitalTwin() {}
    
    public String getStringPropertyState() { return myStringPropertyState; }
    public int getIntegerPropertyState() { return myIntegerPropertyState; }
    
    public void setStringPropertyState(String change) { myStringPropertyState = change; }
    public void setIntegerPropertyState(int change) { myIntegerPropertyState = change; }
    
    public int getMaxIntegerStateThreshold() { return MAX_INTEGER_STATE_THRESHOLD; }
    public void addMessage(MyMessage message) { myMessageList.add(message); } 
} 
```

2) Define a message that your data sources will send to instances of this digital twin model (one instance for each data source) deployed within ScaleOut StreamServer. The message can be a simple Java object:

```
public class MyMessage {
    private String myMessageType;
    private String incomingStringStateChange;
    private int incomingIntegerStateChange;
    private long timestamp;
    
    public MyMessage(String type, String stringChange, int integerChange, long ts) {
        myMessageType = type;
        incomingStringStateChange = stringChange;
        incomingIntegerStateChange = integerChange;
        timestamp = ts;
    }
    
    public String getIncomingStringStateChange() { return incomingStringStateChange; }
    public int getIncomingIntegerStateChange() { return incomingIntegerStateChange; }
    public long getTimestamp() { return timestamp; }
}
```

3) Create a MessageProcessor to process incoming messages from each data source to the corresponding instance of a real-time digital twin model. The message processor is defined as a subclass of the MessageProcessor abstract class. The processMessages method will be called by ScaleOut StreamServer to process incoming messages for each instance of a digital twin object.

```
import com.scaleoutsoftware.digitaltwin.core.MessageProcessor;
import com.scaleoutsoftware.digitaltwin.core.ProcessingContext;
import com.scaleoutsoftware.digitaltwin.core.ProcessingResult;
public class MyMessageProcessor extends MessageProcessor<MyDigitalTwin, MyMessage> {

    @Override
    public ProcessingResult processMessages(ProcessingContext processingContext, 
                                            MyDigitalTwin myDigitalTwin, 
                                            Iterable<MyMessage> incomingMessages) throws Exception {
        int messageCount = 0;
        int integerStateTotal = 0;
        for(MyMessage message : incomingMessages) {
            if(message.getIncomingIntegerStateChange() > myDigitalTwin.getMaxIntegerStateThreshold()) {
                // if an incoming message exceeds a threshold, save the message in the DigitalTwin
                myDigitalTwin.addMessage(message);
                // optionally send a JSON encoded message back to the datasource
                // processingContext.sendToDataSource(...); // send a JSON serializable message
				// optionally log a message
				// processingContext.logMessage(Level.INFO, "the state change exceeded the max integer state threshold");
                
            }
            // calculate an average
            integerStateTotal += message.getIncomingIntegerStateChange();
            messageCount++;
        }
        int current = myDigitalTwin.getIntegerPropertyState(); 
        int incAvg = (integerStateTotal/messageCount);
        int change = 0;
        if(current != 0)
            change = (current + incAvg)/2;
        else 
            change = incAvg;

        // update our state     
        myDigitalTwin.setIntegerPropertyState(change);
        return ProcessingResult.UpdateDigitalTwin;
    }
}
```

### Quickstart

Now that the basics of the real-time DigitalTwin model are out of the way, we can explore building, deploying, and sending messages to our first real-time digital twin! 

As a prerequisite, we will need Java installed and the JAVA_HOME environment variable set. Additionally, we will need ScaleOut StateServer installed and configured. To do this, follow the [installation guide](https://static.scaleoutsoftware.com/docs/user_guide/installation/installation.html) on our website. Once Java and ScaleOut StateServer are installed we can continue with the DigitalTwin quickstart. 

Let's begin by creating a Java project and setting up our classpath. We will need to add the following JARs to our classpath:

For Gradle, add the ScaleOut repository to your build.gradle:

``` 
repositories {
    mavenCentral()
    maven {
        url "https://repo.scaleoutsoftware.com/repository/external"
    }
}
```

Once the repository is added to your build.gradle, add the Digital Twin APIs as dependencies:
 
``` 
compile group: 'com.scaleoutsoftware.digitaltwin', name: "core", version: '0.5-BETA'
compile group: 'com.scaleoutsoftware.digitaltwin', name: "deployment", version: '0.5-BETA'
compile group: 'com.scaleoutsoftware.digitaltwin', name: "datasource", version: '0.5-BETA'
```

For Maven, add the ScaleOut repository to your pom.xml: 

```
<repository>
    <id>ScaleOut API Repository</id>
    <url>https://repo.scaleoutsoftware.com/repository/external</url>
</repository>
```

Once the repository is added to your pom.xml, add the DigitalTwin APIs as dependencies:

``` 
<dependencies>
	<!-- ... -->
	<!-- your dependencies -->
	<!-- ... -->
    <dependency>
      <groupId>com.scaleoutsoftware.digitaltwin</groupId>
      <artifactId>core</artifactId>
      <version>0.5-BETA</version>
    </dependency>
    <dependency>
      <groupId>com.scaleoutsoftware.digitaltwin</groupId>
      <artifactId>deployment</artifactId>
      <version>0.5-BETA</version>
    </dependency>
    <dependency>
      <groupId>com.scaleoutsoftware.digitaltwin</groupId>
      <artifactId>datasource</artifactId>
      <version>0.5-BETA</version>
    </dependency>
</dependencies>
```

Setting the Classpath directly:

```
classpath="/usr/local/soss/java_api/*.jar:/usr/local/soss/java_api/lib/*jar:..."
```

Now that our classpath is configured, we can setup our project and build our first DigitalTwin. Let's start by creating a new package such as the following:

```
com.digitaltwin.quickstart
```

Now inside that package, let's create three classes called "MyDigitalTwin.java", "MyMesssage.java", and "MyMessageProcessor.java". Once the files are created, let's copy and paste the code from the above example into the three classes.

Finally, we want to deploy the real-time DigitalTwin model and send a message to an individual real-time digital twin. Let's create a new class in the package called "Main.java". Inside this class, we will setup the real-time digital twin execution environment and send our first message. Let's start by setting up the environment, copy the following into "Main.java": 

```
import com.scaleoutsoftware.digitaltwin.core.SendingResult;
import com.scaleoutsoftware.digitaltwin.datasource.AppEndpoint;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironment;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironmentBuilder;
public class Main {
    public static void main(String[] args) throws Exception {
        // create the ExecutionEnvironment
        ExecutionEnvironment environment = new ExecutionEnvironmentBuilder()
                .addDigitalTwin("MyDigitalTwin", new MyMessageProcessor(), MyDigitalTwin.class, MyMessage.class)
                .build();
    }
}
```

At this stage we've created an entry point for the application and we've built the execution environment where the real-time digital twin model will live. We've told the execution environment about our MessageProcessor, DigitalTwin, and Message classes. Now, let's send a JSON encoded message to an ID which will trigger StreamServer to create an instance of a real-time digital twin for our DigitalTwinModel and then process the incoming message. Replace the contents of Main.java with the following:

```
import com.scaleoutsoftware.digitaltwin.core.SendingResult;
import com.scaleoutsoftware.digitaltwin.datasource.AppEndpoint;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironment;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironmentBuilder;
public class Main {
    public static void main(String[] args) throws Exception {
        // create the ExecutionEnvironment
        ExecutionEnvironment environment = new ExecutionEnvironmentBuilder()
                .addDigitalTwin("MyDigitalTwin", new MyMessageProcessor(), MyDigitalTwin.class, MyMessage.class)
                .build();
        // send a message to the id "Twin_ID_23"
        SendingResult result = AppEndpoint.send("MyDigitalTwin", "Twin_ID_23", "{\"myMessageType\":\"MyType\",\"incomingStringStateChange\":\"MyStringChange\",\"incomingIntegerStateChange\":50,\"timestamp\":1426325213}");
        switch (result) {
            case Handled:
                System.out.println("Event was handled.");
                break;
            case NotHandled:
                System.out.println("Event was not handled.");
                break;
        }
    }
}
``` 

We need to give StreamServer the classes we've just created so that the environment can create new real-time digital twins from our real-time digital twin model and linearly scale as our workload increases. Before we tell StreamServer, we'll want to create a JAR with all of our newly created classes. Once the JAR is created, we can add them as a dependency to the execution environment. To do this, replace the contents of Main.java with the following:

```
import com.scaleoutsoftware.digitaltwin.core.SendingResult;
import com.scaleoutsoftware.digitaltwin.datasource.AppEndpoint;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironment;
import com.scaleoutsoftware.digitaltwin.deployment.ExecutionEnvironmentBuilder;
public class Main {
    public static void main(String[] args) throws Exception {
        // create the ExecutionEnvironment
        ExecutionEnvironment environment = new ExecutionEnvironmentBuilder()
                // replace this line with the JAR you created
                .addDependencyJar("/path/to/myfirstdigitaltwin.jar")
                .addDigitalTwin("MyDigitalTwin", new MyMessageProcessor(), MyDigitalTwin.class, MyMessage.class)
                .build();
        SendingResult result = AppEndpoint.send("MyDigitalTwin", "Twin_ID_23", "{\"myMessageType\":\"MyType\",\"incomingStringStateChange\":\"MyStringChange\",\"incomingIntegerStateChange\":50,\"timestamp\":1426325213}");
        switch (result) {
            case Handled:
                System.out.println("StreamServer created a new instance of our DigitalTwin and the message was handled.");
                break;
            case NotHandled:
                System.out.println("Message was not handled and no DigitalTwin was created.");
                break;
        }
    }
}
```

Now we can run this program and watch as the entire execution pipeline takes place. The console output will show the message we printed, "StreamServer created a new instance of our DigitalTwin and the message was handled." Additionally, we can open the ScaleOut Object Browser and see an instance of our real-time digital twin that StreamServer instantiated for us as well as the change that the incoming message applied.

Additionally, advanced users may want to use third party datasources for sending and recieving messages to a real-time digital twin model. 

To support all versions of Kafka StreamServer does not ship with Kafka libraries. So, as a prerequisite we will need to place the Kafka libraries on each ScaleOut StateServer host. To do this, copy the JARs from the Kafka installations 'lib' directory and paste them into the 'java_api/lib/Kafka' directory (on Windows paste these JARs into 'JavaAPI/lib/Kafka'). For AWS IoT core, you can run the helper script in the installation directory to download necessary JARs.

Inside your app, you can use Kafka as a data source for sending messages by using the KafkaEndpoint class in the datasource API: 

```
KafkaEndpoint kafkaEndpoint = new KafkaEndpointBuilder(new File("/path/to/server.properties"))
                    .addTopic("DT_MODEL", "datasource_to_dt_topic", "dt_to_datasource_topic")
                    .build();
```

Inside your app, you can use AWS IoT core as a data source for sending messages by using the AwsIotCoreEndpoint class in the datasource API: 

```
AwsIotEndpoint awsIotEndpoint = new AwsIotEndpointBuilder(connectionName, clientEndpoint, clientId, certFilePath, secretFilePath)
					.load();
```
