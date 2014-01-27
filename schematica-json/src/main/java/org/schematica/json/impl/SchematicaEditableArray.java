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
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.schematica.json.EditableJsonArray;
import org.schematica.json.Json;
import org.schematica.json.JsonArray;
import org.schematica.json.JsonObject;
import org.schematica.json.impl.util.JsonValueConverter;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 * @NotThreadSafe
 */
public class SchematicaEditableArray extends AbstractList<JsonValue> implements EditableJsonArray {

    private final JsonArray array;
    private final int arraySize;
    private final List<Object> changes;

    protected SchematicaEditableArray( JsonArray array ) {
        this.array = array;
        this.arraySize = array.size();
        this.changes = new ArrayList<>();
    }

    @Override
    public EditableJsonArray add( String value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( BigDecimal value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( BigInteger value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( int value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( long value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( double value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( boolean value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray addNull() {
        changes.add(NULL);
        return this;
    }

    @Override
    public EditableJsonArray add( JsonObjectBuilder builder ) {
        changes.add(builder.build());
        return this;
    }

    @Override
    public EditableJsonArray add( JsonArrayBuilder builder ) {
        changes.add(builder.build());
        return this;
    }

    @Override
    public EditableJsonArray add( Date value ) {
        changes.add(value);
        return this;
    }

    @Override
    public EditableJsonArray add( byte[] value ) {
        changes.add(value);
        return this;
    }

    @Override
    public JsonObject getJsonObject( int index ) {
        return index < arraySize ? array.getJsonObject(index) : (JsonObject)changes.get(index);
    }

    @Override
    public JsonArray getJsonArray( int index ) {
        return index < arraySize ? array.getJsonArray(index) : (JsonArray)changes.get(index);
    }

    @Override
    public long getLong( int index ) {
        return index < arraySize ? array.getLong(index) : (long)changes.get(index);
    }

    @Override
    public long getLong( int index, long defaultValue ) {
        if (index < arraySize) {
            return array.getLong(index, defaultValue);
        }
        return index < changes.size() ? (long)changes.get(index) : defaultValue;
    }

    @Override
    public double getDouble( int index ) {
        return index < arraySize ? array.getDouble(index) : (double)changes.get(index);
    }

    @Override
    public double getDouble( int index, double defaultValue ) {
        if (index < arraySize) {
            return array.getDouble(index, defaultValue);
        }
        return index < changes.size() ? (double)changes.get(index) : defaultValue;
    }

    @Override
    public BigInteger getBigInteger( int index ) {
        return index < arraySize ? array.getBigInteger(index) : (BigInteger)changes.get(index);
    }

    @Override
    public BigInteger getBigInteger( int index, BigInteger defaultValue ) {
        if (index < arraySize) {
            return array.getBigInteger(index, defaultValue);
        }
        return index < changes.size() ? (BigInteger)changes.get(index) : defaultValue;
    }

    @Override
    public BigDecimal getBigDecimal( int index ) {
        return index < arraySize ? array.getBigDecimal(index) : (BigDecimal)changes.get(index);
    }

    @Override
    public BigDecimal getBigDecimal( int index, BigDecimal defaultValue ) {
        if (index < arraySize) {
            return array.getBigDecimal(index, defaultValue);
        }
        return index < changes.size() ? (BigDecimal)changes.get(index) : defaultValue;
    }

    @Override
    public Date getDate( int index ) {
        return index < arraySize ? array.getDate(index) : (Date)changes.get(index);
    }

    @Override
    public Date getDate( int index, Date defaultValue ) {
        if (index < arraySize) {
            return array.getDate(index, defaultValue);
        }
        return index < changes.size() ? (Date)changes.get(index) : defaultValue;
    }

    @Override
    public byte[] getBinary( int index ) {
        return index < arraySize ? array.getBinary(index) : (byte[])changes.get(index);
    }

    @Override
    public EditableJsonArray edit() {
        return this;
    }

    @Override
    public JsonArray merge( javax.json.JsonArray other ) {
        return array.merge(changesToJson()).merge(other);
    }

    protected JsonArray changesToJson() {
        org.schematica.json.JsonArrayBuilder localChanges = Json.createArrayBuilder();
        for (Object value : changes) {
            localChanges.add(JsonValueConverter.jsonValueFrom(value));
        }
        return localChanges.build();
    }

    @Override
    public JsonNumber getJsonNumber( int index ) {
        return index < arraySize ? array.getJsonNumber(index) : (JsonNumber)changes.get(index);
    }

    @Override
    public JsonString getJsonString( int index ) {
        return index < arraySize ? array.getJsonString(index) : (JsonString)changes.get(index);
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs( Class<T> clazz ) {
        List<T> values = new ArrayList<>(arraySize + changes.size());
        values.addAll(array.getValuesAs(clazz));
        for (Object value : values) {
            values.add(clazz.cast(value));
        }
        return Collections.unmodifiableList(values);
    }

    @Override
    public String getString( int index ) {
        return index < arraySize ? array.getString(index) : (String)changes.get(index);
    }

    @Override
    public String getString( int index, String defaultValue ) {
        if (index < arraySize) {
            return array.getString(index, defaultValue);
        }
        return index < changes.size() ? (String)changes.get(index) : defaultValue;
    }

    @Override
    public int getInt( int index ) {
        return index < arraySize ? array.getInt(index) : (int)changes.get(index);
    }

    @Override
    public int getInt( int index, int defaultValue ) {
        if (index < arraySize) {
            return array.getInt(index, defaultValue);
        }
        return index < changes.size() ? (int)changes.get(index) : defaultValue;
    }

    @Override
    public boolean getBoolean( int index ) {
        return index < arraySize ? array.getBoolean(index) : (boolean)changes.get(index);
    }

    @Override
    public boolean getBoolean( int index, boolean defaultValue ) {
        if (index < arraySize) {
            return array.getBoolean(index, defaultValue);
        }
        return index < changes.size() ? (boolean)changes.get(index) : defaultValue;
    }

    @Override
    public boolean isNull( int index ) {
        return index < arraySize ? array.isNull(index) : changes.get(index) == NULL;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public int size() {
        return arraySize + changes.size();
    }

    @Override
    public JsonValue get( int index ) {
        return index < arraySize ? array.get(index) : JsonValueConverter.jsonValueFrom(changes.get(index - arraySize));
    }

    @Override
    public JsonArray unwrap() {
        return array.merge(this);
    }

    protected JsonArray getArray() {
        return array;
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

        SchematicaEditableArray that = (SchematicaEditableArray)o;

        if (!array.equals(that.array)) {
            return false;
        }
        if (!changes.equals(that.changes)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + array.hashCode();
        result = 31 * result + changes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SchematicaEditableArray{");
        sb.append("array=").append(array);
        sb.append(", changes=").append(changes);
        sb.append('}');
        return sb.toString();
    }
}
