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

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public interface EditableJsonArray extends JsonArray {

    /**
     * Adds a value to the array as a {@link javax.json.JsonString}.
     *
     * @param value the string value
     * @return this instance
     * @throws NullPointerException if the specified value is null
     */
    EditableJsonArray add( String value );

    /**
     * Adds a value to the array as a {@link javax.json.JsonNumber}.
     *
     * @param value the number value
     * @return this instance
     * @throws NullPointerException if the specified value is null
     * @see javax.json.JsonNumber
     */
    EditableJsonArray add( BigDecimal value );

    /**
     * Adds a value to the array as a {@link javax.json.JsonNumber}.
     *
     * @param value the number value
     * @return this instance
     * @throws NullPointerException if the specified value is null
     * @see javax.json.JsonNumber
     */
    EditableJsonArray add( BigInteger value );

    /**
     * Adds a value to the array as a {@link javax.json.JsonNumber}.
     *
     * @param value the number value
     * @return this instance
     * @see javax.json.JsonNumber
     */
    EditableJsonArray add( int value );

    /**
     * Adds a value to the array as a {@link javax.json.JsonNumber}.
     *
     * @param value the number value
     * @return this instance
     * @see javax.json.JsonNumber
     */
    EditableJsonArray add( long value );

    /**
     * Adds a value to the array as a {@link javax.json.JsonNumber}.
     *
     * @param value the number value
     * @return this instance
     * @throws NumberFormatException if the value is Not-a-Number(NaN) or
     * infinity
     * @see javax.json.JsonNumber
     */
    EditableJsonArray add( double value );

    /**
     * Adds a {@link JsonValue#TRUE}  or {@link JsonValue#FALSE} value to the
     * array.
     *
     * @param value the boolean value
     * @return this instance
     */
    EditableJsonArray add( boolean value );

    /**
     * Adds a {@link JsonValue#NULL} value to the array.
     *
     * @return this instance
     */
    EditableJsonArray addNull();

    /**
     * Adds a {@link javax.json.JsonObject} from an object builder to the array.
     *
     * @param builder the object builder
     * @return this instance
     * @throws NullPointerException if the specified builder is null
     */
    EditableJsonArray add( javax.json.JsonObjectBuilder builder );

    /**
     * Adds a {@link javax.json.JsonArray} from an array builder to the array.
     *
     * @param builder the array builder
     * @return this array builder
     * @throws NullPointerException if the specified builder is null
     */
    EditableJsonArray add( javax.json.JsonArrayBuilder builder );

    /**
     * Adds a value to the array as a {@link java.util.Date}.
     *
     * @param value the date value
     * @return this instance
     * @throws NullPointerException if the specified value is null
     */
    EditableJsonArray add( Date value );

    /**
     * Adds a value to the array as a {@code byte[]}.
     *
     * @param value the byte array value
     * @return this instance
     * @throws NullPointerException if the specified value is null
     */
    EditableJsonArray add( byte[] value );

    /**
     * Applies the changes recorded during the edit operation on the underlying array and returns the resulting array.
     *
     * @return a {@link JsonArray} which contains the updated/edited values; never {@code null}
     */
    JsonArray unwrap();
}
