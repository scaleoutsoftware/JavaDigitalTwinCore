/*
 Copyright (c) 2025 by ScaleOut Software, Inc.

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

import java.util.Date;

class SimulationTime {
    private long _currentSimulationEpochMs;
    private long _simulationIntervalMs;

    public SimulationTime(long currentSimulationEpochMs, long intervalMs) {
        _currentSimulationEpochMs   = currentSimulationEpochMs;
        _simulationIntervalMs       = intervalMs;
    }

    long getCurrentSimulationTime() {
        return _currentSimulationEpochMs;
    }

    long getNextSimulationTime() {
        return _currentSimulationEpochMs + _simulationIntervalMs;
    }
}
