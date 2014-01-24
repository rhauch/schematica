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
public class SchematicaObjectBuilderTest {

    private JsonObjectBuilder builder;

    @Before
    public void before() {
        builder = Json.createObjectBuilder();
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

        builder.add("date", dateValue)
               .add("boolean", booleanValue)
               .add("bigDecimal", bigDecimalValue)
               .add("bigInteger", bigIntegerValue)
               .add("string", stringValue)
               .add("int", intValue)
               .add("long", longValue)
               .add("double", doubleValue)
               .add("binary", bytes);

        JsonObject object = builder.build();

        assertEquals(dateValue, object.getDate("date"));
        assertEquals(booleanValue, object.getBoolean("boolean"));
        assertEquals(bigDecimalValue, object.getBigDecimal("bigDecimal"));
        assertEquals(bigIntegerValue, object.getBigInteger("bigInteger"));
        assertEquals(stringValue, object.getString("string"));
        assertEquals(intValue, object.getInt("int"));
        assertEquals(longValue, object.getLong("long"));
        assertEquals(doubleValue, object.getDouble("double"), 0);
        assertArrayEquals(bytes, object.getBinary("binary"));
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

        builder.add("jsonObject", jsonObjectBuilder)
               .add("jsonArray", jsonArrayBuilder)
               .add("schematicaObject", schematicaObjectBuilder)
               .add("schematicaArray", schematicaArrayBuilder);

        JsonObject object = builder.build();

        assertEquals(javax.json.Json.createObjectBuilder().add("name", "John").add("age", 15).build(),
                     object.getJsonObject("jsonObject"));
        assertEquals(javax.json.Json.createArrayBuilder().add("string").add(false).add(1).build(),
                     object.getJsonArray("jsonArray"));
        assertEquals(Json.createObjectBuilder().add("name", "Michael").add("phone", 123456778).build(),
                     object.getJsonObject("schematicaObject"));
        assertEquals(Json.createArrayBuilder().add("other string").add(true).add(2).add(date).build(),
                     object.getJsonArray("schematicaArray"));
    }
}
