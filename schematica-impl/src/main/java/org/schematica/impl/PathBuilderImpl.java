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

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import org.schematica.api.Path;
import org.schematica.api.PathBuilder;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class PathBuilderImpl implements PathBuilder {

    PathBuilderImpl() {
    }

    @Override
    public Path parsePath( String path ) {
        return null;
    }

    @Override
    public Path pathWith( Path parent,
                          String subPath ) {
        return null;
    }

    @Override
    public Path pathWith( Path parent,
                          String... segments ) {
        return null;
    }

    @Override
    public Path pathWith( String... segments ) {
        return null;
    }

    @Override
    public JsonValue valueAtPath( JsonObject object,
                                  Path path ) {
        JsonStructure obj = object;
        JsonValue value = null;
        for (String segment : path) {
            if (obj instanceof JsonObject) {
                value = ((JsonObject)obj).get(segment);
            } else {
                try {
                    int index = Integer.parseInt(segment);
                    value = ((JsonArray)obj).get(index);
                } catch (NumberFormatException e) {
                    // the segment is not an index ...
                    return null;
                }
            }
            if (value == null) break;
        }
        return value;
    }
}
