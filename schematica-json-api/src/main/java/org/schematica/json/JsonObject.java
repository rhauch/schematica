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

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public interface JsonObject extends javax.json.JsonObject {

    @Override
    JsonArray getJsonArray( String name );

    @Override
    JsonObject getJsonObject( String name );

    long getLong(String name);

    long getLong(String name, long defaultValue);

    double getDouble(String name);

    double getDouble(String name, double defaultValue);

    BigInteger getBigInteger(String name);

    BigInteger getBigInteger(String name, BigInteger defaultValue);

    BigDecimal getBigDecimal(String name);

    BigDecimal getBigDecimal(String name, BigDecimal defaultValue);

    Date getDate(String name);

    Date getDate(String name, Date defaultValue);

    byte[] getBinary(String name);

    EditableJsonObject edit();

    JsonObject merge(javax.json.JsonObject other);
}
