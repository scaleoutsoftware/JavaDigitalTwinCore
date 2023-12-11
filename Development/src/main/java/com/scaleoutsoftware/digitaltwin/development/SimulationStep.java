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

import com.scaleoutsoftware.digitaltwin.core.SimulationStatus;

/**
 * The simulation step class encases the metadata for a completed interval of a simulation.
 */
public class SimulationStep {
    private SimulationStatus    _status;
    private long                _intervalTime;

    SimulationStep(SimulationStatus status, long intervalTime) {
        _status         = status;
        _intervalTime   = intervalTime;
    }

    /**
     * Retrieve the {@link SimulationStatus} of the simulation interval.
     * @return the current simulation status.
     */
    public SimulationStatus getStatus() {
        return _status;
    }

    /**
     * Retrieve the time of the simulation interval.
     * @return the simulation interval time.
     */
    public long getTime() {
        return _intervalTime;
    }

    // merge two simulation steps
    void merge(SimulationStep result) {
        if(this._status == SimulationStatus.Running && result._status != SimulationStatus.Running) {
            this._status = result._status;
        }
        if(this._intervalTime > result._intervalTime) {
            this._intervalTime = result.getTime();
        }
    }
}
