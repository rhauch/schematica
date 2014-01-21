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

package org.schematica.impl;

import javax.json.JsonValue;
import org.schematica.api.Field;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class BasicField implements Field {
    private final String name;
    private final Object value;

    protected BasicField( String name,
                          JsonValue jsonValue ) {
        this.name = name;
        //TODO author=Horia Chiorean date=1/21/14 description=Convert
        this.value = jsonValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
