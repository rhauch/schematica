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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.schematica.api.Document;
import org.schematica.api.DocumentEditor;
import org.schematica.api.Field;

/**
* @author Horia Chiorean (hchiorea@redhat.com)
*/
public class BasicDocument implements Document {
    private final String key;
    private final JsonObject json;

    BasicDocument( String key,
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

    @Override
    public DocumentEditor edit() {
        return new BasicDocumentEditor(key, json);
    }

    @Override
    public Boolean getBoolean( String name ) {
        return json.getBoolean(name);
    }

    @Override
    public boolean getBoolean( String name,
                               boolean defaultValue ) {
        return json.getBoolean(name, defaultValue);
    }

    @Override
    public Integer getInteger( String name ) {
        return json.getInt(name);
    }

    @Override
    public int getInteger( String name,
                           int defaultValue ) {
        return json.getInt(name, defaultValue);
    }

    @Override
    public Long getLong( String name ) {
        return json.getJsonNumber(name).longValue();
    }

    @Override
    public long getLong( String name,
                         long defaultValue ) {
        return json.containsKey(name) ? json.getJsonNumber(name).longValue() : defaultValue;
    }

    @Override
    public Double getDouble( String name ) {
        return json.getJsonNumber(name).doubleValue();
    }

    @Override
    public double getDouble( String name,
                             double defaultValue ) {
        return json.containsKey(name) ? json.getJsonNumber(name).doubleValue() : defaultValue;
    }

    @Override
    public String getString( String name ) {
        return json.getString(name);
    }

    @Override
    public String getString( String name,
                             String defaultValue ) {
        return json.getString(name, defaultValue);
    }

    @Override
    public <T> T[] getArray( String name,
                             Class<T> clazz ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public <T> List<T> getArrayAsList( String name,
                                       Class<T> clazz ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public <T> T[] getOrCreateArray( String name,
                                     Class<T> clazz ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public <T> List<T> getOrCreateArrayAsList( String name,
                                               Class<T> clazz ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public Document getDocument( String name ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public Document getOrCreateDocument( String name ) {
        //TODO author=Horia Chiorean date=1/21/14 description=implement
        return null;
    }

    @Override
    public boolean isNull( String name ) {
        return json.isNull(name);
    }

    @Override
    public boolean containsField( String name ) {
        return json.containsKey(name);
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(json.keySet());
    }

    @Override
    public int size() {
        return json.size();
    }

    @Override
    public boolean isEmpty() {
        return json.isEmpty();
    }

    @Override
    public Iterator<Field> iterator() {
        final Iterator<Map.Entry<String, JsonValue>> entryIterator = json.entrySet().iterator();
        return new Iterator<Field>() {
            @Override
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            @Override
            public Field next() {
                Map.Entry<String, JsonValue> nextEntry = entryIterator.next();
                return new BasicField(nextEntry.getKey(), nextEntry.getValue());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Cannot remove");
            }
        };
    }
}
