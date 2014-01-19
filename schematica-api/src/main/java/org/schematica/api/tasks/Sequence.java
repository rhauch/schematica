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

package org.schematica.api.tasks;


/**
 * A simple ordered collection of objects that contains the size information.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @param <T> the type
 */
public interface Sequence<T> extends Iterable<T> {
    /**
     * Return the number of documents that appears in this collection.
     * 
     * @return the number of documents; never negative but possibly 0
     */
    int size();

    /**
     * Return whether this collection of documents is empty. This is sometimes more efficient than checking if {@code size() == 0}
     * .
     * 
     * @return true if there are no documents, or false otherwise
     */
    boolean isEmpty();
}
