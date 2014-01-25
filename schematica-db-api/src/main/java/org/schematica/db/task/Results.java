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
 * The results for a task for processing documents within a {@link Store}, which encapsulate all of the output of the processing.
 * Results are typically returned from a {@link Task}'s {@link Task#call()} method; see {@link Task} for more details.
 * <p>
 * The size of the output will often require some form of temporary storage or buffering in external resources, so as soon as
 * finished using the {@link #output()} the {@link Results} instance should be {@link #close() closed} to release these resources.
 * Results is {@link AutoCloseable auto-closeable}, which means that consuming code can take advantage of Java's <a
 * href="http://docs.oracle.com/javase/7/docs/technotes/guides/language/try-with-resources.html">try-with-resources</a> statement,
 * which will ensure that all resources created at the beginning of the {@code try} block will definitely be closed properly even
 * when exceptions occur.
 * </p>
 * <p>
 * For example, the following code fragment shows how Results from a {@link Task} can be used and automatically closed:
 * 
 * <pre>
 *   Task<T> task = ...
 *   try ( Results<T> results = task.call() ) {  // runs the task, closes the results in a finally
 *       T output = results.getOutput();
 *       // do something with the output
 *   } catch ( SchematicaException e ) {
 *       // deal with any problems ...
 *   }
 * </pre>
 * 
 * </p>
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @param <T> the type of output that is encapsulated by this Results object
 */
public interface Results<T> extends AutoCloseable {

    /**
     * Get the output of the task. This method and all processing of the output must be done before this Results is
     * {@link #close() closed}.
     * 
     * @return the output
     */
    T output();

    /**
     * Release all resources that are or have been in-use.
     */
    @Override
    void close();

}
