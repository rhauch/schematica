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

package org.schematic.db;

import java.util.Map;
import org.schematica.db.Document;
import org.schematica.db.Schematica;
import org.schematica.db.Sequence;
import org.schematica.db.Store;
import org.schematica.db.task.FilterBuilder;
import org.schematica.db.task.MapperBuilder;
import org.schematica.db.task.ReducerBuilder;
import org.schematica.db.task.Task;

/**
 * NOT A REAL TEST - JUST HERE TO HELP DEFINE THE API
 */
public class StoreTest {

    private Store store;
    private FilterBuilder filters = Schematica.filters();
    private MapperBuilder mappers = Schematica.mappers();
    private ReducerBuilder reducers = Schematica.reducers();

    public void countAllDocumentsWithFluentAPI() throws Exception {
        Task<Long> task = store.all().totalCount();
        Long count = task.call().output();
    }

    public void countSomeDocumentsWithFluentAPI() throws Exception {
        Task<Long> task = store.filter(filters.selectAll()).totalCount();
        Long count = task.call().output();
    }

    public void getAllKeysWithFluentAPI() throws Exception {
        Task<Sequence<String>> task = store.all().keys();
        Sequence<String> keys = task.call().output();
    }

    public void getAllDocumentsWithFluentAPI() throws Exception {
        Task<Sequence<Document>> task = store.all().documents();
        Sequence<Document> docs = task.call().output();
    }

    public void getAllDocumentsByKeyWithFluentAPI() throws Exception {
        Task<Map<String, Document>> task = store.all().documentsByKey();
        Map<String, Document> docs = task.call().output();
    }

    public void getMaximumLongValueInAllDocumentsWithFluentAPI() throws Exception {
        Task<Map<String, Long>> task = store.all().map(mappers.longValue("age")).reduce(reducers.longs().maximum());
        Long maxAge = task.call().output().get("age");
    }
}
