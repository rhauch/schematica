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

import java.sql.Date;
import java.util.regex.Pattern;
import javax.json.JsonNumber;
import javax.json.JsonValue;

/**
 * A factory for some standard and common {@link Filter} implementations.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface FilterBuilder {

    /**
     * Create a filter for which all documents satisfy the filter.
     * 
     * @return a filter that every document satisfies; never null
     */
    Filter selectAll();

    /**
     * Create a filter for which no documents satisfy the filter.
     * 
     * @return a filter that no document satisfies; never null
     */
    Filter selectNone();

    /**
     * Create a filter that is satisified only if a document's schema matches that specified by the supplied key.
     * 
     * @param schemaKey the key of the schema document; may not be null
     * @return a filter that matches the schema; never null
     */
    Filter withSchema( String schemaKey );

    /**
     * Create a filter that is satisified only if a document has no schema.
     * 
     * @return a filter that matches only when a document has no schema; never null
     */
    Filter withNoSchema();

    /**
     * Create a filter that is satisified only for documents that were first created/stored on a date that is
     * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
     * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
     * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified date.
     * <p>
     * For example:
     * 
     * <pre>
     * FilterBuilder builder = ...
     * Date date = ...
     * Filter filter = builder.createdDate(Operator.GREATER_THAN,date);
     * </pre>
     * 
     * </p>
     * 
     * @param op the operator; may not be null
     * @param date the date to use for the criteriat; may not be null
     * @return a filter that matches when a document is first stored relative to the specified date; never null
     */
    Filter createdDate( Operator op,
                        Date date );

    /**
     * Create a filter that is satisified only for documents that were last modified on a date that is {@link Operator#EQUALS
     * equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
     * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
     * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified date.
     * <p>
     * For example:
     * 
     * <pre>
     * FilterBuilder builder = ...
     * Date date = ...
     * Filter filter = builder.lastModifiedDate(Operator.LESS_THAN,date);
     * </pre>
     * 
     * </p>
     * 
     * @param op the operator; may not be null
     * @param date the date to use for the criteriat; may not be null
     * @return a filter that matches when a document is last modified relative to the specified date; never null
     */
    Filter lastModifiedDate( Operator op,
                             Date date );

    /**
     * Begin to create a filter on the documents' field given by the supplied name or path. The criteria of the filter must be
     * determined by one of the methods on the returned object.
     * <p>
     * For example:
     * 
     * <pre>
     * FilterBuilder builder = ...
     * Filter filter = builder.field("address").matches(regex);
     * </pre>
     * 
     * </p>
     * 
     * @param nameOrPath the name or path of the field; may not be null
     * @return the component that defines the field criteria; never null
     */
    PathFilter field( String nameOrPath );

    /**
     * A component used to create a {@link Filter} based upon the value of some field in the documents.
     * 
     * @author Randall Hauch (rhauch@redhat.com)
     */
    static interface PathFilter {
        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path exists and has a non-null
         * value.
         * 
         * @return a filter that matches when documents contain the specified field and a non-null value; never null
         */
        Filter exists();

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path does not exist or contains a
         * null value.
         * 
         * @return a filter that matches when documents do not contain the specified field or they contain the field with a null
         *         value; never null
         */
        Filter isAbsent();

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value
         * exactly matches the specified value.
         * 
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose value matches the specified value; never null
         */
        Filter is( JsonValue value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value
         * exactly matches the specified boolean value.
         * 
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose value matches the specified value; never null
         */
        Filter is( boolean value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   JsonNumber value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   int value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   short value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   long value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   float value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value is
         * {@link Operator#EQUALS equal to}, {@link Operator#NOT_EQUALS not equal to}, {@link Operator#GREATER_THAN greater than},
         * {@link Operator#GREATER_THAN_OR_EQUALS greater or equal to}, {@link Operator#LESS_THAN less than}, or
         * {@link Operator#LESS_THAN_OR_EQUALS less than or equal to} the specified value.
         * 
         * @param op the operator; may not be null
         * @param value the value to match; may not be null
         * @return a filter that matches when documents have a field whose numeric value is in the range allowed by the operator
         *         and specified value; never null
         */
        Filter is( Operator op,
                   double value );

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path contains a value that is
         * equal to at least one of the supplied values. If the field given by the name or path is an array, the filter matches
         * the document if the array contains at least one of the supplied values.
         * 
         * @param values the values
         * @return a filter that matches when documents have a field whose value matches one of the specified values; never null
         */
        Filter hasOneOf( Iterable<JsonValue> values );

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path contains a value that is
         * equal to at least one of the supplied values. If the field given by the name or path is an array, the filter matches
         * the document if the array contains at least one of the supplied values.
         * 
         * @param firstValue the firstValue
         * @param additionalValues the additional values
         * @return a filter that matches when documents have a field whose value matches one of the specified values; never null
         */
        Filter hasOneOf( JsonValue firstValue,
                         JsonValue... additionalValues );

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path contains a value that is
         * equal to at least one of the supplied values. If the field given by the name or path is an array, the filter matches
         * the document if the array contains at least one of the supplied values.
         * 
         * @param firstValue the firstValue
         * @param additionalValues the additional values
         * @return a filter that matches when documents have a field whose value matches one of the specified values; never null
         */
        Filter hasOneOf( String firstValue,
                         String... additionalValues );

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path contains a value that is
         * equal to at least one of the supplied values. If the field given by the name or path is an array, the filter matches
         * the document if the array contains at least one of the supplied values.
         * 
         * @param firstValue the firstValue
         * @param additionalValues the additional values
         * @return a filter that matches when documents have a field whose value matches one of the specified values; never null
         */
        Filter hasOneOf( int firstValue,
                         int... additionalValues );

        /**
         * Create a filter that is satisfied only for documents whose field at the given name or path contains a value that is
         * equal to at least one of the supplied values. If the field given by the name or path is an array, the filter matches
         * the document if the array contains at least one of the supplied values.
         * 
         * @param firstValue the firstValue
         * @param additionalValues the additional values
         * @return a filter that matches when documents have a field whose value matches one of the specified values; never null
         */
        Filter hasOneOf( long firstValue,
                         long... additionalValues );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path that is an array
         * that has as an element the specified value.
         * 
         * @param value the value that must be in the array; may not be null
         * @return a filter that matches when documents have a field that is an array with the specified value; never null
         */
        Filter isArrayWith( JsonValue value );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value
         * satisfies a regular expression.
         * 
         * @param regex the regular expression that must match the documents' field value; may not be null
         * @return a filter that matches when documents have a field that match the specified regular expression; never null
         */
        Filter matches( Pattern regex );

        /**
         * Create a filter that is satisfied only for documents that have a field with the given name or path and the value
         * matches the supplied pattern that uses SQL LIKE wildcards.
         * <p>
         * The following wildcards are supported:
         * <ul>
         * <li>Use the '{@code %}' (percent) wildcard character to match 0 or more characters.</li>
         * <li>Use the '{@code _}' (underscore) wildcard character to match exactly one character.</li>
         * <li>Use the '{@code [}<i>charlist</i>{@code ]}' to match sets and ranges of characters.</li>
         * <li>Use the '{@code [^}<i>charlist</i>{@code ]}' or {@code [!}<i>charlist</i>{@code ]}' to match only the characters
         * NOT in the brackets.</li>
         * </ul>
         * </p>
         * <p>
         * Examples of LIKE patterns include:
         * <ul>
         * <li>"{@code %ton%"} - Match values that contain the "{@code ton}" characters, including the word "ton".</li>
         * <li>"{@code L_n_"} - Match values that begin with 'L', followed by a character, followed by 'n' followed by a
         * character.</li>
         * <li>"{@code [a-c]%}" - Match values that start with 'a', 'b', or 'c'</li>
         * <li>"{@code [abc]%}" - Match values that start with 'a', 'b', or 'c'</li>
         * <li>"{@code [^a-c]%}" - Match values that start with characters other than 'a', 'b', or 'c'</li>
         * <li>"{@code [^adkn]%}" - Match values that start with characters other than 'a', 'd', 'k', or 'n'</li>
         * </ul>
         * </p>
         * 
         * @param likePattern the pattern to match; may not be null
         * @return a filter that matches when documents have a field whose value matches the specified value; never null
         */
        Filter like( String likePattern );

    }

    /**
     * The set of numeric operators.
     * 
     * @see PathFilter#is(Operator, double)
     * @see PathFilter#is(Operator, float)
     * @see PathFilter#is(Operator, int)
     * @see PathFilter#is(Operator, short)
     * @see PathFilter#is(Operator, long)
     * @see PathFilter#is(Operator, JsonNumber)
     * @author Randall Hauch (rhauch@redhat.com)
     */
    public static enum Operator {
        /** An operator that represents the {@code =} operation. */
        EQUALS,
        /** An operator that represents the {@code !=} operation. */
        NOT_EQUALS,
        /** An operator that represents the {@code >} operation. */
        GREATER_THAN,
        /** An operator that represents the {@code >=} operation. */
        GREATER_THAN_OR_EQUALS,
        /** An operator that represents the {@code <} operation. */
        LESS_THAN,
        /** An operator that represents the {@code <=} operation. */
        LESS_THAN_OR_EQUALS;
    }
}
