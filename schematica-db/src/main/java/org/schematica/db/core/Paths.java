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
import org.schematica.db.Path;
import org.schematica.db.PathBuilder;
import org.schematica.db.SchematicaException;

/**
 * Basic implementation of {@link PathBuilder}.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Paths implements PathBuilder {

    /**
     * The shared threadsafe {@link PathBuilder} implementation.
     */
    public static final Paths INSTANCE = new Paths();

    protected static final Path EMPTY_PATH = new EmptyPath();

    private Paths() {
    }

    @Override
    public Path emptyPath() {
        return EMPTY_PATH;
    }

    @Override
    public Path parse( String path ) {
        return pathWith(path.split("(?<!\\\\)[.]")); // split on '.' not preceded by an escaping backslash
    }

    @Override
    public Path pathWith( String... segments ) {
        // Validate the segments ...
        for (String segment : segments) {
            if (segment == null || segment.trim().length() == 0) {
                throw new SchematicaException("The path segment '" + segment + "' is not valid");
            }
        }
        if (segments.length == 1) return new SinglePath(segments[0]);
        if (segments.length == 2) return new DoublePath(segments[0], segments[1]);
        if (segments.length == 3) return new TriplePath(segments[0], segments[1], segments[2]);
        return new MultiSegmentPath(segments);
    }

    public Path pathWith( String firstSegment,
                          String... additionalSegments ) {
        // Validate the segments ...
        if (firstSegment == null || firstSegment.trim().length() == 0) {
            throw new SchematicaException("The path segment '" + firstSegment + "' is not valid");
        }
        if (additionalSegments == null || additionalSegments.length == 0) {
            return new SinglePath(firstSegment);
        }
        for (String segment : additionalSegments) {
            if (segment == null || segment.trim().length() == 0) {
                throw new SchematicaException("The path segment '" + segment + "' is not valid");
            }
        }
        if (additionalSegments.length == 1) return new DoublePath(firstSegment, additionalSegments[0]);
        if (additionalSegments.length == 2) return new TriplePath(firstSegment, additionalSegments[0], additionalSegments[1]);
        String[] segments = new String[additionalSegments.length + 1];
        segments[0] = firstSegment;
        System.arraycopy(additionalSegments, 0, segments, 1, additionalSegments.length);
        return new MultiSegmentPath(segments);
    }

    @Override
    public Path pathWith( Path parent,
                          String firstSegment,
                          String... additionalSegments ) {
        Util.notNull(parent, "parent");
        Util.notNull(firstSegment, "firstSegment");
        if (additionalSegments == null || additionalSegments.length == 0) return parent.with(firstSegment);
        if (parent.size() == 0) {
            return pathWith(firstSegment, additionalSegments);
        }
        String[] segments = new String[additionalSegments.length + 1 + parent.size()];
        for (int i = 0; i != parent.size(); ++i) {
            segments[i] = parent.get(i);
        }
        segments[parent.size()] = firstSegment;
        System.arraycopy(additionalSegments, 0, segments, 1 + parent.size(), additionalSegments.length);
        return new MultiSegmentPath(segments);
    }

    protected static final class EmptyPath implements Path {
        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public String next() {
                    throw new NoSuchElementException();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public String get( int index ) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        }

        @Override
        public String getLast() {
            return null;
        }

        @Override
        public String getFirst() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean startsWith( Path ancestor ) {
            return ancestor.size() == 0;
        }

        @Override
        public Path with( String fieldName ) {
            return fieldName != null ? new SinglePath(Util.notEmpty(fieldName, "fieldName")) : this;
        }

        @Override
        public Path parent() {
            return EMPTY_PATH;
        }

        @Override
        public Path ancestorOfSize( int size ) {
            if (size >= 0) {
                String msg = Util.createString("The ancestor size '{0}' is larger than this path's size of '{1}'", size, size());
                throw new IllegalArgumentException(msg);
            }
            return EMPTY_PATH;
        }

        @Override
        public int compareTo( Path that ) {
            if (that == this) return 0;
            if (that instanceof EmptyPath) return 0;
            return 0 - that.size();
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Path) {
                Path that = (Path)obj;
                return that.size() == 0;
            }
            return false;
        }

        @Override
        public String toString() {
            return "";
        }
    }

    protected static final class SinglePath implements Path {

        private final String fieldName;

        protected SinglePath( String fieldName ) {
            this.fieldName = fieldName;
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                private boolean done = false;

                @Override
                public boolean hasNext() {
                    return !done;
                }

                @SuppressWarnings( "synthetic-access" )
                @Override
                public String next() {
                    done = true;
                    return fieldName;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public String get( int index ) {
            if (index != 0) throw new IndexOutOfBoundsException("Index: " + index + ", Size: 1");
            return fieldName;
        }

        @Override
        public String getLast() {
            return fieldName;
        }

        @Override
        public String getFirst() {
            return fieldName;
        }

        @Override
        public Path with( String fieldName ) {
            Util.notNull(fieldName, "fieldName");
            return new DoublePath(this.fieldName, fieldName);
        }

        @Override
        public Path parent() {
            return EMPTY_PATH;
        }

        @Override
        public Path ancestorOfSize( int size ) {
            if (size == 0) return EMPTY_PATH;
            if (size == 1) return this;
            String msg = Util.createString("The ancestor size '{0}' is larger than this path's size of '{1}'", size, size());
            throw new IllegalArgumentException(msg);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public int hashCode() {
            return fieldName.hashCode();
        }

        @Override
        public int compareTo( Path that ) {
            if (that == this) return 0;
            int diff = this.size() - that.size();
            if (diff != 0) return diff;
            return this.fieldName.compareTo(that.get(0));
        }

        @Override
        public boolean startsWith( Path ancestor ) {
            if (ancestor.size() == 0) return true;
            if (ancestor.size() == 1 && fieldName.equals(ancestor.get(0))) return true;
            return false;
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof SinglePath) {
                SinglePath that = (SinglePath)obj;
                return this.fieldName.equals(that.fieldName);
            }
            if (obj instanceof Path) {
                Path that = (Path)obj;
                if (this.size() != that.size()) return false;
                return this.fieldName.equals(that.get(0));
            }
            return false;
        }

        @Override
        public String toString() {
            return fieldName;
        }
    }

    protected static final class DoublePath implements Path {

        private final String fieldName1;
        private final String fieldName2;
        private final int hc;
        private transient Path parent;

        protected DoublePath( String fieldName1,
                              String fieldName2 ) {
            assert fieldName1 != null;
            assert fieldName2 != null;
            this.fieldName1 = fieldName1;
            this.fieldName2 = fieldName2;
            this.hc = Util.hashCode(this.fieldName1, this.fieldName2);
        }

        @Override
        public Iterator<String> iterator() {
            return Util.iteratorFor(fieldName1, fieldName2);
        }

        @Override
        public String get( int index ) {
            if (index == 0) return fieldName1;
            if (index == 1) return fieldName2;
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 2");
        }

        @Override
        public String getLast() {
            return fieldName2;
        }

        @Override
        public String getFirst() {
            return fieldName1;
        }

        @Override
        public Path with( String fieldName ) {
            Util.notNull(fieldName, "fieldName");
            return new TriplePath(fieldName1, fieldName2, fieldName);
        }

        @Override
        public Path parent() {
            if (parent == null) parent = new SinglePath(fieldName1);
            return parent;
        }

        @Override
        public Path ancestorOfSize( int size ) {
            if (size == 0) return EMPTY_PATH;
            if (size == 1) return parent();
            if (size == 2) return this;
            String msg = Util.createString("The ancestor size '{0}' is larger than this path's size of '{1}'", size, size());
            throw new IllegalArgumentException(msg);
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public int compareTo( Path that ) {
            if (that == this) return 0;
            int diff = this.size() - that.size();
            if (diff != 0) return diff;
            diff = this.fieldName1.compareTo(that.get(0));
            if (diff != 0) return diff;
            diff = this.fieldName2.compareTo(that.get(1));
            return diff;
        }

        @Override
        public boolean startsWith( Path ancestor ) {
            if (ancestor.size() == 0) return true;
            if (ancestor.size() == 1 && fieldName1.equals(ancestor.get(0))) return true;
            if (ancestor.size() == 2 && fieldName1.equals(ancestor.get(0)) && fieldName2.equals(ancestor.get(1))) return true;
            return false;
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Path) {
                Path that = (Path)obj;
                if (this.size() != that.size()) return false;
                return this.fieldName1.equals(that.get(0)) && this.fieldName2.equals(that.get(1));
            }
            return false;
        }

        @Override
        public String toString() {
            return fieldName1 + DELIMITER + fieldName2;
        }
    }

    protected static final class TriplePath implements Path {

        private final String fieldName1;
        private final String fieldName2;
        private final String fieldName3;
        private final int hc;
        private transient Path parent;

        protected TriplePath( String fieldName1,
                              String fieldName2,
                              String fieldName3 ) {
            assert fieldName1 != null;
            assert fieldName2 != null;
            assert fieldName3 != null;
            this.fieldName1 = fieldName1;
            this.fieldName2 = fieldName2;
            this.fieldName3 = fieldName3;
            this.hc = Util.hashCode(this.fieldName1, this.fieldName2, this.fieldName3);
        }

        @Override
        public Iterator<String> iterator() {
            return Util.iteratorFor(fieldName1, fieldName2, fieldName3);
        }

        @Override
        public String get( int index ) {
            if (index == 0) return fieldName1;
            if (index == 1) return fieldName2;
            if (index == 2) return fieldName3;
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 3");
        }

        @Override
        public String getLast() {
            return fieldName3;
        }

        @Override
        public String getFirst() {
            return fieldName1;
        }

        @Override
        public Path with( String fieldName ) {
            Util.notNull(fieldName, "fieldName");
            String[] fieldNames = new String[] {fieldName1, fieldName2, fieldName3, fieldName};
            return new MultiSegmentPath(fieldNames);
        }

        @Override
        public Path parent() {
            if (parent == null) {
                return new DoublePath(fieldName1, fieldName2);
            }
            return parent;
        }

        @Override
        public Path ancestorOfSize( int size ) {
            if (size == 0) return EMPTY_PATH;
            if (size == 1) return parent().parent();
            if (size == 2) return parent();
            if (size == 3) return this;
            String msg = Util.createString("The ancestor size '{0}' is larger than this path's size of '{1}'", size, size());
            throw new IllegalArgumentException(msg);
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public int hashCode() {
            return hc;
        }

        @Override
        public int compareTo( Path that ) {
            if (that == this) return 0;
            int diff = this.size() - that.size();
            if (diff != 0) return diff;
            diff = this.fieldName1.compareTo(that.get(0));
            if (diff != 0) return diff;
            diff = this.fieldName2.compareTo(that.get(1));
            if (diff != 0) return diff;
            diff = this.fieldName3.compareTo(that.get(2));
            return diff;
        }

        @Override
        public boolean startsWith( Path ancestor ) {
            if (ancestor.size() == 0) return true;
            if (ancestor.size() == 1 && fieldName1.equals(ancestor.get(0))) return true;
            if (ancestor.size() == 2 && fieldName1.equals(ancestor.get(0)) && fieldName2.equals(ancestor.get(1))) return true;
            if (ancestor.size() == 3 && fieldName1.equals(ancestor.get(0)) && fieldName2.equals(ancestor.get(1))
                && fieldName2.equals(ancestor.get(2))) return true;
            return false;
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Path) {
                Path that = (Path)obj;
                if (this.size() != that.size()) return false;
                return this.fieldName1.equals(that.get(0)) && this.fieldName2.equals(that.get(1))
                       && this.fieldName2.equals(that.get(2));
            }
            return false;
        }

        @Override
        public String toString() {
            return fieldName1 + DELIMITER + fieldName2 + DELIMITER + fieldName3;
        }
    }

    protected static final class MultiSegmentPath implements Path {

        private final String[] fieldNames;
        private transient Path parent;

        protected MultiSegmentPath( String[] fieldNames ) {
            assert fieldNames != null;
            assert fieldNames.length != 0;
            this.fieldNames = fieldNames;
        }

        @Override
        public Iterator<String> iterator() {
            return Util.iteratorFor(fieldNames);
        }

        @Override
        public String get( int index ) {
            return fieldNames[index];
        }

        @Override
        public String getLast() {
            return fieldNames[fieldNames.length - 1];
        }

        @Override
        public String getFirst() {
            return fieldNames[0];
        }

        @Override
        public int size() {
            return fieldNames.length;
        }

        @Override
        public boolean startsWith( Path other ) {
            if (other.size() > this.size()) return false;
            Iterator<String> thatIter = other.iterator();
            Iterator<String> thisIter = this.iterator();
            while (thatIter.hasNext() && thisIter.hasNext()) {
                if (!thisIter.next().equals(thatIter.next())) return false;
            }
            return !thatIter.hasNext();
        }

        @Override
        public Path with( String fieldName ) {
            return INSTANCE.pathWith(this, fieldName);
        }

        @Override
        public Path parent() {
            if (parent == null) {
                if (size() == 1) {
                    parent = EMPTY_PATH;
                } else if (size() == 2) {
                    parent = new SinglePath(fieldNames[0]);
                } else if (size() == 3) {
                    parent = new DoublePath(fieldNames[0], fieldNames[1]);
                } else if (size() == 4) {
                    parent = new TriplePath(fieldNames[0], fieldNames[1], fieldNames[2]);
                } else {
                    String[] fieldNames = new String[this.fieldNames.length - 1];
                    System.arraycopy(this.fieldNames, 0, fieldNames, 0, this.fieldNames.length - 1);
                    parent = new MultiSegmentPath(fieldNames);
                }
            }
            return parent;
        }

        @Override
        public Path ancestorOfSize( int size ) {
            if (size == 0) return EMPTY_PATH;
            int diff = size() - size;
            if (diff == 0) return this;
            if (diff < 0) {
                String msg = Util.createString("The ancestor size '{0}' is larger than this path's size of '{1}'", size, size());
                throw new IllegalArgumentException(msg);
            }
            if (diff == 1) return parent();
            if (size == 1) {
                return new SinglePath(fieldNames[0]);
            } else if (size == 2) {
                parent = new DoublePath(fieldNames[0], fieldNames[1]);
            } else if (size == 3) {
                parent = new TriplePath(fieldNames[0], fieldNames[1], fieldNames[2]);
            }
            return new MultiSegmentPath(Arrays.copyOfRange(this.fieldNames, 0, size));
        }

        @Override
        public int hashCode() {
            return fieldNames.hashCode();
        }

        @Override
        public int compareTo( Path that ) {
            if (that == this) return 0;
            int diff = this.size() - that.size();
            if (diff != 0) return diff;
            Iterator<String> thatIter = that.iterator();
            Iterator<String> thisIter = this.iterator();
            while (thatIter.hasNext()) {
                int value = thisIter.next().compareTo(thatIter.next());
                if (value != 0) return value;
            }
            assert !thisIter.hasNext();
            return 0;
        }

        @Override
        public boolean equals( Object obj ) {
            if (obj == this) return true;
            if (obj instanceof Path) {
                Path that = (Path)obj;
                if (this.size() != that.size()) return false;
                Iterator<String> thatIter = that.iterator();
                Iterator<String> thisIter = this.iterator();
                while (thatIter.hasNext()) {
                    if (!thisIter.next().equals(thatIter.next())) return false;
                }
                assert !thisIter.hasNext();
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i != fieldNames.length; ++i) {
                if (i != 0) sb.append(DELIMITER);
                sb.append(fieldNames[i]);
            }
            return sb.toString();
        }
    }
}
