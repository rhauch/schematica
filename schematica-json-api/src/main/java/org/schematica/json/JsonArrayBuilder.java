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
public interface JsonArrayBuilder extends javax.json.JsonArrayBuilder {

    @Override
    JsonArrayBuilder add( JsonValue value );

    @Override
    JsonArrayBuilder add( String value );

    @Override
    JsonArrayBuilder add( BigDecimal value );

    @Override
    JsonArrayBuilder add( BigInteger value );

    @Override
    JsonArrayBuilder add( int value );

    @Override
    JsonArrayBuilder add( long value );

    @Override
    JsonArrayBuilder add( double value );

    @Override
    JsonArrayBuilder add( boolean value );

    @Override
    JsonArrayBuilder addNull();

    @Override
    JsonArrayBuilder add( javax.json.JsonObjectBuilder builder );

    @Override
    JsonArrayBuilder add( javax.json.JsonArrayBuilder builder );

    @Override
    JsonArray build();

    JsonArrayBuilder add( Date value );

    JsonArrayBuilder add( byte[] value );
}
