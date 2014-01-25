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

package org.schematica.db;

import java.util.Set;

/**
 * A report of which documents were updated or overwritten during a bulk write.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @see Store#writeMultiple(Iterable, boolean)
 */
public interface BulkWriteReport {

    /**
     * The kinds of action that can result from writing a single document.
     * 
     * @author Randall Hauch (rhauch@redhat.com)
     */
    public static enum Action {
        /** The document was updated */
        UPDATED,
        /** The document was overwritten */
        OVERWRITTEN,
        /** The document was not changed */
        NONE;
    }

    /**
     * Get whether the document with the supplied key was updated, overwritten, or not affected.
     * 
     * @param key the document key
     * @return the action; never null
     */
    Action getAction( String key );

    /**
     * Get whether the document with the supplied key was updated.
     * 
     * @param key the document key
     * @return true if the document was updated, or false otherwise
     */
    boolean isUpdated( String key );

    /**
     * Get whether the document with the supplied key was overwritten.
     * 
     * @param key the document key
     * @return true if the document was overwritten, or false otherwise
     */
    boolean isOverwritten( String key );

    /**
     * Get the set of keys for the documents that were updated.
     * 
     * @return the keys for the updated documents; never null but possibly empty
     */
    Set<String> updatedKeys();

    /**
     * Get the set of keys for the documents that were overwritten.
     * 
     * @return the keys for the overwritten documents; never null but possibly empty
     */
    Set<String> overwrittenKeys();

}
