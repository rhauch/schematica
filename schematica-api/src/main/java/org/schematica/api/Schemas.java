/*
 * Schematica (http://www.schematica.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.schematica.api;

import javax.json.JsonObject;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Schemas {

    Validator getValidator( String schemaUri );

    void register( String schemaUri,
                   JsonObject schemaDoc );

    void unregister( String schemaUri );

    public interface Validator {

        Results validate( Document document );
    }

    public interface Results {

    }
}
