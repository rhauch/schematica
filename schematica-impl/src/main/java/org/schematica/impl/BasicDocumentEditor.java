/*
 * Schematica (http://www.schematica.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.schematica.impl;

import java.util.Collection;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.joda.time.DateTime;
import org.schematica.api.ChangeSet;
import org.schematica.api.Document;
import org.schematica.api.DocumentEditor;
import org.schematica.api.Field;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class BasicDocumentEditor implements DocumentEditor {

    private final JsonObjectBuilder jsonObjectBuilder;
    private final ChangeSet changeSet;
    private final String key;

    protected BasicDocumentEditor(String key) {
        this.changeSet = new BasicChangeSet();
        this.jsonObjectBuilder = Json.createObjectBuilder();
        this.key = key;
    }

    protected BasicDocumentEditor( String key, JsonObject object ) {
        this.key = key;
        this.changeSet = new BasicChangeSet();
        this.jsonObjectBuilder = Json.createObjectBuilder();
        for (String fieldName : object.keySet()) {
            this.jsonObjectBuilder.add(fieldName, object.get(fieldName));
        }
    }

    @Override
    public DocumentEditor setBoolean( String name,
                                      boolean value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setInt( String name,
                                  int value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setLong( String name,
                                   long value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setFloat( String name,
                                    float value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setDouble( String name,
                                     double value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setString( String name,
                                     String value ) {
        jsonObjectBuilder.add(name, value);
        changeSet.recordChange(name, value);
        return this;
    }

    @Override
    public DocumentEditor setDate( String name,
                                   Date value ) {
        String dateIsoString = new DateTime(value.getTime()).toString();
        jsonObjectBuilder.add(name, dateIsoString);
        changeSet.recordChange(name, dateIsoString);
        return this;
    }

    @Override
    public DocumentEditor setDocument( String name,
                                       Document document ) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Field field : document) {
            fieldToJson(field, builder);
        }
        jsonObjectBuilder.add(name, builder);
        changeSet.recordChange(name, builder.build());
        return this;
    }

    @Override
    public <T> DocumentEditor setArray( String name,
                                        T... values ) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (T value : values) {
            addValueToArray(value, arrayBuilder);
        }
        jsonObjectBuilder.add(name, arrayBuilder);
        changeSet.recordChange(name, arrayBuilder.build());
        return this;
    }

    @Override
    public Document unwrap() {
        return new BasicDocument(key, jsonObjectBuilder.build());
    }

    @Override
    public ChangeSet changeSet() {
        return changeSet;
    }

    private static void fieldToJson( final Field field,
                                     final JsonObjectBuilder builder ) {
        final String fieldName = field.getName();
        final Object value = field.getValue();
        convertValueToJson(value, new ValueConverter() {
            @Override
            public void withBoolean( Boolean value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withInteger( Integer value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withLong( Long value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withFloat( Float value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withDouble( Double value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withString( String value ) {
                builder.add(fieldName, value);
            }

            @Override
            public void withObject( JsonObjectBuilder object ) {
                builder.add(fieldName, object);
            }

            @Override
            public void withArray( JsonArrayBuilder array ) {
                builder.add(fieldName, array);
            }
        });
    }

    private static void addValueToArray( final Object value,
                                         final JsonArrayBuilder builder ) {
        convertValueToJson(value, new ValueConverter() {
            @Override
            public void withBoolean( Boolean value ) {
                builder.add(value);
            }

            @Override
            public void withInteger( Integer value ) {
                builder.add(value);
            }

            @Override
            public void withLong( Long value ) {
                builder.add(value);
            }

            @Override
            public void withFloat( Float value ) {
                builder.add(value);
            }

            @Override
            public void withDouble( Double value ) {
                builder.add(value);
            }

            @Override
            public void withString( String value ) {
                builder.add(value);
            }

            @Override
            public void withObject( JsonObjectBuilder object ) {
                builder.add(object);
            }

            @Override
            public void withArray( JsonArrayBuilder array ) {
                builder.add(array);
            }
        });
    }

    private static void convertValueToJson( Object value,
                                     ValueConverter converter ) {
        if (value instanceof Integer) {
            converter.withInteger((Integer) value);
        } else if (value instanceof Long) {
            converter.withLong((Long)value);
        } else if (value instanceof Double) {
            converter.withDouble((Double) value);
        } else if (value instanceof Float) {
            converter.withFloat((Float)value);
        }  else if (value instanceof Date) {
            converter.withString(new DateTime(((Date) value).getTime()).toString());
        } else if (value instanceof String) {
            converter.withString((String)value);
        } else if (value instanceof Boolean) {
            converter.withBoolean((Boolean)value);
        } else if (value instanceof Collection<?>) {
            Object[] values = ((Collection<?>) value).toArray();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object innerValue : values) {
                addValueToArray(innerValue, arrayBuilder);
            }
            converter.withArray(arrayBuilder);
        } else if (value instanceof Object[]) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Object innerValue : (Object[]) value) {
                addValueToArray(innerValue, arrayBuilder);
            }
            converter.withArray(arrayBuilder);
        } else if (value instanceof Document) {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            for (Field field : (Document) value) {
                fieldToJson(field, builder);
            }
            converter.withObject(builder);
        }
    }
}
