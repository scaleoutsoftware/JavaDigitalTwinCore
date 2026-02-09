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

import com.scaleoutsoftware.digitaltwin.abstractions.CacheOperationStatus;
import com.scaleoutsoftware.digitaltwin.abstractions.CacheResult;
import com.scaleoutsoftware.digitaltwin.abstractions.SharedData;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

class WorkbenchSharedData implements SharedData {
    private final HashMap<String, byte[]> data;

    public WorkbenchSharedData(HashMap<String, byte[]> shared) {
        data = shared;
    }
    @Override
    public CompletableFuture<CacheResult> get(String s) {
        return CompletableFuture.completedFuture(new CacheResult() {
            @Override
            public String getKey() {
                return s;
            }

            @Override
            public byte[] getValue() {
                return data.getOrDefault(s, null);
            }

            @Override
            public CacheOperationStatus getStatus() {
                return data.containsKey(s) ? CacheOperationStatus.ObjectRetrieved : CacheOperationStatus.ObjectDoesNotExist;
            }
        });

    }

    @Override
    public CompletableFuture<CacheResult> put(String s, byte[] bytes) {
        data.put(s, bytes);
        return CompletableFuture.completedFuture(new CacheResult() {
            @Override
            public String getKey() {
                return s;
            }

            @Override
            public byte[] getValue() {
                return bytes;
            }

            @Override
            public CacheOperationStatus getStatus() {
                return CacheOperationStatus.ObjectPut;
            }
        });
    }

    @Override
    public CompletableFuture<CacheResult> remove(String s) {
        byte[] v = data.remove(s);
        return CompletableFuture.completedFuture(new CacheResult() {
            @Override
            public String getKey() {
                return s;
            }

            @Override
            public byte[] getValue() {
                return v;
            }

            @Override
            public CacheOperationStatus getStatus() {
                return v == null ? CacheOperationStatus.ObjectDoesNotExist : CacheOperationStatus.ObjectRemoved;
            }
        });
    }

    @Override
    public CompletableFuture<CacheResult> clear() {
        data.clear();
        return CompletableFuture.completedFuture(new CacheResult() {
            @Override
            public String getKey() {
                return null;
            }

            @Override
            public byte[] getValue() {
                return null;
            }

            @Override
            public CacheOperationStatus getStatus() {
                return CacheOperationStatus.CacheCleared;
            }
        });
    }
}
