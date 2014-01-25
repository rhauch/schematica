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

import java.util.Iterator;

/**
 * An interface that defines the "reduce" phase of the MapReduce procedure. The reduce phase is called at least once for a set of
 * output values and a corresponding output key.
 * <p>
 * A few {@link ReducerBuilder standard reducers} are available and should be used if they do what an application expectes.
 * However, much of the time these built-in reducers will not be sufficient and applications will simply implement their own
 * reducers.
 * </p>
 * 
 * @param <KeyType> the type of key output by the {@link Mapper}
 * @param <ValueType> the type of value output by the {@link Mapper}
 * @author Randall Hauch (rhauch@redhat.com)
 * @see ReducerBuilder
 */
public interface Reducer<KeyType, ValueType> {
    /**
     * Reduce the supplied values for the given key into a single value.
     * 
     * @param reducedKey the key output by the {@link Mapper#map map phase}
     * @param values the values output during the {@link Mapper#map map phase}
     * @return the single reduced value
     */
    ValueType reduce( KeyType reducedKey,
                      Iterator<ValueType> values );
}
