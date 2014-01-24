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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.schematica.db.Document;
import org.schematica.db.Sequence;
import org.schematica.db.task.Results;

public class Collectors {
    public static abstract class Collector<ValueType, CollectionType> implements Results<CollectionType> {

        protected final CollectionType collected;

        protected Collector( CollectionType collected ) {
            this.collected = collected;
        }

        public abstract void add( ValueType document );

        public abstract CollectionType getCollected();

        public abstract Sequence<ValueType> asSequence();

        public Results<Sequence<ValueType>> asSequenceResult() {
            final Sequence<ValueType> sequence = asSequence();
            return new Results<Sequence<ValueType>>() {
                @Override
                public Sequence<ValueType> output() {
                    return sequence;
                }

                @Override
                public void close() {
                    Collector.this.close();
                }
            };
        }

        @Override
        public CollectionType output() {
            return collected;
        }
    }

    public static Collector<Document, List<Document>> listOfDocuments( long approximateSize ) {
        return new ListOfDocuments(new ArrayList<Document>());
    }

    public static Collector<Document, Map<String, Document>> mapOfDocuments( long approximateSize ) {
        return new MapOfDocuments(new HashMap<String, Document>());
    }

    public static Collector<String, Set<String>> setOfKeys( long approximateSize ) {
        return new SetOfKeys(new HashSet<String>());
    }

    protected static class ListOfDocuments extends Collector<Document, List<Document>> {
        protected ListOfDocuments( List<Document> container ) {
            super(container);
        }

        @Override
        public void add( Document document ) {
            collected.add(document);
        }

        @Override
        public List<Document> getCollected() {
            return collected;
        }

        @Override
        public Sequence<Document> asSequence() {
            return Sequences.of(collected);
        }

        @Override
        public void close() {
        }
    }

    protected static class MapOfDocuments extends Collector<Document, Map<String, Document>> {
        protected MapOfDocuments( Map<String, Document> container ) {
            super(container);
        }

        @Override
        public void add( Document document ) {
            collected.put(document.getKey(), document);
        }

        @Override
        public Map<String, Document> getCollected() {
            return collected;
        }

        @Override
        public Sequence<Document> asSequence() {
            return Sequences.of(collected.values());
        }

        @Override
        public void close() {
        }
    }

    protected static class SetOfKeys extends Collector<String, Set<String>> {
        protected SetOfKeys( Set<String> container ) {
            super(container);
        }

        @Override
        public void add( String key ) {
            collected.add(key);
        }

        @Override
        public Set<String> getCollected() {
            return collected;
        }

        @Override
        public Sequence<String> asSequence() {
            return Sequences.of(collected);
        }

        @Override
        public void close() {
        }
    }

}
