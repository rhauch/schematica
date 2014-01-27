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

package org.schematica.json.impl.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class JsonValueConverter {

    private JsonValueConverter() {
    }

    public static JsonValue jsonValueFrom( final Object object ) {
        if (object instanceof JsonValue) {
            return (JsonValue)object;
        }
        if (object instanceof Boolean) {
            return object.equals(Boolean.TRUE) ? JsonValue.TRUE : JsonValue.FALSE;
        }
        if (object instanceof String) {
            return new SchematicaJsonString(object);
        }
        if (object instanceof Number) {
            return new SchematicaJsonNumber((Number) object);
        }
        if (object instanceof JsonObjectBuilder) {
            //note that this will flush the builder
            return ((JsonObjectBuilder)object).build();
        }
        if (object instanceof JsonArrayBuilder) {
            //note that this will flush the builder
            return ((JsonArrayBuilder)object).build();
        }
        throw new IllegalArgumentException("Object with class " + object.getClass() + " cannot be converted to a JSON value.");
    }

    private static class SchematicaJsonNumber implements JsonNumber {
        private final Number number;

        public SchematicaJsonNumber( Number number ) {
            this.number = number;
        }

        @Override
        public boolean isIntegral() {
            return number instanceof Integer || number instanceof Long || number instanceof Short || number instanceof BigInteger
                    || number instanceof Byte;
        }

        @Override
        public int intValue() {
            return number.intValue();
        }

        @Override
        public int intValueExact() {
            return BigDecimal.valueOf(doubleValue()).intValueExact();
        }

        @Override
        public long longValue() {
            return number.longValue();
        }

        @Override
        public long longValueExact() {
            return BigDecimal.valueOf(doubleValue()).longValueExact();
        }

        @Override
        public BigInteger bigIntegerValue() {
            return BigDecimal.valueOf(doubleValue()).toBigInteger();
        }

        @Override
        public BigInteger bigIntegerValueExact() {
            return BigDecimal.valueOf(doubleValue()).toBigIntegerExact();
        }

        @Override
        public double doubleValue() {
            return number.doubleValue();
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return BigDecimal.valueOf(doubleValue());
        }

        @Override
        public ValueType getValueType() {
            return ValueType.NUMBER;
        }
    }

    private static class SchematicaJsonString implements JsonString {
        private final Object object;

        public SchematicaJsonString( Object object ) {
            this.object = object;
        }

        @Override
        public String getString() {
            return (String)object;
        }

        @Override
        public CharSequence getChars() {
            return (String)object;
        }

        @Override
        public ValueType getValueType() {
            return ValueType.STRING;
        }
    }
}
