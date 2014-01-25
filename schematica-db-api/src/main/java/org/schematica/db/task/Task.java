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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import org.schematica.db.Store;

/**
 * An executable task for processing documents within a {@link Store}. Tasks are potentially very long-running activities that may
 * require significant processing (e.g., running map-reduce over millions of documents) and resources that must be
 * {@link Results#close() released}. But sometimes tasks are also small enough that your application may want to simply run the
 * task and wait for its completion.
 * <p>
 * Tasks are designed to be executed directly (synchronously) within the same thread, or asynchronously via
 * {@link ExecutorService#submit(Callable) submission} to an {@link ExecutorService}.
 * </p>
 * <h3>Synchronous (direct) execution</h3>
 * <p>
 * To inoke immediately and synchronously from the calling thread:
 * 
 * <pre>
 *   Task<T> task = ...
 *   Results<T> results = null;
 *   try {
 *       results = task.call();  // runs the task in this thread
 *       T output = result.getOutput();
 *   } catch ( SchematicaException e ) {
 *       // deal with any problems ...
 *   } finally {
 *       if ( result != null ) result.close();
 *   }
 * </pre>
 * 
 * Now, this example shows how the {@link Results} object must always be closed to release any resources that it might be using.
 * (For example, the results may have been so large that they were buffered on the file system, and when you are finished these
 * temporary files must be cleaned up.)
 * </p>
 * <p>
 * Java 7 introduces the <a
 * href="http://docs.oracle.com/javase/7/docs/technotes/guides/language/try-with-resources.html">try-with-resources</a> statement
 * that will ensure that any resources created at the beginning of the {@code try} block will definitely be closed properly even
 * when exceptions occur. Because Schematica's {@link Results} interface implements {@link AutoCloseable}, we can write the code
 * in a slightly more compact fashion that is even more robust:
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
 * <h3>Asynchronous execution</h3>
 * <p>
 * Some tasks may take a long time to execute, so Schematica's Tasks are designed to be asynchronously executed using Java's
 * standard {@link ExecutorService}. After creating a Task, simply submit the task to your ExecutorService and obtain the
 * {@link Future}:
 * 
 * <pre>
 *   Task<T> task = ...
 *   ExecutorService executor = ...
 *   Future<Results<T>> future = executor.submit(task);     // submit for aynchronous execution
 * </pre>
 * 
 * Here the {@code submit} call does not block and immediately returns a {@link Future} that can be used to
 * {@link Future#cancel(boolean) cancel} the task, determine if the task has been {@link Future#isCancelled() cancelled}, and to
 * {@link Future#get() wait indefinitely} or for a {@link Future#get(long, java.util.concurrent.TimeUnit) maximum amount of time}
 * for the task to be completed.
 * </p>
 * <p>
 * For example, a different part of the code can obtain the {@link Future} instance and periodically check whether the work has
 * been completed:
 * 
 * <pre>
 * if (future.isDone()) {
 *     try (Results&lt;T&gt; results = future.get()) {
 *         T output = results.getOutput();
 *         // do something with the output
 *     } catch (ExecutionException e) {
 *         // The task completed but there was a probelm during task execution ...
 *     }
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * Alternatively, another thread might want to wait a certain amount of time for the task to complete:
 * 
 * <pre>
 * try (Results&lt;T&gt; results = future.get(10, TimeUnit.MINUTES)) {
 *     T output = results.getOutput();
 *     // do something with the output
 * } catch (TimeoutException e) {
 *     // The work wasn't done within our limit ...
 * } catch (InterruptedException e) {
 *     // The task was probably cancelled or stopped ...
 * } catch (ExecutionException e) {
 *     // The task completed but there was a probelm during task execution ...
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * Again, because {@link Results} is {@link AutoCloseable auto-closeable}, Java will guarantee that it is {@link Results#close()
 * closed} in the {@code finally} portion of the {@code try} blocks.
 * </p>
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @param <T> the type of result that should be returned when this task is completed.
 */
public interface Task<T> extends Callable<Results<T>> {

}
