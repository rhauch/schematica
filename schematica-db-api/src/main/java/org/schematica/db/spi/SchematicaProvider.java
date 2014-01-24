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

package org.schematica.db.spi;

import java.util.Properties;
import javax.json.JsonObject;
import org.schematica.db.Document;
import org.schematica.db.PathBuilder;
import org.schematica.db.SchematicaException;
import org.schematica.db.Store;
import org.schematica.db.task.FilterBuilder;
import org.schematica.db.task.MapperBuilder;
import org.schematica.db.task.ReducerBuilder;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface SchematicaProvider {

    /**
     * Get a {@link Store} instance that supports the provided configuration properties.
     * 
     * @param properties the configuration properties for the store; may be null or empty
     * @return the store; never null
     * @throws SchematicaException if there is a problem or error with the supplied properties
     */
    Store getStore( Properties properties ) throws SchematicaException;

    /**
     * Create a document with the supplied key and document.
     * 
     * @param key the document key; may not be null
     * @param json the object representation; may not be null
     * @param schemaKey the unique key of the schema that describes this document; may be null if there is no schema
     * @return the document instance; never null
     */
    Document document( String key,
                       JsonObject json,
                       String schemaKey );

    /**
     * Get the {@link FilterBuilder} instance for this provider.
     * 
     * @return the builder; never null
     */
    FilterBuilder getFilterBuilder();

    /**
     * Get the {@link MapperBuilder} instance for this provider.
     * 
     * @return the builder; never null
     */
    MapperBuilder getMapperBuilder();

    /**
     * Get the {@link ReducerBuilder} instance for this provider.
     * 
     * @return the builder; never null
     */
    ReducerBuilder getReducerBuilder();

    /**
     * Get the {@link PathBuilder} instance for this provider.
     * 
     * @return the builder; never null
     */
    PathBuilder getPathBuilder();
}
