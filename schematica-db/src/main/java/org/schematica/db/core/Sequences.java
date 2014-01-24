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

package org.schematica.db.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.schematica.db.Sequence;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Sequences {

    protected static final class EmptySequence<T> implements Sequence<T> {
        protected EmptySequence() {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public static <T> Sequence<T> of( final Collection<T> values ) {
        if (values == null || values.isEmpty()) return new EmptySequence<T>();
        return new Sequence<T>() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public int size() {
                return values.size();
            }

            @Override
            public Iterator<T> iterator() {
                return values.iterator();
            }
        };
    }

    private Sequences() {
    }

}
