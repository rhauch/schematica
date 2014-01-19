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

import java.util.Iterator;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Reducers {

    public static class Integers {
        public static Reducer<String, Integer> add() {
            return new Reducer<String, Integer>() {
                @Override
                public Integer reduce( String reducedKey,
                                       Iterator<Integer> values ) {
                    int total = 0;
                    while (values.hasNext()) {
                        total += values.next().intValue();
                    }
                    return total;
                }
            };
        }

        public static Reducer<String, Integer> maximum() {
            return new Reducer<String, Integer>() {
                @Override
                public Integer reduce( String reducedKey,
                                       Iterator<Integer> values ) {
                    int max = 0;
                    while (values.hasNext()) {
                        max = Math.max(max, values.next().intValue());
                    }
                    return max;
                }
            };
        }

        public static Reducer<String, Integer> minimum() {
            return new Reducer<String, Integer>() {
                @Override
                public Integer reduce( String reducedKey,
                                       Iterator<Integer> values ) {
                    int min = 0;
                    while (values.hasNext()) {
                        min = Math.min(min, values.next().intValue());
                    }
                    return min;
                }
            };
        }
    }

    public static class Longs {
        public static Reducer<String, Long> add() {
            return new Reducer<String, Long>() {
                @Override
                public Long reduce( String reducedKey,
                                    Iterator<Long> values ) {
                    long total = 0L;
                    while (values.hasNext()) {
                        total += values.next().longValue();
                    }
                    return total;
                }
            };
        }

        public static Reducer<String, Long> maximum() {
            return new Reducer<String, Long>() {
                @Override
                public Long reduce( String reducedKey,
                                    Iterator<Long> values ) {
                    long max = 0L;
                    while (values.hasNext()) {
                        max = Math.max(max, values.next().longValue());
                    }
                    return max;
                }
            };
        }

        public static Reducer<String, Long> minimum() {
            return new Reducer<String, Long>() {
                @Override
                public Long reduce( String reducedKey,
                                    Iterator<Long> values ) {
                    long min = 0L;
                    while (values.hasNext()) {
                        min = Math.min(min, values.next().longValue());
                    }
                    return min;
                }
            };
        }
    }

    private Reducers() {
    }

}
