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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Util {

    // Prime number used in improving distribution: 1,000,003
    private static final int PRIME = 103;
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final Pattern PARAMETER_COUNT_PATTERN = Pattern.compile("\\{(\\d+)\\}");

    /**
     * Compute a combined hash code from the supplied objects. This method always returns 0 if no objects are supplied.
     * 
     * @param objects the objects that should be used to compute the hash code
     * @return the hash code
     */
    public static int hashCode( Object... objects ) {
        return computeHashCode(0, objects);
    }

    /**
     * Compute a combined hash code from the supplied objects using the supplied seed.
     * 
     * @param seed a value upon which the hash code will be based; may be 0
     * @param objects the objects that should be used to compute the hash code
     * @return the hash code
     */
    private static int computeHashCode( int seed,
                                        Object... objects ) {
        if (objects == null || objects.length == 0) {
            return seed * PRIME;
        }
        // Compute the hash code for all of the objects ...
        int hc = seed;
        for (Object object : objects) {
            hc = PRIME * hc;
            if (object instanceof byte[]) {
                hc += Arrays.hashCode((byte[])object);
            } else if (object instanceof boolean[]) {
                hc += Arrays.hashCode((boolean[])object);
            } else if (object instanceof short[]) {
                hc += Arrays.hashCode((short[])object);
            } else if (object instanceof int[]) {
                hc += Arrays.hashCode((int[])object);
            } else if (object instanceof long[]) {
                hc += Arrays.hashCode((long[])object);
            } else if (object instanceof float[]) {
                hc += Arrays.hashCode((float[])object);
            } else if (object instanceof double[]) {
                hc += Arrays.hashCode((double[])object);
            } else if (object instanceof char[]) {
                hc += Arrays.hashCode((char[])object);
            } else if (object instanceof Object[]) {
                hc += Arrays.hashCode((Object[])object);
            } else if (object != null) {
                hc += object.hashCode();
            }
        }
        return hc;
    }

    public static final void notNull( Object value,
                                      String name ) {
        if (value == null) throw new IllegalArgumentException("The '" + name + "' argument may not be null");
    }

    public static final String notNull( String value,
                                        String name ) {
        if (value == null) throw new IllegalArgumentException("The '" + name + "' argument may not be null");
        value = value.trim();
        return value;
    }

    public static final String notEmpty( String value,
                                         String name ) {
        if (value == null) throw new IllegalArgumentException("The '" + name + "' argument may not be empty");
        value = value.trim();
        return value;
    }

    /**
     * Create a string by substituting the parameters into all key occurrences in the supplied format. The pattern consists of
     * zero or more keys of the form <code>{n}</code>, where <code>n</code> is an integer starting at 0. Therefore, the first
     * parameter replaces all occurrences of "{0}", the second parameter replaces all occurrences of "{1}", etc.
     * <p>
     * If any parameter is null, the corresponding key is replaced with the string "null". Therefore, consider using an empty
     * string when keys are to be removed altogether.
     * </p>
     * <p>
     * If there are no parameters, this method does nothing and returns the supplied pattern as is.
     * </p>
     * 
     * @param pattern the pattern
     * @param parameters the parameters used to replace keys
     * @return the string with all keys replaced (or removed)
     */
    public static String createString( String pattern,
                                       Object... parameters ) {
        notNull(pattern, "pattern");
        if (parameters == null) parameters = EMPTY_STRING_ARRAY;
        Matcher matcher = PARAMETER_COUNT_PATTERN.matcher(pattern);
        // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
        StringBuffer text = new StringBuffer();
        int requiredParameterCount = 0;
        boolean err = false;
        while (matcher.find()) {
            int ndx = Integer.valueOf(matcher.group(1));
            if (requiredParameterCount <= ndx) {
                requiredParameterCount = ndx + 1;
            }
            if (ndx >= parameters.length) {
                err = true;
                matcher.appendReplacement(text, matcher.group());
            } else {
                Object parameter = parameters[ndx];

                // Automatically pretty-print arrays
                if (parameter != null && parameter.getClass().isArray()) {
                    parameter = Arrays.asList((Object[])parameter);
                }

                matcher.appendReplacement(text, Matcher.quoteReplacement(parameter == null ? "null" : parameter.toString()));
            }
        }
        if (err || requiredParameterCount < parameters.length) {
            String msg = createString("Incorrect number of paramters; found {0}{1} but expected {2}{3} parameters for pattern \"{4}\"",
                                      parameters.length,
                                      parameters.length == 1 ? "" : "s",
                                      requiredParameterCount,
                                      requiredParameterCount == 1 ? "" : "s",
                                      pattern);
            throw new IllegalArgumentException(msg);
        }
        matcher.appendTail(text);

        return text.toString();
    }

    /**
     * Create a new string containing the specified character repeated a specific number of times.
     * 
     * @param charToRepeat the character to repeat
     * @param numberOfRepeats the number of times the character is to repeat in the result; must be greater than 0
     * @return the resulting string
     */
    public static String createString( final char charToRepeat,
                                       int numberOfRepeats ) {
        assert numberOfRepeats >= 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfRepeats; ++i) {
            sb.append(charToRepeat);
        }
        return sb.toString();
    }

    /**
     * Returns an iterator over the supplied single value.
     * 
     * @param singleValue the single value over which the iterator should work; may not be null
     * @return the iterator; never null
     */
    public static final <T> Iterator<T> iteratorFor( final T singleValue ) {
        return new Iterator<T>() {
            private boolean done = false;

            @Override
            public boolean hasNext() {
                return !done;
            }

            @Override
            public T next() {
                done = true;
                return singleValue;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns an iterator over the supplied values.
     * 
     * @param firstValue the first value over which the iterator should work; may not be null
     * @param secondValue the first value over which the iterator should work; may not be null
     * @return the iterator; never null
     */
    public static final <T> Iterator<T> iteratorFor( final T firstValue,
                                                     final T secondValue ) {
        return new Iterator<T>() {
            private int index = -1;

            @Override
            public boolean hasNext() {
                return index < 1;
            }

            @Override
            public T next() {
                ++index;
                if (index == 0) return firstValue;
                if (index == 1) return secondValue;
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns an iterator over the supplied values.
     * 
     * @param firstValue the first value over which the iterator should work; may not be null
     * @param secondValue the second value over which the iterator should work; may not be null
     * @param thirdValue the third value over which the iterator should work; may not be null
     * @return the iterator; never null
     */
    public static final <T> Iterator<T> iteratorFor( final T firstValue,
                                                     final T secondValue,
                                                     final T thirdValue ) {
        return new Iterator<T>() {
            private int index = -1;

            @Override
            public boolean hasNext() {
                return index < 2;
            }

            @Override
            public T next() {
                ++index;
                if (index == 0) return firstValue;
                if (index == 1) return secondValue;
                if (index == 2) return thirdValue;
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns an iterator over the supplied array. The array is never leaked from this iterator.
     * 
     * @param array the array over which the iterator should work; may not be null
     * @return the iterator
     */
    public static final <T> Iterator<T> iteratorFor( final T[] array ) {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index <= array.length;
            }

            @Override
            public T next() {
                if (++index == array.length) throw new NoSuchElementException();
                return array[index];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns an empty iterator for the supplied type.
     * 
     * @param type the type
     * @return the iterator
     */
    public static final <T> Iterator<T> emptyIterator( Class<T> type ) {
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

    private Util() {
    }

}
