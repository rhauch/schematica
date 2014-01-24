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

import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * An abstraction of a single document that has a unique key and an object representation of its content.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Document {

    /**
     * Get the unique key for this document.
     * 
     * @return the unique key; never null
     */
    String getKey();

    /**
     * Get the object representation of this document.
     * 
     * @return the object representation; never null
     */
    JsonObject getJsonObject();

    /**
     * Get the unique key for the document's schema;
     * 
     * @return the schema's key; never null
     */
    String getSchemaKey();

    /**
     * Obtain the value of the field at the given path.
     * 
     * @param path the path of the field; may not be null
     * @return the value; may not be null if the field value is null or if there is no field at the specified path
     */
    JsonValue valueAtPath( Path path );

}
