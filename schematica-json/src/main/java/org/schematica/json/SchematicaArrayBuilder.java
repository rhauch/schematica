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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.json.JsonValue;
import org.schematica.json.util.Base64;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaArrayBuilder implements JsonArrayBuilder {

    private final javax.json.JsonArrayBuilder defaultBuilder;

    public SchematicaArrayBuilder( javax.json.JsonArrayBuilder defaultBuilder ) {
        this.defaultBuilder = defaultBuilder;
    }

    public JsonArrayBuilder add( JsonValue value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( String value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( BigDecimal value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( BigInteger value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( int value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( long value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( double value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add( boolean value ) {
        this.defaultBuilder.add(value);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull() {
        defaultBuilder.addNull();
        return this;
    }

    @Override
    public JsonArrayBuilder add( javax.json.JsonObjectBuilder builder ) {
        defaultBuilder.add(builder);
        return this;
    }

    @Override
    public JsonArrayBuilder add( javax.json.JsonArrayBuilder builder ) {
        defaultBuilder.add(builder);
        return this;
    }

    @Override
    public JsonArray build() {
        return new SchematicaArray(defaultBuilder.build());
    }

    @Override
    public JsonArrayBuilder add( Date value ) {
        defaultBuilder.add(value.getTime());
        return this;
    }

    @Override
    public JsonArrayBuilder add( byte[] value ) {
        defaultBuilder.add(Base64.encodeBytes(value));
        return this;
    }
}
