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

package org.schematica.json.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.schematica.json.EditableJsonObject;
import org.schematica.json.JsonArray;
import org.schematica.json.JsonObject;
import org.schematica.json.JsonObjectBuilder;
import org.schematica.json.impl.util.Base64;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaObject extends AbstractMap<String, JsonValue> implements JsonObject {
    static final JsonObject EMPTY_INSTANCE = new SchematicaObject(Json.createObjectBuilder().build());

    private final javax.json.JsonObject defaultObject;

    protected SchematicaObject( javax.json.JsonObject defaultObject ) {
        this.defaultObject = defaultObject;
    }

    @Override
    public JsonObject getJsonObject( String name ) {
        javax.json.JsonObject jsonObject = defaultObject.getJsonObject(name);
        return jsonObject == null ? EMPTY_INSTANCE : new SchematicaObject(jsonObject);
    }

    @Override
    public Long getLong( String name ) {
        JsonNumber jsonNumber = getJsonNumber(name);
        return jsonNumber != null ? jsonNumber.longValue() : null;
    }

    @Override
    public long getLong( String name,
                         long defaultValue ) {
        return containsKey(name) ? getLong(name) : defaultValue;
    }

    @Override
    public Double getDouble( String name ) {
        JsonNumber jsonNumber = getJsonNumber(name);
        return jsonNumber != null ? jsonNumber.doubleValue() : null;
    }

    @Override
    public double getDouble( String name,
                             double defaultValue ) {
        return containsKey(name) ? getDouble(name) : defaultValue;
    }

    @Override
    public BigInteger getBigInteger( String name ) {
        return getJsonNumber(name).bigIntegerValue();
    }

    @Override
    public BigInteger getBigInteger( String name,
                                     BigInteger defaultValue ) {
        return containsKey(name) ? getBigInteger(name) : defaultValue;
    }

    @Override
    public BigDecimal getBigDecimal( String name ) {
        return getJsonNumber(name).bigDecimalValue();
    }

    @Override
    public BigDecimal getBigDecimal( String name,
                                     BigDecimal defaultValue ) {
        return containsKey(name) ? getBigDecimal(name) : defaultValue;
    }

    @Override
    public Date getDate( String name ) {
        Long ts = getLong(name);
        return ts != null ? new Date(ts) : null;
    }

    @Override
    public Date getDate( String name,
                         Date defaultValue ) {
        return containsKey(name) ? getDate(name) : defaultValue;
    }

    @Override
    public byte[] getBinary( String name ) {
        try {
            String string = getString(name);
            return string != null ? Base64.decode(string) : null;
        } catch (IOException e) {
            throw new JsonException("Cannot decode Base64 field " + name);
        }
    }

    @Override
    public JsonArray getJsonArray( String name ) {
        javax.json.JsonArray jsonArray = defaultObject.getJsonArray(name);
        return jsonArray != null ? new SchematicaArray(jsonArray) : SchematicaArray.EMPTY_INSTANCE;
    }

    @Override
    public JsonNumber getJsonNumber( String name ) {
        return defaultObject.getJsonNumber(name);
    }

    @Override
    public JsonString getJsonString( String name ) {
        return defaultObject.getJsonString(name);
    }

    @Override
    public String getString( String name ) {
        return defaultObject.getString(name);
    }

    @Override
    public String getString( String name,
                             String defaultValue ) {
        return defaultObject.getString(name, defaultValue);
    }

    @Override
    public int getInt( String name ) {
        return defaultObject.getInt(name);
    }

    @Override
    public int getInt( String name,
                       int defaultValue ) {
        return defaultObject.getInt(name, defaultValue);
    }

    @Override
    public boolean getBoolean( String name ) {
        return defaultObject.getBoolean(name);
    }

    @Override
    public boolean getBoolean( String name,
                               boolean defaultValue ) {
        return defaultObject.getBoolean(name, defaultValue);
    }

    @Override
    public boolean isNull( String name ) {
        return defaultObject.isNull(name);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return defaultObject.entrySet();
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof javax.json.JsonObject)) {
            return false;
        }

        javax.json.JsonObject that = (javax.json.JsonObject)o;
        return defaultObject.equals(that);
    }

    @Override
    public int hashCode() {
        return defaultObject.hashCode();
    }

    @Override
    public String toString() {
        return defaultObject.toString();
    }

    @Override
    public EditableJsonObject edit() {
        return new SchematicaEditableObject(this);
    }

    @Override
    public JsonObject merge( javax.json.JsonObject other ) {
        javax.json.JsonObject mergeSource = other;
        if (other instanceof SchematicaEditableObject && ((SchematicaEditableObject) other).jsonObject().equals(this)) {
            mergeSource = ((SchematicaEditableObject) other).changesToJson();
        }

        JsonObjectBuilder mergeObjectBuilder = org.schematica.json.Json.createObjectBuilder();
        //create a copy of the current object's data
        for (Map.Entry<String, JsonValue> entry : defaultObject.entrySet()) {
            mergeObjectBuilder.add(entry.getKey(), entry.getValue());
        }

        //merge with source
        for (Map.Entry<String, JsonValue> entry : mergeSource.entrySet()) {
            String fieldName = entry.getKey();
            JsonValue fieldValue = entry.getValue();

            JsonValue existingValue = defaultObject.get(fieldName);
            if (existingValue == null) {
                //there is no previous value, so just add it
                mergeObjectBuilder.add(fieldName, fieldValue);
            } else {
                ValueType incomingValueType = fieldValue.getValueType();
                if (incomingValueType.equals(ValueType.NULL)) {
                    mergeObjectBuilder.addNull(fieldName);
                    continue;
                }
                if (!existingValue.getValueType().equals(ValueType.NULL) && !existingValue.getValueType().equals(incomingValueType)) {
                    throw new IllegalArgumentException("The value type for the '" + fieldName + "' field cannot be changed from" +
                                                       "'" + existingValue + "' to '" + fieldValue + "'.");
                }
                switch (existingValue.getValueType()) {
                    case ARRAY: {
                        if (existingValue instanceof JsonArray && fieldValue instanceof javax.json.JsonArray) {
                            mergeObjectBuilder.add(fieldName, ((JsonArray)existingValue).merge((javax.json.JsonArray)fieldValue));
                        } else {
                            //we don't know the type of array
                            mergeObjectBuilder.add(fieldName, fieldValue);
                        }
                        break;
                    }
                    case OBJECT: {
                        if (existingValue instanceof JsonObject && fieldValue instanceof javax.json.JsonObject) {
                            mergeObjectBuilder.add(fieldName, ((JsonObject)existingValue).merge((javax.json.JsonObject)fieldValue));
                        } else {
                            //we don't know the type of object
                            mergeObjectBuilder.add(fieldName, fieldValue);
                        }
                        break;
                    }
                    default: {
                        //simple override/add the value
                        mergeObjectBuilder.add(fieldName, fieldValue);
                    }
                }
            }
        }
        return mergeObjectBuilder.build();
    }

}
