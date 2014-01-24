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

package org.schematica.db.task;

import java.util.Map;
import org.schematica.db.Document;
import org.schematica.db.Sequence;

public interface TaskMaker {
    Task<Long> totalCount();

    Task<Sequence<String>> keys();

    Task<Sequence<Document>> documents();

    Task<Map<String, Document>> documentsByKey();

    <Kout, Vout> TaskMaker.Reducable<Kout, Vout> map( Mapper<Kout, Vout> mapper );

    public static interface Reducable<Kout, Vout> {
        Task<Map<Kout, Vout>> reduce( Reducer<Kout, Vout> reducer );
    }
}
