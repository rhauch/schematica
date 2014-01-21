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

package org.schematica.api;

import java.util.Properties;
import java.util.ServiceLoader;
import javax.json.JsonObject;
import org.schematica.api.task.FilterBuilder;
import org.schematica.api.task.MapperBuilder;
import org.schematica.api.task.ReducerBuilder;
import org.schematica.spi.SchematicaProvider;

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

    public static FilterBuilder filters() {
        return PROVIDER.getFilterBuilder();
    }

    public static MapperBuilder mappers() {
        return PROVIDER.getMapperBuilder();
    }

    public static ReducerBuilder reducers() {
        return PROVIDER.getReducerBuilder();
    }

    public static PathBuilder pathBuilder() {
        return PROVIDER.getPathBuilder();
    }

    public static Document document( String key,
                                     JsonObject json ) {
        return PROVIDER.document(key, json);
    }
}
