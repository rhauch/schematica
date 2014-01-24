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

import javax.json.JsonObject;
import org.schematica.db.task.Filter;
import org.schematica.db.task.Task;
import org.schematica.db.task.TaskMaker;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Store {

    /**
     * Get the number of documents in this store.
     * 
     * @return the number of documents currently in the store
     */
    long size();

    /**
     * Determine whehter the store contains a document with the given key.
     * 
     * @param key the unique document key; may not be null
     * @return true if the document contains a document with the key, or false otherwise
     */
    boolean has( String key );

    /**
     * Get the document with the specified unique key.
     * 
     * @param key the unique document key; may not be null
     * @return the document instance, or null if there is no document with the supplied key
     * @see #readMultiple(Iterable)
     */
    Document read( String key );

    /**
     * Store the document with the specified unique key, overwriting or updating any document that is already stored.
     * 
     * @param key the unique document key; may not be null
     * @param document the object representation of the document; may not be null
     * @return true if an existing document was overwritten or updated, or false if the document was inserted for the first time
     * @see #write(Document)
     * @see #writeIfAbsent(Document)
     * @see #writeIfAbsent(String, JsonObject)
     * @see #writeMultiple(Iterable)
     * @see #writeMultiple(Iterable, DocumentUpdates)
     */
    boolean write( String key,
                   JsonObject document );

    /**
     * Store the document with the specified unique key, overwriting or updating any document that is already stored.
     * 
     * @param document the document; may not be null
     * @return true if an existing document was overwritten or updated, or false if the document was inserted for the first time
     * @see #write(String, JsonObject)
     * @see #writeIfAbsent(Document)
     * @see #writeIfAbsent(String, JsonObject)
     * @see #writeMultiple(Iterable)
     * @see #writeMultiple(Iterable, DocumentUpdates)
     */
    boolean write( Document document );

    /**
     * Store the document with the specified unique key only if a document with the unique key has not already been stored.
     * 
     * @param key the unique document key; may not be null
     * @param document the object representation of the document; may not be null
     * @return true if the document was stored, or false if an existing document with the unique key already exists
     * @see #write(Document)
     * @see #write(String, JsonObject)
     * @see #writeIfAbsent(Document)
     * @see #writeMultiple(Iterable)
     * @see #writeMultiple(Iterable, DocumentUpdates)
     */
    boolean writeIfAbsent( String key,
                           JsonObject document );

    /**
     * Store the document with the specified unique key only if a document with the unique key has not already been stored.
     * 
     * @param document the document; may not be null
     * @return true if the document was stored, or false if an existing document with the unique key already exists
     * @see #write(Document)
     * @see #write(String, JsonObject)
     * @see #writeIfAbsent(String, JsonObject)
     * @see #writeMultiple(Iterable)
     * @see #writeMultiple(Iterable, DocumentUpdates)
     */
    boolean writeIfAbsent( Document document );

    /**
     * Merge the fields in the supplied document into the existing document with the same key. This will add/set on the persisted
     * document each of the fields in the supplied document; nested documents in the supplied document will be merged recursively.
     * If there is no persisted document with the given key, then this method is equivalent to calling
     * {@link #write(String, JsonObject)}.
     * <p>
     * Consider the following example. If the persisted document contains:
     * 
     * <pre>
     * {
     *   "firstName" : "Jane",
     *   "lastName" : "Smith",
     *   "address" : {
     *     "street" : "Main Street",
     *     "city" : "Springfield"
     *   },
     *   "phone" : "(800)555-1212"
     * }
     * </pre>
     * 
     * and the supplied document contains:
     * 
     * <pre>
     * {
     *   "lastName" : "Doe",
     *   "address" : {
     *     "city" : "Memphis",
     *     "zip" : 12345
     *   },
     *   "phone" : {
     *     "home" : "(800)555-1212"
     *   }
     * }
     * </pre>
     * 
     * then the result of merging will be the following persisted document:
     * 
     * <pre>
     * {
     *   "firstName" : "Jane",
     *   "lastName" : "Doe",
     *   "address" : {
     *     "street" : "Main Street",
     *     "city" : "Memphis",
     *     "zip" : 12345
     *   },
     *   "phone" : {
     *     "home" : "(800)555-1212"
     *   }
     * }
     * </pre>
     * 
     * @param key the unique document key; may not be null
     * @param document the object representation of the document to be merged; may not be null
     */
    void merge( String key,
                JsonObject document );

    /**
     * Remove the document with the supplied key. This method does nothing if a persisted document with the given key does not
     * exist.
     * 
     * @param key the unique document key; may not be null
     */
    void remove( String key );

    /**
     * Get the document with the specified unique key.
     * 
     * @param keys the unique document keys; may not be null
     * @return the document instance, or null if there is no document with the supplied key
     * @see #read(String)
     */
    Sequence<Document> readMultiple( Iterable<String> keys );

    /**
     * Store the documents, overwriting or updating any documents already stored under the same keys.
     * 
     * @param documents the documents; may not be null
     * @see #writeMultiple(Iterable, DocumentUpdates)
     * @see #write(Document)
     * @see #write(String, JsonObject)
     * @see #writeIfAbsent(Document)
     * @see #writeIfAbsent(String, JsonObject)
     */
    void writeMultiple( Iterable<Document> documents );

    /**
     * Store the documents, overwriting or updating any documents already stored under the same keys.
     * 
     * @param documents the documents; may not be null
     * @param updates the component that should be called based upon which documents were updated vs overwritten; may be null if
     *        this information is not needed
     * @see #writeMultiple(Iterable)
     * @see #write(Document)
     * @see #write(String, JsonObject)
     * @see #writeIfAbsent(Document)
     * @see #writeIfAbsent(String, JsonObject)
     */
    void writeMultiple( Iterable<Document> documents,
                        DocumentUpdates updates );

    /**
     * Remove the documents with the supplied keys. This method does nothing if a persisted document with the given key does not
     * exist.
     * 
     * @param keys the unique keys for the documents to be removed; may not be null
     */
    void remove( Iterable<String> keys );

    /**
     * Begin to create a {@link Task} that will find and process in some way all of the persisted documents that satisfy the
     * supplied filter. What processing will be done is dependent upon how the returned {@link TaskMaker} instance is used and the
     * resulting {@link Task} is defined.
     * 
     * @param filter the document filter that must be satisfied; may not be null
     * @return the {@link TaskMaker} that should be used to construct the callable {@link Task}; never null
     * @see #all()
     */
    TaskMaker filter( Filter filter );

    /**
     * Begin to create a {@link Task} that will find and process in some way all of the persisted documents. What processing will
     * be done is dependent upon how the returned {@link TaskMaker} instance is used and the resulting {@link Task} is defined.
     * 
     * @return the {@link TaskMaker} that should be used to construct the callable {@link Task}; never null
     * @see #filter(Filter)
     */
    TaskMaker all();

    Schemas getSchemas();

    Observation getObservation();

    /**
     * Signal that this store instance is not needed anymore and that any resources it currently utilizes may be released.
     */
    void close();
}
