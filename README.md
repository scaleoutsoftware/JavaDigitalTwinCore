# ScaleOut DigitalTwin Builder&trade; for ScaleOut StreamServer&trade;

The ScaleOut Digital Twin Builder™ lets developers build real-time digital twin models of data sources for stateful stream-processing using ScaleOut StreamServer™. These models enable applications to associate parameters and dynamic state for each data source that generates messages; this information provides additional context for introspecting on incoming messages. A real-time digital twin model consists of a state object and a message processor, both defined using base classes provided by the API library. Additional APIs enable deployment of real-time digital twin models to ScaleOut StreamServer for real-time message processing.

Instances of digital twins are serialized using JSON and stored within ScaleOut StateServer’s in-memory data grid for fast access. Incoming messages from data sources are delivered to instances of the real-time digital twin, each identified by a model name (string) and an instance identifier (string). During message processing, outbound messages can be sent back to data sources and/or to other instances of real-time digital twins.

For concepts and sample usage of the ScaleOut Digital Twin Builder, please read the ScaleOut Digital Twin Builder User Guide, which you can find on our website.

