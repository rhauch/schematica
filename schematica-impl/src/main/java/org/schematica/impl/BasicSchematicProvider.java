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
    public Document document( String key,
                              JsonObject json ) {
        return new BasicDocument(key, json);
    }

    @Override
    public final PathBuilder getPathBuilder() {
        return pathBuilder;
    }

    static class BasicDocument implements Document {
        private final String key;
        private final JsonObject json;

        BasicDocument( String key,
                       JsonObject json ) {
            this.key = key;
            this.json = json;
        }

        @Override
        public JsonObject getJsonObject() {
            return json;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Document) {
                Document that = (Document)obj;
                return this.key.equals(that.getKey());
            }
            return false;
        }

        @Override
        public String toString() {
            return key + " -> " + json;
        }
    }

}
