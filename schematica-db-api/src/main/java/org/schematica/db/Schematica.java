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

import java.util.Properties;
import java.util.ServiceLoader;
import javax.json.JsonObject;
import org.schematica.db.spi.SchematicaProvider;
import org.schematica.db.task.Filter;
import org.schematica.db.task.FilterBuilder;
import org.schematica.db.task.Mapper;
import org.schematica.db.task.MapperBuilder;
import org.schematica.db.task.Reducer;
import org.schematica.db.task.ReducerBuilder;

/**
 * The factory class for obtaining Schematica {@link Store} instances and simple {@link Document} objects.
 * <p>
 * The methods in this class locate a {@link SchematicaProvider provider} instance using the {@link ServiceLoader} framework. The
 * first instance found is used; if none is found, then this class attempts to instantiate the default implementation.
 * </p>
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Schematica {

    private static final String DEFAULT_PROVIDER = "org.schematic.impl.BasicSchematicProvider";

    private static SchematicaProvider loadProvider() {
        // Find the first implementation found using the ServiceLoader ...
        for (SchematicaProvider provider : ServiceLoader.load(SchematicaProvider.class)) {
            if (provider != null) return provider;
        }
        // Look for the default provider ...
        try {
            return (SchematicaProvider)Class.forName(DEFAULT_PROVIDER).newInstance();
        } catch (ClassNotFoundException e) {
            throw new SchematicaException("Schematicaprovider " + DEFAULT_PROVIDER + " not found", e);
        } catch (Exception e) {
            throw new SchematicaException("Schematicaprovider " + DEFAULT_PROVIDER + " could not be instantiated: " + e, e);
        }
    }

    private final static SchematicaProvider PROVIDER = loadProvider();

    /**
     * Get a {@link Store} instance that supports the provided configuration properties.
     * 
     * @param properties the configuration properties for the store; may be null or empty
     * @return the store; never null
     * @throws SchematicaException if there is a problem or error with the supplied properties
     */
    public static Store getStore( Properties properties ) throws SchematicaException {
        return PROVIDER.getStore(properties);
    }

    /**
     * Get a builder that can be used to obtain common built-in {@link Filter} instances as well as combine both built-in and
     * custom {@link Filter} instances.
     * 
     * @return the filter builder; never null
     */
    public static FilterBuilder filters() {
        return PROVIDER.getFilterBuilder();
    }

    /**
     * Get a builder for several common built-in {@link Mapper} instances.
     * 
     * @return the mapper builder; never null
     */
    public static MapperBuilder mappers() {
        return PROVIDER.getMapperBuilder();
    }

    /**
     * Get a builder for several common built-in {@link Reducer} instances.
     * 
     * @return the reduer builder; never null
     */
    public static ReducerBuilder reducers() {
        return PROVIDER.getReducerBuilder();
    }

    /**
     * Get a builder for paths.
     * 
     * @return the path builder; never null
     */
    public static PathBuilder pathBuilder() {
        return PROVIDER.getPathBuilder();
    }

    /**
     * Create a document with the supplied key and document.
     * 
     * @param key the document key; may not be null
     * @param json the object representation; may not be null
     * @return the document instance; never null
     */
    public static Document document( String key,
                                     JsonObject json ) {
        return PROVIDER.document(key, json, null);
    }

    /**
     * Create a document with the supplied key and document.
     * 
     * @param key the document key; may not be null
     * @param json the object representation; may not be null
     * @param schemaKey the unique key of the schema that describes this document; may be null if there is no schema
     * @return the document instance; never null
     */
    public static Document document( String key,
                                     JsonObject json,
                                     String schemaKey ) {
        return PROVIDER.document(key, json, schemaKey);
    }

}
