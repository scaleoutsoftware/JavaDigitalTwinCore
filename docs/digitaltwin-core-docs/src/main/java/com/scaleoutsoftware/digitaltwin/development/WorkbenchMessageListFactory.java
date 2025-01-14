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

import com.google.gson.Gson;
import com.scaleoutsoftware.digitaltwin.core.MessageFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

class WorkbenchMessageListFactory implements MessageFactory {
    private String          _serializedJsonList;
    private List<Object>    _messages;
    private Class<?>        _type;

    WorkbenchMessageListFactory(String serializedJson, Class<?> type) {
        _serializedJsonList = serializedJson;
        _messages           = null;
        _type               = type;
    }

    WorkbenchMessageListFactory(List<Object> messages, Class<?> type) {
        _serializedJsonList = null;
        _messages           = messages;
        _type               = type;
    }

    @Override
    public <V> Iterable<V> getIncomingMessages() {
        if (_messages != null) {
            return (Iterable<V>) _messages;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(_serializedJsonList, getTypedList(_type));
        }
    }

    private Type getTypedList(final Class<?> paramClass) {
        return new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return new Type[]{paramClass};
            }

            public Type getRawType() {
                return List.class;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
