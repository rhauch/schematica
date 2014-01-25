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
import org.schematica.db.Schematica;

/**
 * A factory or builder for several commonly-used {@link Mapper}s.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @see Schematica#mappers()
 */
public interface MapperBuilder {

    /**
     * Return a {@link Mapper} implementation that counts the number of documents. This mapper always uses the "count" output key.
     * 
     * @return the mapper; never null
     */
    Mapper<String, Long> count();

    /**
     * Return a {@link Mapper} implementation that accumulates the keys for all documents that are mapped. This mapper emits into
     * the collector a single name-value pair for each document, where the document's key is the name and {@link Boolean#TRUE} for
     * the value.
     * 
     * @return the mapper; never null
     */
    Mapper<String, Boolean> keys();

    /**
     * Return a {@link Mapper} implementation that accumulates the keys and the corresponding document for all documents that are
     * mapped. This mapper emits into the collector a single name-value pair for each document, where the document's key is the
     * name and the document as the value.
     * 
     * @return the mapper; never null
     */
    Mapper<String, Document> documents();
}
