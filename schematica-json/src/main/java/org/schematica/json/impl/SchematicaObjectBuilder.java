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
import java.util.Date;
import javax.json.JsonValue;
import org.schematica.json.JsonObject;
import org.schematica.json.JsonObjectBuilder;
import org.schematica.json.impl.util.Base64;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaObjectBuilder implements JsonObjectBuilder {

    private final javax.json.JsonObjectBuilder defaultBuilder;

    public SchematicaObjectBuilder( javax.json.JsonObjectBuilder defaultBuilder ) {
        this.defaultBuilder = defaultBuilder;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  JsonValue value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  String value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  BigInteger value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  BigDecimal value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  int value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  long value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  double value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  boolean value ) {
        defaultBuilder.add(name, value);
        return this;
    }

    @Override
    public JsonObjectBuilder addNull( String name ) {
        defaultBuilder.addNull(name);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  javax.json.JsonObjectBuilder builder ) {
        defaultBuilder.add(name, builder);
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  javax.json.JsonArrayBuilder builder ) {
        defaultBuilder.add(name, builder);
        return this;
    }

    @Override
    public JsonObject build() {
        return new SchematicaObject(defaultBuilder.build());
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  Date value ) {
        defaultBuilder.add(name, value.getTime());
        return this;
    }

    @Override
    public JsonObjectBuilder add( String name,
                                  byte[] data ) {
        defaultBuilder.add(name, Base64.encodeBytes(data));
        return this;
    }
}
