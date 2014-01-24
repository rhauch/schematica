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

import org.schematica.db.Store;

/**
 * The results for a task for processing documents within a {@link Store}.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @param <T> the type of result that should be returned when this task is completed.
 */
public interface Results<T> extends AutoCloseable {

    /**
     * Release all resources that are or have been in-use.
     */
    @Override
    void close();

    /**
     * Get the output of the task.
     * 
     * @return the output
     */
    T output();

}
