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

package org.schematica.db.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.schematica.db.Document;

/**
 * An abstraction of a component that reads and writes a Document.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface DocumentFormat extends Serializable {

    /**
     * Parse the supplied input stream to obtain a Document instance.
     * 
     * @param key the unique document key; may not be null
     * @param stream the stream containing the document's content; may not be null
     * @param schemaKey the unique key for the document's schema; may be null
     * @return the document
     * @throws IOException if there is a problem reading the stream
     */
    Document parse( String key,
                    InputStream stream,
                    String schemaKey ) throws IOException;

    /**
     * Write the supplied document to a stream form.
     * 
     * @param document the document; may not be null
     * @return the stream
     * @throws IOException if there is a problem reading the stream
     */
    InputStream write( Document document ) throws IOException;

    int getType();

    String getName();
}
