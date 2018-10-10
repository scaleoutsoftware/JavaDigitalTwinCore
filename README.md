# ScaleOut DigitalTwin Builder&trade; for ScaleOut StreamServer&trade;

The Scaleout DigitalTwin Builder allows developers to simulate, communicate, and introspect on Edge devices with straight forward APIs built in Java.  

#Quickstart 

1) Create a class representation of your edge Device - a DigitalTwin:

public class WindTurbine extends DigitalTwin {
    private static final int RPM_MIN = 60;
    private static final int RPM_MAX = 300;
    private String Region;
    private double RPM;

    public WindTurbine() {}

    public void setRegion(String region) {Region = region;}
    public String getRegion() {return Region;}
    public void setRPM(double rpm) {RPM = rpm;}
    public double getRPM() {return RPM;}
} 

2) Create a MessageProcessor




