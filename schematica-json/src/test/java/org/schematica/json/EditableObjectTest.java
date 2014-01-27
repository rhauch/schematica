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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class EditableObjectTest {

    @Test
    public void shouldEditSimpleFields() throws Exception {
        JsonObject object = Json.createObjectBuilder().add("age", 22).add("name", "John Doe").build();
        JsonObject editedObject = object.edit().add("age", 55).add("surname", "Doe").unwrap();

        assertEquals(3, editedObject.size());
        assertEquals(55, editedObject.getInt("age"));
        assertEquals("John Doe", editedObject.getString("name"));
        assertEquals("Doe", editedObject.getString("surname"));
    }

    @Test
    public void shouldEditNestedFields() throws Exception {
        JsonObject object = Json.createObjectBuilder()
                                .add("name", "John Doe")
                                .add("home address", Json.createObjectBuilder()
                                                         .add("street", "A")
                                                         .add("number", 20))
                                .add("work address", Json.createObjectBuilder()
                                                         .add("street", "B")
                                                         .add("number", 10))
                                .build();

        JsonObject homeAddress = object.getJsonObject("home address");
        assertEquals("A", homeAddress.getString("street"));
        assertEquals(20, homeAddress.getInt("number"));

        JsonObject workAddress = object.getJsonObject("work address");
        assertEquals("B", workAddress.getString("street"));
        assertEquals(10, workAddress.getInt("number"));


        JsonObject editedObject = object.edit()
                                        .add("home address", Json.createObjectBuilder()
                                                                 .add("street", "W")
                                                                 .add("number", 66)
                                                                 .add("phone", 1234))
                                        .add("vacation address", Json.createObjectBuilder()
                                                                     .add("street", "X")
                                                                     .add("number", 77))
                                        .unwrap();

        homeAddress = editedObject.getJsonObject("home address");
        assertEquals("W", homeAddress.getString("street"));
        assertEquals(66, homeAddress.getInt("number"));
        assertEquals(1234, homeAddress.getInt("phone"));

        workAddress = editedObject.getJsonObject("work address");
        assertEquals("B", workAddress.getString("street"));
        assertEquals(10, workAddress.getInt("number"));

        assertNotNull(editedObject.getJsonObject("vacation address"));
    }

    @Test
    public void shouldMergeObjectsWithSimpleFields() throws Exception {
        JsonObject object1 = Json.createObjectBuilder().add("age1", 22).add("name1", "John Doe").build();
        JsonObject object2 = Json.createObjectBuilder().add("age2", 55).add("name2", "Jane Doe").build();

        JsonObject mergeResult = object1.merge(object2);
        assertEquals(22, mergeResult.getInt("age1"));
        assertEquals(55, mergeResult.getInt("age2"));
        assertEquals("John Doe", mergeResult.getString("name1"));
        assertEquals("Jane Doe", mergeResult.getString("name2"));
    }

    @Test
    public void shouldHandleNullValues() throws Exception {
        JsonObject object = Json.createObjectBuilder()
                                .add("age", 22)
                                .add("name", "John Doe")
                                .addNull("placeholder")
                                .build();
        JsonObject editedObject = object.edit()
                                        .addNull("name")
                                        .add("placeholder", "value")
                                        .unwrap();
        assertTrue(editedObject.isNull("name"));
        assertEquals("value", editedObject.getString("placeholder"));
    }
}
