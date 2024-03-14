package com.scaleoutsoftware.digitaltwin.development;

import com.scaleoutsoftware.digitaltwin.core.CacheOperationStatus;
import com.scaleoutsoftware.digitaltwin.core.CacheResult;
import com.scaleoutsoftware.digitaltwin.core.SharedData;

import java.util.HashMap;

public class WorkbenchSharedData implements SharedData {
    private final HashMap<String, byte[]> data;

    public WorkbenchSharedData(HashMap<String, byte[]> shared) {
        data = shared;
    }
    @Override
    public CacheResult get(String s) {
        return new CacheResult() {
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
        };
    }

    @Override
    public CacheResult put(String s, byte[] bytes) {
        data.put(s, bytes);
        return new CacheResult() {
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
        };
    }

    @Override
    public CacheResult remove(String s) {
        byte[] v = data.remove(s);
        return new CacheResult() {
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
        };
    }

    @Override
    public CacheResult clear() {
        data.clear();
        return new CacheResult() {
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
        };
    }
}
