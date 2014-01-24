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

package org.schematica.db.jdbc;

import java.util.Properties;
import javax.json.JsonObject;
import org.schematica.db.Document;
import org.schematica.db.PathBuilder;
import org.schematica.db.SchematicaException;
import org.schematica.db.Store;
import org.schematica.db.core.Paths;
import org.schematica.db.core.SimpleDocument;
import org.schematica.db.spi.SchematicaProvider;
import org.schematica.db.task.FilterBuilder;
import org.schematica.db.task.MapperBuilder;
import org.schematica.db.task.ReducerBuilder;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class JdbcSchematicaProvider implements SchematicaProvider {

    @Override
    public Store getStore( Properties properties ) throws SchematicaException {
        return null;
    }

    @Override
    public Document document( String key,
                              JsonObject json ) {
        return new SimpleDocument(key, json);
    }

    @Override
    public final PathBuilder getPathBuilder() {
        return Paths.INSTANCE;
    }

    @Override
    public FilterBuilder getFilterBuilder() {
        return null;
    }

    @Override
    public MapperBuilder getMapperBuilder() {
        return null;
    }

    @Override
    public ReducerBuilder getReducerBuilder() {
        return null;
    }
}
