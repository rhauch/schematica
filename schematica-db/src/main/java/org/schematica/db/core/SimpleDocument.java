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
 * A {@link Document} implementation includes just the unique key and object representation.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class SimpleDocument implements Document {
    protected final String key;
    protected final JsonObject json;

    public SimpleDocument( String key,
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
    public String getSchemaKey() {
        return null;
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
