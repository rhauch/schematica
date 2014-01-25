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

import javax.json.JsonObject;
import org.schematica.db.Document;

/**
 * A simple component that filters documents. All filters must be immutable, so that calling
 * {@link #satisfies(Document, JsonObject)} (or any other method) will never cause the behavior of the filter to be modified or
 * changed.
 * <p>
 * Some pre-defined filters can be obtained from {@link FilterBuilder}, and these are optimized within the system to push down as
 * much of the filter criteria down to the underlying database. But you can also implement {@link Filter} with your own logic;
 * such implementations may not be pushed down to the underlying database but rather executed in-process.
 * </p>
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public abstract class Filter {

    final static int PRIME = 103;

    /**
     * Determine whether the supplied document (and metadata) are deemed acceptable by this filter.
     * 
     * @param document the document; never null
     * @param metadata the metadata; never null
     * @return true if the document and metadata pass this filter, or false otherwise
     */
    public abstract boolean satisfies( Document document,
                                       JsonObject metadata );

    /**
     * Create a new {@link Filter} that requires <em>both</em> this filter <em>and</em> the specified filter to be satisfied.
     * 
     * @param otherFilter the second filter to be satisfied in addition to this filter
     * @return the new filter that performs a logical AND of this and another filter; never null, but possibly this Filter
     *         instance if {@code otherFilter} is null or is this Filter
     */
    public final Filter and( Filter otherFilter ) {
        if (otherFilter == null || this.equals(otherFilter)) return this;
        return new And(this, otherFilter);
    }

    /**
     * Create a new {@link Filter} that requires <em>either</em> this filter <em>or</em> the specified filter to be satisfied.
     * 
     * @param otherFilter the other filter to may be satisfied rather than this filter; may not be null
     * @return the new filter that performs a logical OR of this and another filter; never null, but possibly this Filter instance
     *         if {@code otherFilter} is null or is this Filter
     */
    public final Filter or( Filter otherFilter ) {
        if (this.equals(otherFilter)) return this;
        return new Or(this, otherFilter);
    }

    /**
     * Create a new {@link Filter} that is the negation of this filter. If this Filter instance is already a negation, this method
     * will return the negated Filter.
     * 
     * @return the new filter that negates this filter; never null
     */
    public final Filter not() {
        if (this instanceof Not) return ((Not)this).notted();
        return new Not(this);
    }

    protected static final class And extends Filter {
        private final Filter first;
        private final Filter second;
        private final int hc;

        protected And( Filter first,
                       Filter second ) {
            this.first = first;
            this.second = second;
            this.hc = 1 + PRIME * (first.hashCode() + PRIME * second.hashCode());
        }

        @Override
        public boolean satisfies( Document document,
                                  JsonObject metadata ) {
            return first.satisfies(document, metadata) && second.satisfies(document, metadata);
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof And) {
                And that = (And)obj;
                return this.hc == that.hc && this.first.equals(that.first) && this.second.equals(that.second);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public String toString() {
            return "And(" + first + "," + second + ")";
        }
    }

    protected static final class Or extends Filter {
        private final Filter first;
        private final Filter second;
        private final int hc;

        protected Or( Filter first,
                      Filter second ) {
            this.first = first;
            this.second = second;
            this.hc = 2 + PRIME * (first.hashCode() + PRIME * second.hashCode());
        }

        @Override
        public boolean satisfies( Document document,
                                  JsonObject metadata ) {
            return first.satisfies(document, metadata) && second.satisfies(document, metadata);
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Or) {
                Or that = (Or)obj;
                return this.hc == that.hc && this.first.equals(that.first) && this.second.equals(that.second);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public String toString() {
            return "Or(" + first + "," + second + ")";
        }
    }

    protected static final class Not extends Filter {
        private final Filter filter;
        private final int hc;

        protected Not( Filter filter ) {
            this.filter = filter;
            this.hc = 3 + PRIME * filter.hashCode();
        }

        protected Filter notted() {
            return filter;
        }

        @Override
        public boolean satisfies( Document document,
                                  JsonObject metadata ) {
            return !filter.satisfies(document, metadata);
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Not) {
                Not that = (Not)obj;
                return this.hc == that.hc && this.filter.equals(that.filter);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public String toString() {
            return "Not(" + filter + ")";
        }
    }
}
