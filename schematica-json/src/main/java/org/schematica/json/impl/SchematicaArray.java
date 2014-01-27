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
import java.util.AbstractList;
import java.util.Date;
import java.util.List;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.schematica.json.EditableJsonArray;
import org.schematica.json.Json;
import org.schematica.json.JsonArray;
import org.schematica.json.JsonArrayBuilder;
import org.schematica.json.JsonObject;
import org.schematica.json.impl.util.Base64;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaArray extends AbstractList<JsonValue> implements JsonArray {

    static final SchematicaArray EMPTY_INSTANCE = new SchematicaArray(javax.json.Json.createArrayBuilder().build());

    private final javax.json.JsonArray defaultArray;

    protected SchematicaArray( javax.json.JsonArray defaultArray ) {
        this.defaultArray = defaultArray;
    }

    @Override
    public JsonObject getJsonObject( int index ) {
        return new SchematicaObject(defaultArray.getJsonObject(index));
    }

    @Override
    public JsonArray getJsonArray( int index ) {
        return new SchematicaArray(defaultArray.getJsonArray(index));
    }

    @Override
    public long getLong( int index ) {
        return defaultArray.getJsonNumber(index).longValue();
    }

    @Override
    public long getLong( int index,
                         long defaultValue ) {
        try {
            return getLong(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public double getDouble( int index ) {
        return defaultArray.getJsonNumber(index).doubleValue();
    }

    @Override
    public double getDouble( int index,
                             double defaultValue ) {
        try {
            return getDouble(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public BigInteger getBigInteger( int index ) {
        return defaultArray.getJsonNumber(index).bigIntegerValue();
    }

    @Override
    public BigInteger getBigInteger( int index,
                                     BigInteger defaultValue ) {
        try {
            return getBigInteger(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public BigDecimal getBigDecimal( int index ) {
        return defaultArray.getJsonNumber(index).bigDecimalValue();
    }

    @Override
    public BigDecimal getBigDecimal( int index,
                                     BigDecimal defaultValue ) {
        try {
            return getBigDecimal(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Date getDate( int index ) {
        return new Date(getLong(index));
    }

    @Override
    public Date getDate( int index,
                         Date defaultValue ) {
        try {
            return getDate(index);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public byte[] getBinary( int index ) {
        try {
            return Base64.decode(getString(index));
        } catch (IOException e) {
            throw new JsonException("Cannot decode Base64 value at index " + index);
        }
    }

    @Override
    public JsonNumber getJsonNumber( int index ) {
        return defaultArray.getJsonNumber(index);
    }

    @Override
    public JsonString getJsonString( int index ) {
        return defaultArray.getJsonString(index);
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs( Class<T> clazz ) {
        return defaultArray.getValuesAs(clazz);
    }

    @Override
    public String getString( int index ) {
        return defaultArray.getString(index);
    }

    @Override
    public String getString( int index,
                             String defaultValue ) {
        return defaultArray.getString(index, defaultValue);
    }

    @Override
    public int getInt( int index ) {
        return defaultArray.getInt(index);
    }

    @Override
    public int getInt( int index,
                       int defaultValue ) {
        return defaultArray.getInt(index, defaultValue);
    }

    @Override
    public boolean getBoolean( int index ) {
        return defaultArray.getBoolean(index);
    }

    @Override
    public boolean getBoolean( int index,
                               boolean defaultValue ) {
        return defaultArray.getBoolean(index, defaultValue);
    }

    @Override
    public boolean isNull( int index ) {
        return defaultArray.isNull(index);
    }

    @Override
    public ValueType getValueType() {
        return ValueType.ARRAY;
    }

    @Override
    public int size() {
        return defaultArray.size();
    }

    @Override
    public JsonValue get( int index ) {
        return defaultArray.get(index);
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof javax.json.JsonArray)) {
            return false;
        }

        javax.json.JsonArray that = (javax.json.JsonArray)o;
        return defaultArray.equals(that);
    }

    @Override
    public int hashCode() {
        return defaultArray.hashCode();
    }

    @Override
    public String toString() {
        return defaultArray.toString();
    }

    @Override
    public EditableJsonArray edit() {
        return new SchematicaEditableArray(this);
    }

    @Override
    public JsonArray merge( javax.json.JsonArray other ) {
        javax.json.JsonArray mergeSource = other;
        if (other instanceof SchematicaEditableArray && ((SchematicaEditableArray) other).getArray().equals(this)) {
            mergeSource = (((SchematicaEditableArray) other)).changesToJson();
        }
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (JsonValue jsonValue : this) {
            arrayBuilder.add(jsonValue);
        }
        for (JsonValue otherJsonValue : mergeSource) {
            //TODO author=Horia Chiorean date=1/27/14 description=Should keep or ignore NULLs ?
            arrayBuilder.add(otherJsonValue);
        }
        return arrayBuilder.build();
    }
}
