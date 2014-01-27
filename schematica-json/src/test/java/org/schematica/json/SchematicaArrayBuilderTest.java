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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaArrayBuilderTest {

    private JsonArrayBuilder builder;

    @Before
    public void before() {
        builder = Json.createArrayBuilder();
    }

    @Test
    public void shouldStoreSimpleDataTypes() {
        Date dateValue = new Date();
        boolean booleanValue = false;
        BigDecimal bigDecimalValue = new BigDecimal(12.4);
        BigInteger bigIntegerValue = new BigInteger(10, new Random());
        String stringValue = "some string";
        int intValue = Integer.MAX_VALUE;
        long longValue = Long.MAX_VALUE;
        double doubleValue = Double.MAX_VALUE;
        byte[] bytes = "The quick brown fox jumps over the lazy dog".getBytes();

        builder.add(dateValue)
               .add(booleanValue)
               .add(bigDecimalValue)
               .add(bigIntegerValue)
               .add(stringValue)
               .add(intValue)
               .add(longValue)
               .add(doubleValue)
               .add(bytes);

        JsonArray array = builder.build();

        assertEquals(dateValue, array.getDate(0));
        assertEquals(booleanValue, array.getBoolean(1));
        assertEquals(bigDecimalValue, array.getBigDecimal(2));
        assertEquals(bigIntegerValue, array.getBigInteger(3));
        assertEquals(stringValue, array.getString(4));
        assertEquals(intValue, array.getInt(5));
        assertEquals(longValue, array.getLong(6));
        assertEquals(doubleValue, array.getDouble(7), 0);
        assertArrayEquals(bytes, array.getBinary(8));
    }

    @Test
    public void shouldStoreComplexDataTypes() {
        javax.json.JsonObjectBuilder jsonObjectBuilder = javax.json.Json.createObjectBuilder();
        jsonObjectBuilder.add("name", "John").add("age", 15);

        javax.json.JsonArrayBuilder jsonArrayBuilder = javax.json.Json.createArrayBuilder();
        jsonArrayBuilder.add("string").add(false).add(1);

        JsonObjectBuilder schematicaObjectBuilder = Json.createObjectBuilder();
        schematicaObjectBuilder.add("name", "Michael").add("phone", 123456778);

        JsonArrayBuilder schematicaArrayBuilder = Json.createArrayBuilder();
        Date date = new Date();
        schematicaArrayBuilder.add("other string").add(true).add(2).add(date);

        builder.add(jsonObjectBuilder)
               .add(jsonArrayBuilder)
               .add(schematicaObjectBuilder)
               .add(schematicaArrayBuilder);

        JsonArray array = builder.build();

        assertEquals(javax.json.Json.createObjectBuilder().add("name", "John").add("age", 15).build(), array.getJsonObject(0));
        assertEquals(javax.json.Json.createArrayBuilder().add("string").add(false).add(1).build(), array.getJsonArray(1));
        assertEquals(Json.createObjectBuilder().add("name", "Michael").add("phone", 123456778).build(), array.getJsonObject(2));
        assertEquals(Json.createArrayBuilder().add("other string").add(true).add(2).add(date).build(), array.getJsonArray(3));
    }
}
