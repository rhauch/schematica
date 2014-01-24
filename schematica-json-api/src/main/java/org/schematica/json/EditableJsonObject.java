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
import javax.json.*;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public interface EditableJsonObject extends JsonObject {

    /**
     * Adds a name/{@code JsonValue} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name or value is null
     */
    EditableJsonObject add( String name, JsonValue value );

    /**
     * Adds a name/{@code JsonString} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name or value is null
     */
    EditableJsonObject add( String name, String value );

    /**
     * Adds a name/{@code JsonNumber} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name or value is null
     * @see javax.json.JsonNumber
     */
    EditableJsonObject add( String name, BigInteger value );

    /**
     * Adds a name/{@code JsonNumber} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name or value is null
     * @see javax.json.JsonNumber
     */
    EditableJsonObject add( String name, BigDecimal value );

    /**
     * Adds a name/{@code JsonNumber} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name is null
     * @see javax.json.JsonNumber
     */
    EditableJsonObject add( String name, int value );

    /**
     * Adds a name/{@code JsonNumber} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name is null
     * @see javax.json.JsonNumber
     */
    EditableJsonObject add( String name, long value );

    /**
     * Adds a name/{@code JsonNumber} pair to the JSON object associated with
     * this instance. If the object contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NumberFormatException if the value is Not-a-Number(NaN) or
     * infinity
     * @throws NullPointerException if the specified name is null
     * @see javax.json.JsonNumber
     */
    EditableJsonObject add( String name, double value );

    /**
     * Adds a name/{@code JsonValue#TRUE} or name/{@code JsonValue#FALSE} pair
     * to the JSON object associated with this instance. If the object
     * contains a mapping for the specified name, this method replaces the old
     * value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name is null
     */
    EditableJsonObject add( String name, boolean value );

    /**
     * Adds a name/{@code JsonValue#NULL} pair to the JSON object associated
     * with this instance where the value is {@code null}.
     * If the object contains a mapping for the specified name, this method
     * replaces the old value with {@code null}.
     *
     * @param name name in the name/value pair
     * @return this instance
     * @throws NullPointerException if the specified name is null
     */
    EditableJsonObject addNull( String name );

    /**
     * Adds a name/{@code JsonObject} pair to the JSON object associated
     * with this instance. The value {@code JsonObject} is built from the
     * specified instance. If the object contains a mapping for the
     * specified name, this method replaces the old value with the
     * {@code JsonObject} from the specified instance.
     *
     * @param name name in the name/value pair
     * @param builder the value is the object associated with this builder
     * @return this instance
     * @throws NullPointerException if the specified name or builder is null
     */
    EditableJsonObject add( String name, javax.json.JsonObjectBuilder builder );

    /**
     * Adds a name/{@code JsonArray} pair to the JSON object associated with
     * this instance. The value {@code JsonArray} is built from the
     * specified array builder. If the object contains a mapping for the
     * specified name, this method replaces the old value with the
     * {@code JsonArray} from the specified array builder.
     *
     * @param name the name in the name/value pair
     * @param builder the value is the object array with this builder
     * @return this instance
     * @throws NullPointerException if the specified name or builder is null
     */
    EditableJsonObject add( String name, javax.json.JsonArrayBuilder builder );

    /**
     * Set the value for the field with the given name to the supplied date value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This builder, to allow for chaining methods
     */
    EditableJsonObject add( String name, Date value );

    /**
     * Set the value for the field with the given name to be a binary value. JSON does not formally support binary values, and so
     * such values will be encoded in Base64:
     *
     * @param name The name of the field
     * @param data the bytes representing the value
     * @return This builder, to allow for chaining methods
     */
    EditableJsonObject add( String name, byte[] data );
}
