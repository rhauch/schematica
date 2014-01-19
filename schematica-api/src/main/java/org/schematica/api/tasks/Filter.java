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

package org.schematica.api.tasks;

import javax.json.JsonObject;
import org.schematica.api.Document;

/**
 * A simple component that filters documents.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Filter {
    /**
     * Determine whether the supplied document (and metadata) are deemed acceptable by this filter.
     * 
     * @param document the document; never null
     * @param metadata the metadata; never null
     * @return true if the document and metadata pass this filter, or false otherwise
     */
    boolean satisfies( Document document,
                       JsonObject metadata );
}
