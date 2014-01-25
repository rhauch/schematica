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

package org.schematica.db.task;

import java.util.Map;
import org.schematica.db.Document;
import org.schematica.db.Sequence;
import org.schematica.db.Store;

/**
 * Component that completes the construction of {@link Task} instances.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @see Task
 * @see Store#filter(Filter)
 * @see Store#all()
 */
public interface TaskBuilder {

    /**
     * Create a task that obtains the total count of documents. The scope of the documents is defined by the method that returned
     * this {@link TaskBuilder} instance.
     * 
     * @return the task that computes and returns the number of documents
     */
    Task<Long> totalCount();

    /**
     * Create a task that obtains the keys of the documents. The scope of the documents is defined by the method that returned
     * this {@link TaskBuilder} instance.
     * 
     * @return the task that computes and returns the keys for the documents
     */
    Task<Sequence<String>> keys();

    /**
     * Create a task that obtains the documents. The scope of the documents is defined by the method that returned this
     * {@link TaskBuilder} instance.
     * 
     * @return the task that computes and returns the documents
     */
    Task<Sequence<Document>> documents();

    /**
     * Create a task that obtains a map of documents and their keys. The scope of the documents is defined by the method that
     * returned this {@link TaskBuilder} instance.
     * 
     * @return the task that computes and returns the documents mapped by their unique keys
     */
    Task<Map<String, Document>> documentsByKey();

    /**
     * Return a {@link Reducible} component that defines how the output of the map portion of MapReduce is to be reduced.
     * 
     * @param mapper the component (see {@link MapperBuilder}) that will perform the map portion of a MapReduce operation; may not
     *        be null
     * @return the reducible component for completing the MapReduce definition; never null
     * @see MapperBuilder
     */
    <Kout, Vout> TaskBuilder.Reducible<Kout, Vout> map( Mapper<Kout, Vout> mapper );

    /**
     * A component that defines the reduce portion of a MapReduce operation.
     * 
     * @param <Kout> the type of key output by the {@link Mapper mapping operation}
     * @param <Vout> the type of value output by the {@link Mapper mapping operation}
     * @author Randall Hauch (rhauch@redhat.com)
     * @see TaskBuilder#map(Mapper)
     */
    public static interface Reducible<Kout, Vout> {
        /**
         * Define the reduce portion of a MapReduce operation, and return a task that will perform the whole MapReduce operation.
         * 
         * @param reducer the component (see {@link ReducerBuilder}) that will perform the reduce portion of a MapReduce
         *        operation; may not be null
         * @return the task that executes the MapReduce operation; never null
         * @see ReducerBuilder
         */
        Task<Map<Kout, Vout>> reduce( Reducer<Kout, Vout> reducer );
    }
}
