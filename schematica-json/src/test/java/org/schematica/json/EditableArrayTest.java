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

import static javax.json.JsonValue.ValueType.ARRAY;
import static javax.json.JsonValue.ValueType.FALSE;
import static javax.json.JsonValue.ValueType.NULL;
import static javax.json.JsonValue.ValueType.NUMBER;
import static javax.json.JsonValue.ValueType.OBJECT;
import static javax.json.JsonValue.ValueType.STRING;
import static javax.json.JsonValue.ValueType.TRUE;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.json.JsonValue;
import org.junit.Test;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class EditableArrayTest {

    @Test
    public void shouldEditArray() {
        JsonArray array = Json.createArrayBuilder().add(true).add(false).addNull().add("some string").add(new Date()).build();
        assertEquals(5, array.size());
        JsonArray updatedArray = array.edit().addNull()
                                             .add("some other string")
                                             .add(Json.createArrayBuilder().add(false))
                                             .add(Json.createObjectBuilder().add("field", "value"))
                                             .unwrap();
        assertEquals(9, updatedArray.size());

        List<JsonValue.ValueType> actualValueTypes = new ArrayList<>();
        for (JsonValue value : updatedArray) {
            actualValueTypes.add(value.getValueType());
        }
        assertEquals(Arrays.asList(TRUE, FALSE, NULL, STRING, NUMBER, NULL, STRING, ARRAY, OBJECT), actualValueTypes);
    }

    @Test
    public void shouldMergeArrays() {
        JsonArrayBuilder array1Builder = Json.createArrayBuilder();
        JsonArray array1 = array1Builder.add(1).add(2).build();

        JsonArrayBuilder array2Builder = Json.createArrayBuilder();
        JsonArray array2 = array2Builder.add(true).add(false).build();

        JsonArray mergeResult = array1.merge(array2);
        assertEquals(4, mergeResult.size());

        List<JsonValue.ValueType> actualValueTypes = new ArrayList<>();
        for (JsonValue value : mergeResult) {
            actualValueTypes.add(value.getValueType());
        }
        assertEquals(Arrays.asList(NUMBER, NUMBER, TRUE, FALSE), actualValueTypes);
    }
}
