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

package org.schematica.api.task;

/**
 * A factory or builder for several commonly-used {@link Reducer}s.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface ReducerBuilder {

    /**
     * Get the component that creates Integer-specific {@link Reducer}s.
     * 
     * @return the factory for Integer-specific reducers; never null
     */
    NumericReducer<Integer> integers();

    /**
     * Get the component that creates Double-specific {@link Reducer}s.
     * 
     * @return the factory for Double-specific reducers; never null
     */
    NumericReducer<Double> doubles();

    /**
     * Get the component that creates Long-specific {@link Reducer}s.
     * 
     * @return the factory for Long-specific reducers; never null
     */
    NumericReducer<Long> longs();

    static interface NumericReducer<T extends Number> {
        /**
         * Create a reducer that simply computes the sum of all supplied values, regardless of the key.
         * 
         * @return the summing reducer; never null
         */
        Reducer<String, T> sum();

        /**
         * Create a reducer that simply computes the maximum of all supplied values, regardless of the key.
         * 
         * @return the maximizing reducer; never null
         */
        Reducer<String, T> maximum();

        /**
         * Create a reducer that simply computes the minimum of all supplied values, regardless of the key.
         * 
         * @return the minimizing reducer; never null
         */
        Reducer<String, T> minimum();
    }

}
