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

/**
 * An immutable path to a field somewhere within a document. A path consists of 1 or more <i>segments</i> separated by the
 * {@code .} (period) character, where each segment represents the name of a field within the parent segment. The number of
 * segments in the path represent the depth of the field. A path describes which fields must be navigated (and in what order) to
 * locate the particular field of interest.
 * <p>
 * In a JSON document that contains only fields with string, number, boolean or {@code null} values, a valid path will always
 * contain a single segment. Consider this flat JSON document:
 * 
 * <pre>
 * {
 *   "firstName" : "Jane",
 *   "lastName" : "Smith",
 *   "phone" : "(111)555-1212"
 * </pre>
 * 
 * The path to any of the three fields is simply the name of the field: "{@code firstName}", "{@code lastName}", and "{@code phone}
 * ". For documents like this, paths don't add a lot of value.
 * <p>
 * When documents contain fields whose values are nested objects and arrays, then the paths become far more useful. Consider this
 * JSON document:
 * 
 * <pre>
 * {
 *   "firstName" : "Jane",
 *   "lastName" : "Smith",
 *   "addresses" : {
 *      "home" : {
 *         "street" : "Main Street",
 *         "city" : "Springfield",
 *         "zip" : "12345"
 *      },
 *      "work" : {
 *         "street" : "Washington Street",
 *         "city" : "Springfield",
 *         "zip" : "12346"
 *      },
 *   },
 *   "phone" : {
 *     "home" : "(111)555-1212",
 *     "mobile" : "(111)555-1234"
 *   }
 * }
 * </pre>
 * 
 * Paths make it much easier to identify fields that are within nested objects or arrays. For example, the field containing Jane's
 * work address can be represented by the path "{@code addresses.work}", while the path to the field containing Jane's work ZIP
 * code is "{@code addresses.work.zip}".
 * </p>
 * <h3>Arrays</h3>
 * <p>
 * When a document contains a nested array, the 0-based index of the array value is used for the segment. Here's a different
 * example:
 * 
 * <pre>
 * {
 *   "firstName" : "Jane",
 *   "lastName" : "Smith",
 *   "addresses" : [
 *      {
 *         "street" : "Main Street",
 *         "city" : "Springfield",
 *         "zip" : "12345"
 *      },
 *      {
 *         "street" : "Washington Street",
 *         "city" : "Springfield",
 *         "zip" : "12346"
 *      },
 *   ]
 * }
 * </pre>
 * 
 * In this example, the value of the "{@code addresses}" field is an array of objects. The path to the first address in the array
 * is "{@code addresses.0}" while the path to the second address is "{@code addresses.1}". The path to the ZIP code field in the
 * second address is "{@code addresses.1.zip}".
 * </p>
 * <p>
 * To make these a little easier to read and slightly more compact, you can optionally put the index into square brackets and
 * combine it with the previous segment. In this case, the path to the first address in the array is "{@code addresses[0]}" while
 * the path to the second address is "{@code addresses[1]}". Similarly, the path to the ZIP code field in the second address is "
 * {@code addresses[1].zip} ".
 * </p>
 * <h3>Parent paths</h3>
 * <p>
 * Every path has a <i>parent</i> path, which is simply the path containing all but the last segment. The only exception to this
 * rule is that an <i>empty path</i> has no segments and thus is its own parent.
 * </p>
 * <p>
 * A path is an <i>ancestor</i> of another path if it is the parent or (recursively) the parent of an ancestor.
 * </p>
 * <h3>Immutability</h3>
 * <p>
 * All {@code Path} objects are immutable, and thus can be shared and passed around without fear of another component changing an
 * object that you hold.
 * </p>
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @see PathBuilder
 * @see Schematica#pathBuilder()
 */
public interface Path extends Iterable<String>, Comparable<Path> {

    /**
     * The delimiter used between path segments.
     */
    static final char DELIMITER = '.';

    /**
     * Get the number of segments in this path. An empty path with have a size of '0'.
     * 
     * @return the size of this path; never negative
     */
    int size();

    /**
     * Get a particular segment within this path.
     * 
     * @param segmentNumber the 0-based index of the segment in this path
     * @return the segment; never null
     * @throws IndexOutOfBoundsException if the segment number is negative, or greater than or equal to the {@link #size() size}.
     */
    String get( int segmentNumber );

    /**
     * Get the first segment in the path.
     * 
     * @return the field name in the first segment of this path, or null only when this is the empty path
     */
    String getFirst();

    /**
     * Get the last segment in the path.
     * 
     * @return the field name in the last segment of this path, or null only when this is the empty path
     */
    String getLast();

    /**
     * Get the parent path, which may be an empty path if this is the empty path or if {@link #size()} is 1.
     * 
     * @return the parent path; never null
     */
    Path parent();

    /**
     * Obtain this path's ancestor whose {@link #size() size} matches the value specified.
     * 
     * @param size the size of the ancestor
     * @return the ancestor path, or this path if <code>size</code> is equal to {@link #size()}; never null
     * @throws IllegalArgumentException if the size is greater than {@link #size()}
     */
    Path ancestorOfSize( int size );

    /**
     * Determine if the first segments of this path are equal to the segments in the supplied path. This method returns true if
     * the two paths are equal, or if the all of the segments in the supplied path are equal to the same number of leading
     * segments in this path.
     * 
     * @param other the other path; may not be null
     * @return true if the other path is equal to or an ancestor of this path, or false otherwise
     */
    boolean startsWith( Path other );

    /**
     * Obtain a new path that has this path as its parent and the supplied field name as its last segment.
     * 
     * @param fieldName the field name for the last segment in the new path; may be null
     * @return the new path, or this path if the <code>fieldName</code> parameter is null; never null
     */
    Path with( String fieldName );

}
