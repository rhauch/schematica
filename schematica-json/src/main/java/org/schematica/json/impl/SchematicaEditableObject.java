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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.schematica.json.EditableJsonObject;
import org.schematica.json.Json;
import org.schematica.json.JsonArray;
import org.schematica.json.JsonObject;
import org.schematica.json.impl.util.JsonValueConverter;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 * @NotThreadSafe
 */
public class SchematicaEditableObject extends AbstractMap<String, JsonValue> implements EditableJsonObject {

    private final JsonObject jsonObject;
    private final Map<String, Object> changes;

    protected SchematicaEditableObject( JsonObject jsonObject ) {
        this.jsonObject = jsonObject;
        //order is important
        this.changes = new LinkedHashMap<>();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        Map<String, JsonValue> allEntries = new LinkedHashMap<>(jsonObject);
        for (String fieldName : changes.keySet()) {
            allEntries.put(fieldName, JsonValueConverter.jsonValueFrom(changes.get(fieldName)));
        }
        return Collections.unmodifiableSet(allEntries.entrySet());
    }

    @Override
    public EditableJsonObject add( String name, JsonValue value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, String value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, BigInteger value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, BigDecimal value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, int value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, long value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, double value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, boolean value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject addNull( String name ) {
        changes.put(name, JsonValue.NULL);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, JsonObjectBuilder builder ) {
        //this will flush the builder
        changes.put(name, builder.build());
        return this;
    }

    @Override
    public EditableJsonObject add( String name, JsonArrayBuilder builder ) {
        //this will flush the builder
        changes.put(name, builder.build());
        return this;
    }

    @Override
    public EditableJsonObject add( String name, Date value ) {
        changes.put(name, value);
        return this;
    }

    @Override
    public EditableJsonObject add( String name, byte[] data ) {
        changes.put(name, data);
        return this;
    }

    @Override
    public JsonArray getJsonArray( String name ) {
        JsonArray result = (JsonArray) changes.get(name);
        return result != null ? result : jsonObject.getJsonArray(name);
    }

    @Override
    public JsonObject getJsonObject( String name ) {
        JsonObject result = (JsonObject) changes.get(name);
        return result != null ? result : jsonObject.getJsonObject(name);
    }

    @Override
    public Long getLong( String name ) {
        Long result = (Long) changes.get(name);
        return result != null ? result : jsonObject.getLong(name);
    }

    @Override
    public long getLong( String name, long defaultValue ) {
        Long result = (Long) changes.get(name);
        return result != null ? result : jsonObject.getLong(name, defaultValue);
    }

    @Override
    public Double getDouble( String name ) {
        Double result = (Double) changes.get(name);
        return result != null ? result : jsonObject.getDouble(name);
    }

    @Override
    public double getDouble( String name, double defaultValue ) {
        Double result = (Double) changes.get(name);
        return result != null ? result : jsonObject.getDouble(name, defaultValue);
    }

    @Override
    public BigInteger getBigInteger( String name ) {
        BigInteger result = (BigInteger) changes.get(name);
        return result != null ? result : jsonObject.getBigInteger(name);
    }

    @Override
    public BigInteger getBigInteger( String name, BigInteger defaultValue ) {
        BigInteger result = (BigInteger) changes.get(name);
        return result != null ? result : jsonObject.getBigInteger(name, defaultValue);
    }

    @Override
    public BigDecimal getBigDecimal( String name ) {
        BigDecimal result = (BigDecimal) changes.get(name);
        return result != null ? result : jsonObject.getBigDecimal(name);
    }

    @Override
    public BigDecimal getBigDecimal( String name, BigDecimal defaultValue ) {
        BigDecimal result = (BigDecimal) changes.get(name);
        return result != null ? result : jsonObject.getBigDecimal(name, defaultValue);
    }

    @Override
    public Date getDate( String name ) {
        Date result = (Date) changes.get(name);
        return result != null ? result : jsonObject.getDate(name);
    }

    @Override
    public Date getDate( String name, Date defaultValue ) {
        Date result = (Date) changes.get(name);
        return result != null ? result : jsonObject.getDate(name, defaultValue);
    }

    @Override
    public byte[] getBinary( String name ) {
        byte[] result = (byte[])changes.get(name);
        return result != null ? result : jsonObject.getBinary(name);
    }

    @Override
    public EditableJsonObject edit() {
        return this;
    }

    @Override
    public JsonObject merge( javax.json.JsonObject other ) {
        //first merge the local changes and then the incoming changes
        return jsonObject.merge(changesToJson()).merge(other);
    }

    protected JsonObject changesToJson() {
        org.schematica.json.JsonObjectBuilder localChanges = Json.createObjectBuilder();
        //first merge the local changes into an object
        for (Entry<String, Object> localChange : changes.entrySet()) {
            String field = localChange.getKey();
            JsonValue value = JsonValueConverter.jsonValueFrom(localChange.getValue());
            localChanges.add(field, value);
        }
        return localChanges.build();
    }

    @Override
    public JsonNumber getJsonNumber( String name ) {
        JsonNumber result = (JsonNumber) changes.get(name);
        return result != null ? result : jsonObject.getJsonNumber(name);
    }

    @Override
    public JsonString getJsonString( String name ) {
        JsonString result = (JsonString) changes.get(name);
        return result != null ? result : jsonObject.getJsonString(name);
    }

    @Override
    public String getString( String name ) {
        String result = (String) changes.get(name);
        return result != null ? result : jsonObject.getString(name);
    }

    @Override
    public String getString( String name, String defaultValue ) {
        String result = (String) changes.get(name);
        return result != null ? result : jsonObject.getString(name, defaultValue);
    }

    @Override
    public int getInt( String name ) {
        Integer result = (Integer) changes.get(name);
        return result != null ? result : jsonObject.getInt(name);
    }

    @Override
    public int getInt( String name, int defaultValue ) {
        Integer result = (Integer) changes.get(name);
        return result != null ? result : jsonObject.getInt(name, defaultValue);
    }

    @Override
    public boolean getBoolean( String name ) {
        Boolean result = (Boolean) changes.get(name);
        return result != null ? result : jsonObject.getBoolean(name);
    }

    @Override
    public boolean getBoolean( String name, boolean defaultValue ) {
        Boolean result = (Boolean) changes.get(name);
        return result != null ? result : jsonObject.getBoolean(name, defaultValue);
    }

    @Override
    public boolean isNull( String name ) {
        Object value = changes.get(name);
        return value == JsonValue.NULL || jsonObject.isNull(name);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.OBJECT;
    }

    @Override
    public JsonObject unwrap() {
        return jsonObject.merge(this);
    }

    protected JsonObject jsonObject() {
        return jsonObject;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        SchematicaEditableObject that = (SchematicaEditableObject)o;

        if (!changes.equals(that.changes)) {
            return false;
        }
        if (!jsonObject.equals(that.jsonObject)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + jsonObject.hashCode();
        result = 31 * result + changes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SchematicaEditableObject{");
        sb.append("jsonObject=").append(jsonObject);
        sb.append(", changes=").append(changes);
        sb.append('}');
        return sb.toString();
    }
}
