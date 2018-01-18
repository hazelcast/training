/*
 * Copyright (c) 2008-2016, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DummyDatabase {
    private final Map<Integer, String> database = new ConcurrentHashMap<>();

    public DummyDatabase() {
        for (int i = 0; i < 20; i++) {
            database.put(i, "value-" + i);
        }
    }

    public String select(int key) {
        return database.get(key);
    }

    public boolean remove(int key) {
        return database.remove(key) != null;
    }

    public void store(int key, String value) {
        database.put(key, value);
    }

    public int[] selectKeys() {
        return database.keySet().stream().mapToInt(i -> i).toArray();
    }
}
