package com.scaleoutsoftware.digitaltwin.development;

import com.scaleoutsoftware.digitaltwin.core.InitSimulationContext;
import com.scaleoutsoftware.digitaltwin.core.SharedData;

public class WorkbenchInitSimulationContext implements InitSimulationContext {
    SharedData _globalData;
    SharedData _modelData;

    public WorkbenchInitSimulationContext(SharedData globalData, SharedData modelData) {
        _globalData = globalData;
        _modelData = modelData;
    }

    @Override
    public SharedData getSharedModelData() {
        return _modelData;
    }

    @Override
    public SharedData getSharedGlobalData() {
        return _globalData;
    }
}
