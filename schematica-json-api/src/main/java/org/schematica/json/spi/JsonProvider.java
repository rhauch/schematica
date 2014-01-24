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

package org.schematica.json.spi;

import java.util.Iterator;
import java.util.ServiceLoader;
import javax.json.JsonException;
import org.schematica.json.JsonArrayBuilder;
import org.schematica.json.JsonObjectBuilder;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public abstract class JsonProvider extends javax.json.spi.JsonProvider {

    public static final JsonProvider PROVIDER_INSTANCE;

    static {
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        PROVIDER_INSTANCE = it.hasNext() ? it.next() : null;
        if (PROVIDER_INSTANCE == null) {
            throw new JsonException("No " + JsonProvider.class.getName() + " located in the classpath");
        }
    }

    @Override
    public abstract JsonObjectBuilder createObjectBuilder();

    @Override
    public abstract JsonArrayBuilder createArrayBuilder();
}
