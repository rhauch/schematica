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

package org.schematica.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.schematica.json.util.Base64;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaObject implements JsonObject {
    private final javax.json.JsonObject defaultObject;

    protected SchematicaObject( javax.json.JsonObject defaultObject ) {
        this.defaultObject = defaultObject;
    }

    @Override
    public JsonObject getJsonObject( String name ) {
        return new SchematicaObject(defaultObject.getJsonObject(name));
    }

    @Override
    public long getLong( String name ) {
        return getJsonNumber(name).longValue();
    }

    @Override
    public long getLong( String name,
                         long defaultValue ) {
        return containsKey(name) ? getLong(name) : defaultValue;
    }

    @Override
    public double getDouble( String name ) {
        return getJsonNumber(name).doubleValue();
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
        return new Date(getLong(name));
    }

    @Override
    public Date getDate( String name,
                         Date defaultValue ) {
        return containsKey(name) ? getDate(name) : defaultValue;
    }

    @Override
    public byte[] getBinary( String name ) {
        try {
            return Base64.decode(getString(name));
        } catch (IOException e) {
            throw new JsonException("Cannot decode Base64 field " + name);
        }
    }

    @Override
    public JsonArray getJsonArray( String name ) {
        return new SchematicaArray(defaultObject.getJsonArray(name));
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
        return defaultObject.getValueType();
    }

    @Override
    public int size() {
        return defaultObject.size();
    }

    @Override
    public boolean isEmpty() {
        return defaultObject.isEmpty();
    }

    @Override
    public boolean containsKey( Object key ) {
        return defaultObject.containsKey(key);
    }

    @Override
    public boolean containsValue( Object value ) {
        if (value instanceof Date) {
            value = ((Date) value).getTime();
        } else if (value instanceof byte[]) {
            value = Base64.encodeBytes((byte[]) value);
        }
        return defaultObject.containsValue(value);
    }

    @Override
    public JsonValue get( Object key ) {
        return defaultObject.get(key);
    }

    @Override
    public JsonValue put( String key,
                          JsonValue value ) {
        return defaultObject.put(key, value);
    }

    @Override
    public JsonValue remove( Object key ) {
        return defaultObject.remove(key);
    }

    @Override
    public void putAll( Map<? extends String, ? extends JsonValue> m ) {
        defaultObject.putAll(m);
    }

    @Override
    public void clear() {
        defaultObject.clear();
    }

    @Override
    public Set<String> keySet() {
        return defaultObject.keySet();
    }

    @Override
    public Collection<JsonValue> values() {
        return defaultObject.values();
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
        //TODO author=Horia Chiorean date=1/24/14 description=implement
        return null;
    }

    @Override
    public JsonObject merge( javax.json.JsonObject other ) {
        //TODO author=Horia Chiorean date=1/24/14 description=implement
        return this;
    }
}
