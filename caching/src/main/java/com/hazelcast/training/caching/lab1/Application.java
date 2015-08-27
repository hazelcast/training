/*
 * Copyright (c) 2008-2010, Hazel Ltd. All Rights Reserved.
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
 *
 */

package com.hazelcast.training.caching.lab1;

import com.hazelcast.training.caching.common.cache.MyCache;
import com.hazelcast.training.caching.dto.Associate;
import com.hazelcast.training.caching.dto.Company;

import java.util.ArrayList;
import java.util.List;

public class Application {

    final MyCache<Integer, Company> companyCache;

    public Application(MyCache cache) {
        companyCache = cache;
    }

    public void run() {
        if (companyCache.size() == 2) {
            System.out.println("Printing the cache content");
            System.out.println(companyCache.get(1));
            System.out.println(companyCache.get(2));
        } else {
            System.out.println("Cache is empty, populating the cache");
            List<Associate> hazelcastAssociates = new ArrayList<>();
            hazelcastAssociates.add(new Associate("Fuad Malikov"));
            hazelcastAssociates.add(new Associate("Talip Ozturk"));
            hazelcastAssociates.add(new Associate("Viktor Gamov"));
            Company hazelcast = new Company("Hazelcast");
            hazelcast.setAssociates(hazelcastAssociates);
            companyCache.put(1, hazelcast);
            List<Associate> googleAssociates = new ArrayList<>();
            googleAssociates.add(new Associate("Larry Page"));
            googleAssociates.add(new Associate("Sergey Brin"));
            Company google = new Company("Google");
            google.setAssociates(googleAssociates);
            companyCache.put(2, google);
        }
    }
}
