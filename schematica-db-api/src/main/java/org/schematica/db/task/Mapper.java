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

import org.schematica.db.Document;

/**
 * An interface that defines the "map" phase of the MapReduce procedure. The {@link #map(Document, Collector)} method is called
 * once for each document, and the implementation should place in the {@link Collector} the output key/value pair(s).
 * <p>
 * A few {@link MapperBuilder standard mappers} are available and should be used if they do what an application expectes. However,
 * much of the time these built-in mappers will not be sufficient and applications will simply implement their own mappers.
 * </p>
 * 
 * @param <KeyType> the type of key
 * @param <ValueType> the type of value
 * @author Randall Hauch (rhauch@redhat.com)
 * @see MapperBuilder
 */
public interface Mapper<KeyType, ValueType> {

    /**
     * Map the supplied document into one or more output key/value pairs.
     * 
     * @param document the document that is to be processed; never null
     * @param collector the collector into which the implementation should register all output key/value pairs; never null
     */
    void map( Document document,
              Collector<KeyType, ValueType> collector );

    /**
     * A collector of output name/value pairs. The implementation of this interface is provided by Schematica and supplied to the
     * {@link Mapper#map(Document, Collector)} method.
     * 
     * @param <K> the type of the names
     * @param <V> the type of the values
     * @author Randall Hauch (rhauch@redhat.com)
     * @see Mapper#map(Document, Collector)
     */
    public interface Collector<K, V> {
        /**
         * Record a name/value pair.
         * 
         * @param key the name
         * @param value the value
         */
        void emit( K key,
                   V value );
    }

}
