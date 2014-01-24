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

package org.schematica.db;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class DocumentUpdates {

    public static enum Action {
        UPDATED,
        OVERWRITTEN,
        NEITHER;
    }

    private final Set<String> updated;
    private final Set<String> overwritten;

    public DocumentUpdates() {
        this(null, null);
    }

    protected DocumentUpdates( Set<String> updated,
                               Set<String> overwritten ) {
        this.updated = updated != null ? updated : new HashSet<String>();
        this.overwritten = overwritten != null ? overwritten : new HashSet<String>();
    }

    public void recordUpdated( String key ) {
        updated.add(key);
    }

    public void recordOverwritten( String key ) {
        overwritten.add(key);
    }

    public boolean isUpdated( String key ) {
        return updated.contains(key);
    }

    public boolean isOverwritten( String key ) {
        return overwritten.contains(key);
    }

    public Iterator<String> updatedKeys() {
        return immutableIterator(this.updated);
    }

    public Iterator<String> overwrittenKeys() {
        return immutableIterator(this.overwritten);
    }

    protected final Iterator<String> immutableIterator( Set<String> values ) {
        final Iterator<String> keys = values.iterator();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return keys.hasNext();
            }

            @Override
            public String next() {
                return keys.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Action get( String key ) {
        if (updated.contains(key)) return Action.UPDATED;
        if (overwritten.contains(key)) return Action.OVERWRITTEN;
        return Action.NEITHER;
    }
}
