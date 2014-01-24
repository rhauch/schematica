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
public interface JsonArray extends javax.json.JsonArray {

    @Override
    JsonObject getJsonObject( int index );

    @Override
    JsonArray getJsonArray( int index );

    long getLong(int index);

    long getLong(int index, long defaultValue);

    double getDouble(int index);

    double getDouble(int index, double defaultValue);

    BigInteger getBigInteger(int index);

    BigInteger getBigInteger(int index, BigInteger defaultValue);

    BigDecimal getBigDecimal(int index);

    BigDecimal getBigDecimal(int index, BigDecimal defaultValue);

    Date getDate(int index);

    Date getDate(int index, Date defaultValue);

    byte[] getBinary(int index);

    EditableJsonArray edit();

    JsonArray merge(javax.json.JsonArray other);
}
