/*
 Copyright (c) 2023 by ScaleOut Software, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.scaleoutsoftware.digitaltwin.development;

import com.google.gson.Gson;
import com.scaleoutsoftware.digitaltwin.core.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class TestWorkbench {
    public static class SimpleDigitalTwin extends DigitalTwinBase {
        private String _stringProp;
        public SimpleDigitalTwin() {}
        public SimpleDigitalTwin(String stringProp) {
            _stringProp = stringProp;
        }
    }

    public static class SimpleMessage {
        String stringChange;
        int intChange;
        String payload;
        public SimpleMessage(String s, int i) {
            stringChange = s;
            intChange = i;
        }
    }

    public static class SimpleMessageProcessor extends MessageProcessor<SimpleDigitalTwin, SimpleMessage> implements Serializable {

        public SimpleMessageProcessor() {}

        @Override
        public ProcessingResult processMessages(ProcessingContext processingContext, SimpleDigitalTwin instance, Iterable<SimpleMessage> messages) {
            Gson gson = new Gson();
            Date currentTime = processingContext.getCurrentTime();
            PersistenceProvider provider = processingContext.getPersistenceProvider();
            if(processingContext.getDigitalTwinModel().compareTo(instance.getModel()) != 0) {
                throw new IllegalStateException(String.format("context.getModel and instance.getModel difer. %s:%s", processingContext.getDigitalTwinModel(), instance.getModel()));
            }
            if(processingContext.getDataSourceId().compareTo(instance.getId()) != 0) {
                throw new IllegalStateException(String.format("context.getModel and instance.getModel difer. %s:%s", processingContext.getDigitalTwinModel(), instance.getModel()));
            }

            if(instance.getModel().compareTo("SimSimple") == 0) {// if this is a simulation model...
                for(SimpleMessage msg : messages) {
                    System.out.println(msg.stringChange);
                }
            } else {
                for(SimpleMessage msg : messages) {
                    switch (msg.stringChange) {
                        case "SendToSource":
                            processingContext.sendToDataSource(new SimpleMessage("Hello from data source!", 10));
                            break;
                        case "SendToTwin":
                            String jsonMsg = gson.toJson(msg);
                            byte[] bytes = jsonMsg.getBytes(StandardCharsets.UTF_8);
                            processingContext.sendToDigitalTwin(msg.payload == null ? msg.payload : "model", msg.intChange+"",bytes);
                            break;
                        case "LogMessage":
                            processingContext.logMessage(Level.INFO, msg.payload);
                            break;
                        case "StartTimer":
                            processingContext.startTimer("timer", Duration.ofMillis(1000), TimerType.Recurring,
                                    new TimerHandler<DigitalTwinBase>() {
                                        @Override
                                        public ProcessingResult onTimedMessage(String s, DigitalTwinBase digitalTwinBase, ProcessingContext processingContext) {
                                            System.out.println("Hello from real-time timer.");
                                            return ProcessingResult.UpdateDigitalTwin;
                                        }
                                    });
                            break;
                        case "StopTimer":
                            processingContext.stopTimer("timer");
                            break;
                        case "SharedData":
                            SharedData sharedData = processingContext.getSharedModelData();
                            CacheResult result = sharedData.put("Hello", "Some string...".getBytes(StandardCharsets.UTF_8));
                            if(result.getStatus() == CacheOperationStatus.ObjectPut) {
                                System.out.println("Successfully stored object in model storage.");
                            }
                            result = sharedData.get("Hello");
                            if(result.getStatus() == CacheOperationStatus.ObjectRetrieved) {
                                System.out.println("Successfully retrieved " + new String(result.getValue(), StandardCharsets.UTF_8) + " from model storage.");
                            }
                            result = sharedData.remove("Hello");
                            if(result.getStatus() == CacheOperationStatus.ObjectRemoved) {
                                System.out.println("Successfully removed " + new String(result.getValue(), StandardCharsets.UTF_8) + " from model storage.");
                            }
                            sharedData = processingContext.getSharedGlobalData();
                            result = sharedData.put("Hello", "Some string...".getBytes(StandardCharsets.UTF_8));
                            if(result.getStatus() == CacheOperationStatus.ObjectPut) {
                                System.out.println("Successfully stored object in global storage.");
                            }
                            result = sharedData.get("Hello");
                            if(result.getStatus() == CacheOperationStatus.ObjectRetrieved) {
                                System.out.println("Successfully retrieved " + new String(result.getValue(), StandardCharsets.UTF_8) + " from global storage.");
                            }
                            result = sharedData.remove("Hello");
                            if(result.getStatus() == CacheOperationStatus.ObjectRemoved) {
                                System.out.println("Successfully removed " + new String(result.getValue(), StandardCharsets.UTF_8) + " from global storage.");
                            }
                            break;
                        case "WakeUp":
                            SimulationController controller = processingContext.getSimulationController();
                            instance._stringProp = "WakeUp";
                            System.out.println("Calling run this twin...");
                            controller.runThisInstance();
                            break;
                        default:
                            break;
                    }
                }
            }
            return ProcessingResult.UpdateDigitalTwin;
        }
    }

    public static class RealTimeCar extends DigitalTwinBase {
        private int _tirePressure;
        public RealTimeCar() { _tirePressure=0; }
        public RealTimeCar(int startingTirePressure) {
            _tirePressure = startingTirePressure;
        }

        public void incrementTirePressure(int increment) {
            _tirePressure += increment;
        }

        public int getTirePressure() {
            return _tirePressure;
        }
    }

    public static class TirePressureMessage {
        final int _pressureChange;
        public TirePressureMessage(int pressureChange) {
            _pressureChange = pressureChange;
        }

        public int getPressureChange() {
            return _pressureChange;
        }
    }

    public static class RealTimeCarMessageProcessor extends MessageProcessor<RealTimeCar, TirePressureMessage> implements Serializable {
        final int TIRE_PRESSURE_FULL = 100;
        @Override
        public ProcessingResult processMessages(ProcessingContext processingContext, RealTimeCar car, Iterable<TirePressureMessage> messages) throws Exception {
            // apply the updates from the messages
            for(TirePressureMessage message : messages) {
                car.incrementTirePressure(message.getPressureChange());
            }
            if(car.getTirePressure() > TIRE_PRESSURE_FULL) {
                processingContext.sendToDataSource(new TirePressureMessage(car.getTirePressure()));
            }
            return ProcessingResult.UpdateDigitalTwin;
        }
    }

    public static class SimulationPump extends DigitalTwinBase {
        private double _tirePressureChange;
        private boolean _tirePressureReached = false;
        public SimulationPump() {}
        public SimulationPump(double pressureChange) {
            _tirePressureChange = pressureChange;
        }

        public double getTirePressureChange() {
            return _tirePressureChange;
        }

        public void setTirePressureReached() {
            _tirePressureReached = true;
        }

        public boolean isTireFull() {
            return _tirePressureReached;
        }
    }

    public static class SimulatedPumpMessageProcessor extends MessageProcessor<SimulationPump, TirePressureMessage> implements Serializable {
        @Override
        public ProcessingResult processMessages(ProcessingContext processingContext, SimulationPump simCar, Iterable<TirePressureMessage> messages) throws Exception {
            // apply the updates from the messages
            simCar.setTirePressureReached();
            return ProcessingResult.UpdateDigitalTwin;
        }
    }

    public static class PumpSimulationProcessor extends SimulationProcessor<SimulationPump> implements Serializable {
        @Override
        public ProcessingResult processModel(ProcessingContext processingContext, SimulationPump simPump, Date date) {
            SimulationController controller = processingContext.getSimulationController();
            if(simPump.isTireFull()) {
                controller.deleteThisInstance();
            } else {
                int change = (int) (100 * simPump.getTirePressureChange());
                controller.emitTelemetry("RealTimeCar", new TirePressureMessage(change));
            }
            return ProcessingResult.UpdateDigitalTwin;
        }
    }

    public static class SimpleSimProcessor extends SimulationProcessor<SimpleDigitalTwin> implements Serializable {
        private Gson            _gson = new Gson();
        private AtomicInteger   timesInvoked = new AtomicInteger(0);
        private boolean         _useJson;
        private String          _modelIdToMessage;
        private String          _instanceIdToMessage;

        public SimpleSimProcessor(boolean json) {
            _useJson = json;
        }

        public SimpleSimProcessor(String modelIdToMessage, String instanceIdToMessage) {
            _useJson                = false;
            _modelIdToMessage       = modelIdToMessage;
            _instanceIdToMessage    = instanceIdToMessage;
        }

        public int getTimesInvoked() {
            return timesInvoked.get();
        }

        @Override
        public ProcessingResult processModel(ProcessingContext processingContext, SimpleDigitalTwin simpleDigitalTwin, Date date) {
            timesInvoked.addAndGet(1);
            SimulationController controller = processingContext.getSimulationController();
            if(simpleDigitalTwin.getId().compareTo("stop") == 0) {
                controller.stopSimulation();
                return ProcessingResult.UpdateDigitalTwin;
            } else if(simpleDigitalTwin.getId().contains("delay")) {
                controller.delay(Duration.ofSeconds(600));
                return ProcessingResult.UpdateDigitalTwin;
            } else if (simpleDigitalTwin.getId().contains("timer")) {
                processingContext.startTimer("timer", Duration.ofMillis(1000), TimerType.OneTime, new TimerHandler<>() {
                    @Override
                    public ProcessingResult onTimedMessage(String s, DigitalTwinBase digitalTwinBase, ProcessingContext processingContext) {
                        System.out.println("timer called!");
                        return ProcessingResult.UpdateDigitalTwin;
                    }
                });
                return ProcessingResult.UpdateDigitalTwin;
            } else if (simpleDigitalTwin.getId().contains("log")) {
                processingContext.logMessage(Level.INFO, simpleDigitalTwin._stringProp);
                processingContext.logMessage(Level.WARNING, simpleDigitalTwin._stringProp);
                processingContext.logMessage(Level.SEVERE, simpleDigitalTwin._stringProp);
                return ProcessingResult.UpdateDigitalTwin;
            } else if (simpleDigitalTwin.getId().contains("alert")) {
                processingContext.sendAlert("alert", new AlertMessage(simpleDigitalTwin.getId(), simpleDigitalTwin.getId(), simpleDigitalTwin._stringProp));
                return ProcessingResult.UpdateDigitalTwin;
            } else if (simpleDigitalTwin.getId().contains("sleeper")) {
                if(simpleDigitalTwin._stringProp.compareTo("WakeUp") == 0) {
                    System.out.println("Model ran after runThisInstance");
                    simpleDigitalTwin._stringProp = "asleep";
                }
                System.out.println("Going to sleep...");
                controller.delayIndefinitely();
                return ProcessingResult.UpdateDigitalTwin;
            } else if (simpleDigitalTwin.getId().contains("waker")) {
                System.out.println("Waking up sleeper...");
                processingContext.sendToDigitalTwin(_modelIdToMessage, _instanceIdToMessage, new SimpleMessage("WakeUp", 23));
                return ProcessingResult.UpdateDigitalTwin;
            }
            long delay = Long.parseLong(simpleDigitalTwin.getId());
            controller.delay(Duration.ofSeconds(delay));
            if(_useJson) {
                byte[] msg = _gson.toJson(new SimpleMessage("SendToSource", 23)).getBytes(StandardCharsets.UTF_8);
                controller.emitTelemetry("Simple", msg);
            } else {
                SimpleMessage telemetry = new SimpleMessage("SendToSource", 23);
                controller.emitTelemetry("Simple", telemetry);
            }
            return ProcessingResult.UpdateDigitalTwin;
        }
    }

    @Test
    public void TestWorkbenchNoInstances() throws WorkbenchException {
        Gson gson = new Gson();
        SimulationStep result;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), new SimpleSimProcessor(false), SimpleDigitalTwin.class, SimpleMessage.class);

            result = workbench.runSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1, 1000);
            Assert.assertSame(SimulationStatus.NoRemainingWork, result.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchOnlySimulationInstances() throws WorkbenchException {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        Gson gson = new Gson();
        SimulationStep result;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int i = 0; i < 1; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("hello" + i);
                workbench.addInstance("SimSimple", "" + i, instance);
            }

            result = workbench.runSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1, 1000);
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(60, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void TestWorkbenchSample() throws WorkbenchException {
        long expectedStop;
        SimulationStep step;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("RealTimeCar", new RealTimeCarMessageProcessor(), RealTimeCar.class, TirePressureMessage.class);
            workbench.addSimulationModel("SimPump", new SimulatedPumpMessageProcessor(), new PumpSimulationProcessor(), SimulationPump.class, TirePressureMessage.class);

            workbench.addInstance("SimPump", "23", new SimulationPump(0.29d));
            long start = System.currentTimeMillis();
            long end = start + 60000;
            expectedStop = start + 5000;
            step = workbench.initializeSimulation(start, end, 1000);

            while (step.getStatus() == SimulationStatus.Running) {
                step = workbench.step();
                HashMap<String, DigitalTwinBase> realTimeCars = workbench.getInstances("RealTimeCar");
                RealTimeCar rtCar = (RealTimeCar) realTimeCars.get("23");
                System.out.println("rtCar: " + rtCar.getTirePressure());
            }
            Assert.assertEquals(expectedStop, step.getTime());
            System.out.println("Simulation completed. Sim status: " + step.getStatus() + " endTime: " + step.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchSampleRun() throws WorkbenchException {
        long expectedStop;
        SimulationStep step;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("RealTimeCar", new RealTimeCarMessageProcessor(), RealTimeCar.class, TirePressureMessage.class);
            workbench.addSimulationModel("SimModel", new SimulatedPumpMessageProcessor(), new PumpSimulationProcessor(), SimulationPump.class, TirePressureMessage.class);

            workbench.addInstance("SimModel", "23", new SimulationPump(0.29d));
            long start = System.currentTimeMillis();
            long end = start + 60000;
            expectedStop = start + 5000;
            step = workbench.runSimulation(start, end, 1, 1000);
            Assert.assertEquals(expectedStop, step.getTime());
            System.out.println("Simulation completed. Sim status: " + step.getStatus() + " endTime: " + step.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchSpeedup() throws WorkbenchException {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        Gson gson = new Gson();
        SimulationStep result;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int i = 0; i < 1000; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("hello" + i);
                workbench.addInstance("SimSimple", "" + i, instance);
            }

            long start = System.currentTimeMillis();
            result = workbench.runSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 100, 1000);
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(1308, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchDebug() throws WorkbenchException{
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        for(int i = 0; i < 20; i ++) {
            SimpleSimProcessor processor = new SimpleSimProcessor(i >= 10);
            SimulationStep result;
            long start;
            try (Workbench workbench = new Workbench()) {
                workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
                workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

                for (int twinCount = 0; twinCount < 1000; twinCount++) {
                    DigitalTwinBase instance = new SimpleDigitalTwin("hello" + twinCount);
                    workbench.addInstance("SimSimple", "" + twinCount, instance);
                }
                result = workbench.initializeSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1000);
                start = System.currentTimeMillis();
                while (result.getStatus() == SimulationStatus.Running) {
                    result = workbench.step();
                    if (result.getStatus() == SimulationStatus.Running) {
                        numItterations++;
                    }
                }
                long stop = System.currentTimeMillis();
                System.out.println("RunTime: " + (stop-start));
                Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
                Assert.assertEquals(1308, processor.getTimesInvoked());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Test
    public void testWorkbenchDebugOnlySimulationInstances() throws WorkbenchException {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        Gson gson = new Gson();
        long stopTimeMs;
        SimulationStep step;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int i = 0; i < 1; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("hello" + i);
                workbench.addInstance("SimSimple", "" + i, instance);
            }

            long startTimeMs = System.currentTimeMillis();
            stopTimeMs = startTimeMs + 60000;
            long expectedTm = startTimeMs;
            step = workbench.initializeSimulation(startTimeMs, stopTimeMs, 1000);
            while (step.getStatus() == SimulationStatus.Running) {
                step = workbench.step();
                Assert.assertEquals(expectedTm, step.getTime());
                expectedTm += 1000;
            }
            Assert.assertSame(SimulationStatus.EndTimeReached, step.getStatus());
            Assert.assertEquals(stopTimeMs, step.getTime());
            Assert.assertEquals(60, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchStop() throws WorkbenchException{
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        SimulationStep result;
        long start;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int twinCount = 0; twinCount < 1000; twinCount++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("hello" + twinCount);
                workbench.addInstance("SimSimple", "" + twinCount, instance);
            }
            DigitalTwinBase instance = new SimpleDigitalTwin("stop");
            workbench.addInstance("SimSimple", "stop", instance);

            result = workbench.initializeSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1000);
            start = System.currentTimeMillis();
            while (result.getStatus() == SimulationStatus.Running) {
                result = workbench.step();
            }
            long stop = System.currentTimeMillis();
            System.out.println("RunTime: " + (stop-start));
            Assert.assertSame(SimulationStatus.InstanceRequestedStop, result.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void TestWorkbenchHugeDelays() throws WorkbenchException{
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        SimulationStep result;
        long start;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int twinCount = 0; twinCount < 1000; twinCount++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("delay" + twinCount);
                workbench.addInstance("SimSimple", "delay" + twinCount, instance);
            }

            result = workbench.initializeSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1000);
            start = System.currentTimeMillis();
            while (result.getStatus() == SimulationStatus.Running) {
                result = workbench.step();
            }
            long stop = System.currentTimeMillis();
            System.out.println("RunTime: " + (stop-start));
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(1000, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void TestWorkbenchTimer() throws WorkbenchException{
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        SimulationStep result;
        long start;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int twinCount = 0; twinCount < 1; twinCount++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("timer" + twinCount);
                workbench.addInstance("SimSimple", "timer" + twinCount, instance);
            }

            result = workbench.initializeSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1000);
            start = System.currentTimeMillis();
            while (result.getStatus() == SimulationStatus.Running) {
                result = workbench.step();
            }
            long stop = System.currentTimeMillis();
            System.out.println("RunTime: " + (stop-start));
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(60, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void TestWorkbenchRealtimeTimer() throws WorkbenchException, InterruptedException {
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);

            for (int twinCount = 0; twinCount < 1; twinCount++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("timer" + twinCount);
                workbench.addInstance("Simple", "timer" + twinCount, instance);
            }

            List<Object> messages = new LinkedList<>();
            messages.add(new SimpleMessage("StartTimer", 0));
            workbench.send("Simple", "timer0", messages);

            Thread.sleep(3000);
            messages = new LinkedList<>();
            messages.add(new SimpleMessage("StopTimer", 0));
            workbench.send("Simple", "timer0", messages);
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void TestWorkbenchRealtimeTimerScope() throws WorkbenchException, InterruptedException {
        Gson gson = new Gson();
        int numInstance = 10;
        int numItterations = 0;
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);

            for(int twinCount = 0; twinCount < 1; twinCount++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("timer"+twinCount);
                workbench.addInstance("Simple", "timer" + twinCount, instance);
            }

            List<Object> messages = new LinkedList<>();
            messages.add(new SimpleMessage("StartTimer", 0));
            workbench.send("Simple", "timer0", messages);
            Thread.sleep(3000);
        } catch (Exception e) {
            Assert.fail();
        }
        Thread.sleep(5000);
    }

    @Test
    public void TestWorkbenchSimulationLogMessage() throws WorkbenchException {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        Gson gson = new Gson();
        String logMessageContent;
        long exp;
        long start;
        long stop;
        List<LogMessage> messages;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            logMessageContent = "this is a log message";
            for (int i = 0; i < 1; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin(logMessageContent);
                workbench.addInstance("SimSimple", "log" + i, instance);
            }

            exp = 60;
            start = System.currentTimeMillis();
            stop = start + (exp * 1000);
            SimulationStep result = workbench.runSimulation(start, stop, 1, 1000);
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(stop, result.getTime());
            Assert.assertEquals(exp, processor.getTimesInvoked());
            messages = workbench.getLoggedMessages("SimSimple", start);
            Assert.assertEquals(messages.size(), exp * 3);
            for (LogMessage message : messages) {
                Assert.assertSame(logMessageContent, message.getMessage());
                Assert.assertTrue(message.getSeverity() == Level.INFO ||
                        message.getSeverity() == Level.WARNING ||
                        message.getSeverity() == Level.SEVERE);
                Assert.assertTrue(message.getTimestamp() >= start && message.getTimestamp() < stop);
            }

            messages = workbench.getLoggedMessages("SimSimple", 0L);
            Assert.assertEquals(messages.size(), exp * 3);
            for (LogMessage message : messages) {
                Assert.assertSame(logMessageContent, message.getMessage());
                Assert.assertTrue(message.getSeverity() == Level.INFO ||
                        message.getSeverity() == Level.WARNING ||
                        message.getSeverity() == Level.SEVERE);
                Assert.assertTrue(message.getTimestamp() >= start && message.getTimestamp() < stop);
            }

            messages = workbench.getLoggedMessages("SimSimple", start + 5000);
            Assert.assertEquals(messages.size(), (exp*3 - (5*3)));
            for(LogMessage message : messages) {
                Assert.assertSame(logMessageContent, message.getMessage());
                Assert.assertTrue(message.getSeverity() == Level.INFO ||
                        message.getSeverity() == Level.WARNING ||
                        message.getSeverity() == Level.SEVERE);
                Assert.assertTrue(message.getTimestamp() >= start && message.getTimestamp() < stop);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchSimulationAlertMessage() throws WorkbenchException {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        Gson gson = new Gson();
        SimulationStep result;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addAlertProvider("SimSimple", new AlertProviderConfiguration("test", "www.url.com", "integrationkey", "routingKey", "alert", "entityId"));

            String alertMessageContent = "this is an alert message";
            String id = "alert";
            for (int i = 0; i < 1; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin(alertMessageContent);
                workbench.addInstance("SimSimple", id, instance);
            }
            long exp   = 60;
            long start = System.currentTimeMillis();
            long stop  = start + (exp * 1000);
            result = workbench.runSimulation(System.currentTimeMillis(), System.currentTimeMillis() + 60000, 1, 1000);
            Assert.assertSame(SimulationStatus.EndTimeReached, result.getStatus());
            Assert.assertEquals(stop, result.getTime());
            Assert.assertEquals(exp, processor.getTimesInvoked());
            List<AlertMessage> messages = workbench.getAlertMessages("SimSimple", "alert");
            Assert.assertEquals(messages.size(), exp);
            for(AlertMessage msg : messages) {
                Assert.assertSame(msg.getMessage(), alertMessageContent);
                Assert.assertSame(msg.getSeverity(), id);
                Assert.assertSame(msg.getTitle(), id);
                Assert.assertNull(msg.getOptionalTwinInstanceProperties());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = WorkbenchException.class)
    public void TestWorkbenchException() throws Exception {
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", null, SimpleDigitalTwin.class, SimpleMessage.class);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void TestWorkbenchPeek() throws Exception {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        long stopTimeMs;
        SimulationStep step;
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);

            for (int i = 0; i < 1; i++) {
                DigitalTwinBase instance = new SimpleDigitalTwin("hello" + i);
                workbench.addInstance("SimSimple", "" + i, instance);
            }

            long startTimeMs = System.currentTimeMillis();
            stopTimeMs = startTimeMs + 60000;
            long expectedTm = startTimeMs;
            step = workbench.initializeSimulation(startTimeMs, stopTimeMs, 1000);
            while (step.getStatus() == SimulationStatus.Running) {
                step = workbench.step();
                Assert.assertEquals(expectedTm, step.getTime());
                expectedTm += 1000;
                if(step.getStatus() == SimulationStatus.Running) {
                    Date peek = workbench.peek();
                    Assert.assertEquals(expectedTm, peek.getTime());
                }
            }
            Assert.assertSame(SimulationStatus.EndTimeReached, step.getStatus());
            Assert.assertEquals(stopTimeMs, step.getTime());
            Assert.assertEquals(60, processor.getTimesInvoked());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWorkbenchGenerateModelSchema() throws Exception {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);
            String schemaAsJson = workbench.generateModelSchema("Simple");
            Assert.assertEquals(schemaAsJson, "{\"modelType\":\"com.scaleoutsoftware.digitaltwin.development.TestWorkbench$SimpleDigitalTwin\",\"messageProcessorType\":\"com.scaleoutsoftware.digitaltwin.development.TestWorkbench$SimpleMessageProcessor\",\"messageType\":\"com.scaleoutsoftware.digitaltwin.development.TestWorkbench$SimpleMessage\",\"assemblyName\":\"NOT_USED_BY_JAVA_MODELS\",\"enablePersistence\":false,\"enableSimulationSupport\":false,\"alertProviders\":[]}");
            String dir = workbench.generateModelSchema("SimSimple", System.getProperty("user.dir"));
            Assert.assertEquals(String.format("%s\\model.json", System.getProperty("user.dir")), dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test (expected = WorkbenchException.class)
    public void TestWorkbenchGenerateModelSchemaExceptionally() throws Exception {
        SimpleSimProcessor processor = new SimpleSimProcessor(false);
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("SimSimple", new SimpleMessageProcessor(), processor, SimpleDigitalTwin.class, SimpleMessage.class);
            String schemaAsJson = workbench.generateModelSchema("");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void TestWorkbenchSharedData() throws Exception {
        try (Workbench workbench = new Workbench()) {
            workbench.addRealTimeModel("Simple", new SimpleMessageProcessor(), SimpleDigitalTwin.class, SimpleMessage.class);
            LinkedList<Object> messages = new LinkedList<>();
            messages.add(new SimpleMessage("SharedData", 29));
            workbench.send("Simple", "23", messages);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void TestWorkbenchRunThisInstance() throws Exception {
        try (Workbench workbench = new Workbench()) {
            workbench.addSimulationModel("Simple", new SimpleMessageProcessor(), new SimpleSimProcessor("Simple2", "sleeper"), SimpleDigitalTwin.class, SimpleMessage.class);
            workbench.addSimulationModel("Simple2", new SimpleMessageProcessor(), new SimpleSimProcessor(false), SimpleDigitalTwin.class, SimpleMessage.class);

            workbench.addInstance("Simple", "waker", new SimpleDigitalTwin("waker"));
            workbench.addInstance("Simple2", "sleeper", new SimpleDigitalTwin("sleeper"));
            long startTimeMs = System.currentTimeMillis();
            long stopTimeMs = startTimeMs + 15000L;
            long step = 1000L;
            workbench.runSimulation(startTimeMs, stopTimeMs, 1, step);
        } catch (Exception e) {
            throw e;
        }
    }
}
