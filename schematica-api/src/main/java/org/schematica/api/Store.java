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

package org.schematica.api;

import java.util.Map;
import org.schematica.api.tasks.Filter;
import org.schematica.api.tasks.Mapper;
import org.schematica.api.tasks.Reducer;
import org.schematica.api.tasks.Sequence;
import org.schematica.api.tasks.Task;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Store {

    /**
     * Determine whehter the store contains a document with the given key.
     *
     * @param key the key; may not be null
     * @return true if the document contains a document with the key, or false otherwise
     */
    boolean has( String key );

    Document read( String key );

    void write( String key,
                Document document );

    boolean writeIfAbsent( String key,
                           Document document );

    void merge( String key,
                Document document );

    boolean remove( String key );

    TaskMaker filter( Filter filter );

    TaskMaker all();

    public static interface TaskMaker {
        Task<Long> totalCount();

        Task<Sequence<String>> keys();

        Task<Sequence<Document>> documents();

        Task<Map<String, Document>> documentsByKey();

        <Kout, Vout> Reducable<Kout, Vout> map( Mapper<Kout, Vout> mapper );
    }

    public static interface Reducable<Kout, Vout> {
        Task<Map<Kout, Vout>> reduce( Reducer<Kout, Vout> reducer );
    }

    Schemas getSchemas();

    Observation getObservation();

    void close();

}
