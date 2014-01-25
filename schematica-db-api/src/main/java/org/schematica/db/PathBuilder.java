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
 * A component that can build paths.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 * @see Schematica#pathBuilder()
 */
public interface PathBuilder {

    /**
     * Obtain the empty path. Because {@link Path} instances are immutable, this method may always return the same instance.
     * 
     * @return the empty path; never null
     */
    Path emptyPath();

    /**
     * Parse the supplied path string into its object representation. See {@link Path} for the format.
     * 
     * @param path the string representation
     * @return the path; never null
     * @throws IllegalArgumentException if the supplied string representation is not a valid path
     */
    Path parse( String path );

    /**
     * Create a new path for the given sequence of individual path segments. Each of the supplied segments may contain a single
     * field name and an optional index (e.g., " {@code addresses[0]"}).
     * 
     * @param segments the segments for the path
     * @return the path; never null
     * @throws IllegalArgumentException if any of the supplied string segments are not valid path segments
     */
    Path pathWith( String... segments );

    /**
     * Create a new path given the parent path and one or more additional segments. Each of the supplied segments may contain a
     * single field name and an optional index (e.g., " {@code addresses[0]"}).
     * 
     * @param parent the parent path; may not be null
     * @param firstSegment the first segment
     * @param additionalSegments the additional segments
     * @return the path; never null
     * @throws IllegalArgumentException if any of the supplied string segments are not valid path segments
     */
    Path pathWith( Path parent,
                   String firstSegment,
                   String... additionalSegments );
}
