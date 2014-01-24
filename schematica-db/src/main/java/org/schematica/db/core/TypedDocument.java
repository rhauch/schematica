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

import javax.json.JsonObject;
import org.schematica.db.Document;

/**
 * A {@link Document} implementation includes the unique key for this document's schema.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class TypedDocument extends SimpleDocument {

    private final String schemaKey;

    public TypedDocument( String key,
                          JsonObject json,
                          String schemaKey ) {
        super(key, json);
        assert schemaKey != null;
        this.schemaKey = schemaKey;
    }

    @Override
    public String getSchemaKey() {
        return schemaKey;
    }

    @Override
    public String toString() {
        return key + " (" + schemaKey + ") -> " + json;
    }
}
