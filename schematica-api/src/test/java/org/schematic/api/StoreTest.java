/*
 * Schematica (http://www.schematica.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.schematic.api;

import java.util.Map;
import org.schematica.api.Document;
import org.schematica.api.Store;
import org.schematica.api.tasks.Filters;
import org.schematica.api.tasks.Mappers;
import org.schematica.api.tasks.Reducers;
import org.schematica.api.tasks.Sequence;
import org.schematica.api.tasks.Task;

/**
 * NOT A REAL TEST - JUST HERE TO HELP DEFINE THE API
 */
public class StoreTest {

    private Store store;

    public void countAllDocumentsWithFluentAPI() throws Exception {
        Task<Long> task = store.all().totalCount();
        Long count = task.call();
    }

    public void countSomeDocumentsWithFluentAPI() throws Exception {
        Task<Long> task = store.filter(Filters.passthrough()).totalCount();
        Long count = task.call();
    }

    public void getAllKeysWithFluentAPI() throws Exception {
        Task<Sequence<String>> task = store.all().keys();
        Sequence<String> keys = task.call();
    }

    public void getAllDocumentsWithFluentAPI() throws Exception {
        Task<Sequence<Document>> task = store.all().documents();
        Sequence<Document> docs = task.call();
    }

    public void getAllDocumentsByKeyWithFluentAPI() throws Exception {
        Task<Map<String, Document>> task = store.all().documentsByKey();
        Map<String, Document> docs = task.call();
    }

    public void getMaximumLongValueInAllDocumentsWithFluentAPI() throws Exception {
        Task<Map<String, Long>> task = store.all().map(Mappers.longValue("age")).reduce(Reducers.Longs.maximum());
        Long maxAge = task.call().get("age");
    }
}
