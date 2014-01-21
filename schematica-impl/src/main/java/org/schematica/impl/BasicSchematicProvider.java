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

package org.schematica.impl;

import java.util.Properties;
import javax.json.JsonObject;
import org.schematica.api.Document;
import org.schematica.api.DocumentEditor;
import org.schematica.api.PathBuilder;
import org.schematica.api.SchematicaException;
import org.schematica.api.Store;
import org.schematica.spi.SchematicaProvider;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class BasicSchematicProvider implements SchematicaProvider {

    private final PathBuilderImpl pathBuilder = new PathBuilderImpl();

    @Override
    public Store getStore( Properties properties ) throws SchematicaException {
        return null;
    }

    @Override
    public DocumentEditor document( String key,
                              JsonObject json ) {
        return new BasicDocumentEditor(key);
    }

    @Override
    public DocumentEditor document( String key ) {
        return new BasicDocumentEditor(key);
    }

    @Override
    public final PathBuilder getPathBuilder() {
        return pathBuilder;
    }
}
