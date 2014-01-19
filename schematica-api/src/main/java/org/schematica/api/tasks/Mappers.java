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

package org.schematica.api.tasks;

import javax.json.JsonNumber;
import javax.json.JsonValue;
import org.schematica.api.Document;
import org.schematica.api.Path;
import org.schematica.api.Schematica;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Mappers {

    public static Mapper<String, Long> count() {
        return new Mapper<String, Long>() {
            @Override
            public void map( Document document,
                             Collector<String, Long> collector ) {
                collector.emit("count", 1L);
            }
        };
    }

    public static Mapper<String, Boolean> keys() {
        return new Mapper<String, Boolean>() {
            @Override
            public void map( Document document,
                             Collector<String, Boolean> collector ) {
                collector.emit(document.getKey(), Boolean.TRUE);
            }
        };
    }

    public static Mapper<String, Document> documents() {
        return new Mapper<String, Document>() {
            @Override
            public void map( Document document,
                             Collector<String, Document> collector ) {
                collector.emit(document.getKey(), document);
            }
        };
    }

    public static Mapper<String, Integer> intValue( final String path ) {
        final Path pathObj = Schematica.pathBuilder().parsePath(path);
        return new Mapper<String, Integer>() {
            @Override
            public void map( Document document,
                             Collector<String, Integer> collector ) {
                JsonValue value = Schematica.pathBuilder().valueAtPath(document.getJsonObject(), pathObj);
                if (value != null && value instanceof JsonNumber) {
                    try {
                        collector.emit(path, ((JsonNumber)value).intValueExact());
                    } catch (ArithmeticException e) {
                        throw e;
                    }
                }
            }
        };
    }

    public static Mapper<String, Long> longValue( final String path ) {
        final Path pathObj = Schematica.pathBuilder().parsePath(path);
        return new Mapper<String, Long>() {
            @Override
            public void map( Document document,
                             Collector<String, Long> collector ) {
                JsonValue value = Schematica.pathBuilder().valueAtPath(document.getJsonObject(), pathObj);
                if (value != null && value instanceof JsonNumber) {
                    collector.emit(path, ((JsonNumber)value).longValue());
                }
            }
        };
    }

    public static Mapper<String, Double> doubleValue( final String path ) {
        final Path pathObj = Schematica.pathBuilder().parsePath(path);
        return new Mapper<String, Double>() {
            @Override
            public void map( Document document,
                             Collector<String, Double> collector ) {
                JsonValue value = Schematica.pathBuilder().valueAtPath(document.getJsonObject(), pathObj);
                if (value != null && value instanceof JsonNumber) {

                    collector.emit(path, ((JsonNumber)value).doubleValue());
                }
            }
        };
    }

    private Mappers() {
    }

}
