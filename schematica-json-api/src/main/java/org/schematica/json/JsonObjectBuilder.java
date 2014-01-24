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
public interface JsonObjectBuilder extends javax.json.JsonObjectBuilder {

    @Override
    JsonObjectBuilder add( String name,
                           JsonValue value );

    @Override
    JsonObjectBuilder add( String name,
                           String value );

    @Override
    JsonObjectBuilder add( String name,
                           BigInteger value );

    @Override
    JsonObjectBuilder add( String name,
                           BigDecimal value );

    @Override
    JsonObjectBuilder add( String name,
                           int value );

    @Override
    JsonObjectBuilder add( String name,
                           long value );

    @Override
    JsonObjectBuilder add( String name,
                           double value );

    @Override
    JsonObjectBuilder add( String name,
                           boolean value );

    @Override
    JsonObjectBuilder addNull( String name );


    @Override
    JsonObjectBuilder add( String name,
                           javax.json.JsonObjectBuilder builder );

    @Override
    JsonObjectBuilder add( String name,
                           javax.json.JsonArrayBuilder builder );

    @Override
    JsonObject build();

    /**
     * Set the value for the field with the given name to the supplied date value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This builder, to allow for chaining methods
     */
    JsonObjectBuilder add( String name,
                           Date value );

    /**
     * Set the value for the field with the given name to be a binary value. JSON does not formally support binary values, and so
     * such values will be encoded in Base64:
     *
     * @param name The name of the field
     * @param data the bytes representing the value
     * @return This builder, to allow for chaining methods
     */
    JsonObjectBuilder add( String name,
                           byte[] data );
}
