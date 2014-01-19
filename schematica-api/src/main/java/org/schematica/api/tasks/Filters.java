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

import java.util.regex.Pattern;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.schematica.api.Document;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Filters {

    public static enum Operator {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS;
    }

    static final class AllFilter implements Filter {
        @Override
        public boolean satisfies( Document document,
                                  JsonObject metadata ) {
            return true;
        }
    }

    private static final Filter ALL = new AllFilter();

    /**
     * Return a {@link Filter} that accepts all documents. This filter always returns {@code true} for
     * {@link Filter#satisfies(Document, JsonObject)}.
     * 
     * @return the filter; never null
     */
    public static Filter passthrough() {
        return ALL;
    }

    protected static final boolean canSkip( Filter filter ) {
        return filter == null || filter instanceof AllFilter;
    }

    /**
     * Create a {@link Filter} that requires both of the two supplied filters to be satisfied.
     * 
     * @param first the first filter to be satisfied; may not be null
     * @param second the second filter to be satisfied; may not be null
     * @return the new filter that performs a logical AND of the two filters; never null
     */
    public static Filter and( final Filter first,
                              final Filter second ) {
        return new Filter() {
            @Override
            public boolean satisfies( Document document,
                                      JsonObject metadata ) {
                return first.satisfies(document, metadata) && second.satisfies(document, metadata);
            }
        };
    }

    /**
     * Create a {@link Filter} that requires all of the supplied filters to be satisfied.
     * 
     * @param first the first filter to be satisfied
     * @param second the second filter to be satisfied
     * @param third the third filter to be satisfied
     * @param additional the additional filters to be satisfied
     * @return the new filter that performs a logical AND of all of the filters; never null
     */
    public static Filter and( Filter first,
                              Filter second,
                              Filter third,
                              Filter... additional ) {
        Filter filter = and(and(first, second), third);
        if (additional != null) {
            for (Filter more : additional) {
                filter = and(filter, more);
            }
        }
        return filter;
    }

    /**
     * Create a {@link Filter} that requires only one of the two supplied filters to be satisfied.
     * 
     * @param first the first filter; may not be null
     * @param second the second filter; may not be null
     * @return the new filter that performs a logical OR of the two filters; never null
     */
    public static Filter or( final Filter first,
                             final Filter second ) {
        return new Filter() {
            @Override
            public boolean satisfies( Document document,
                                      JsonObject metadata ) {
                return first.satisfies(document, metadata) || second.satisfies(document, metadata);
            }
        };
    }

    /**
     * Create a {@link Filter} that requires only one of the supplied filters to be satisfied.
     * 
     * @param first the first filter to be satisfied
     * @param second the second filter to be satisfied
     * @param third the third filter to be satisfied
     * @param additional the additional filters to be satisfied
     * @return the new filter that performs a logical OR of all of the filters; never null
     */
    public static Filter or( Filter first,
                             Filter second,
                             Filter third,
                             Filter... additional ) {
        Filter filter = or(or(first, second), third);
        if (additional != null) {
            for (Filter more : additional) {
                filter = or(filter, more);
            }
        }
        return filter;
    }

    /**
     * Create a {@link Filter} that negates the supplied filter.
     * 
     * @param filter the filter to be negated
     * @return the new filter that negates the supplied filter; never null
     */
    public static Filter not( final Filter filter ) {
        return new Filter() {
            @Override
            public boolean satisfies( Document document,
                                      JsonObject metadata ) {
                return !filter.satisfies(document, metadata);
            }
        };
    }

    /**
     * Start to create a {@link Filter} that applies a criteria to a field at a specific location in the document.
     * <p>
     * For example, the following shows how to create a filter that requires a document to contain the "address" field:
     * 
     * <pre>
     * Filter filter = Filters.path(&quot;address&quot;).exists();
     * </pre>
     * 
     * This next fragment of code creates a filter that requires a document to contain a "age" field to contain a value less than
     * 30':
     * 
     * <pre>
     * Filter filter = Filters.path(&quot;age&quot;).is(Operator.LESS_THAN, 30);
     * </pre>
     * 
     * </p>
     * 
     * @param path the path to the field in the document; may not be null and must be a valid path
     * @return the component that can be used to complete the creation of the {@link Filter}; never null.
     */
    public static FilterFieldMaker path( String path ) {
        return null;
    }

    /**
     * Start to create a {@link Filter} that applies a criteria to a field at a specific location in the <em>metadata</em> for a
     * document.
     * 
     * @param path the path to the field in the document; may not be null and must be a valid path
     * @return the component that can be used to complete the creation of the {@link Filter}; never null.
     */
    public static FilterFieldMaker metadata( String path ) {
        return null;
    }

    /**
     * A component that finishes making a {@link Filter} for a field in a document or metadata.
     * 
     * @author Randall Hauch (rhauch@redhat.com)
     */
    public static interface FilterFieldMaker {
        Filter exists();

        Filter isAbsent();

        Filter is( JsonValue value );

        Filter is( Operator op,
                   JsonNumber value );

        Filter is( Operator op,
                   int value );

        Filter is( Operator op,
                   short value );

        Filter is( Operator op,
                   long value );

        Filter is( Operator op,
                   float value );

        Filter is( Operator op,
                   double value );

        Filter isArrayWith( JsonValue value );

        Filter matches( Pattern regex );
    }

}
