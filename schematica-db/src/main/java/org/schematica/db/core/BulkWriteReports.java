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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.schematica.db.BulkWriteReport;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class BulkWriteReports {

    protected static final Set<String> EMPTY_SET = Collections.emptySet();
    private static final BulkWriteReport EMPTY_REPORT = new EmptyBulkWriteReport();
    private static final ReportBuilder EMPTY_REPORT_BUILDER = new EmptyReportBuilder();

    /**
     * A builder of {@link BulkWriteReport}s.
     * 
     * @author Randall Hauch (rhauch@redhat.com)
     */
    public static interface ReportBuilder {
        void recordUpdated( String key );

        void recordOverwritten( String key );

        boolean isRecording();

        BulkWriteReport getReport();
    }

    protected static final class EmptyReportBuilder implements ReportBuilder {
        @Override
        public void recordOverwritten( String key ) {
        }

        @Override
        public void recordUpdated( String key ) {
        }

        @Override
        public boolean isRecording() {
            return false;
        }

        @Override
        public BulkWriteReport getReport() {
            return empty();
        }
    }

    /**
     * Create an empty {@link BulkWriteReport}.
     * 
     * @return the empty report; never null
     */
    public static BulkWriteReport empty() {
        return EMPTY_REPORT;
    }

    /**
     * Create a new {@link ReportBuilder}.
     * 
     * @param captureResults true if this method should capture and return whether each document was updated or overwritten
     * @return the report maker; never null
     */
    public static ReportBuilder create( boolean captureResults ) {
        if (!captureResults) return EMPTY_REPORT_BUILDER;

        final Set<String> updated = new HashSet<>();
        final Set<String> overwritten = new HashSet<>();
        return new ReportBuilder() {

            @Override
            public boolean isRecording() {
                return true;
            }

            @Override
            public void recordUpdated( String key ) {
                updated.add(key);
            }

            @Override
            public void recordOverwritten( String key ) {
                overwritten.add(key);
            }

            @Override
            public BulkWriteReport getReport() {
                return new BasicBulkWriteReport(updated, overwritten);
            }
        };
    }

    protected static class EmptyBulkWriteReport implements BulkWriteReport {
        @Override
        public Action getAction( String key ) {
            return Action.NONE;
        }

        @Override
        public boolean isOverwritten( String key ) {
            return false;
        }

        @Override
        public boolean isUpdated( String key ) {
            return false;
        }

        @Override
        public Set<String> overwrittenKeys() {
            return EMPTY_SET;
        }

        @Override
        public Set<String> updatedKeys() {
            return EMPTY_SET;
        }
    }

    protected static class BasicBulkWriteReport implements BulkWriteReport {

        private final Set<String> updated;
        private final Set<String> overwritten;

        protected BasicBulkWriteReport( Set<String> updated,
                                        Set<String> overwritten ) {
            this.updated = updated != null ? updated : new HashSet<String>();
            this.overwritten = overwritten != null ? overwritten : new HashSet<String>();
        }

        @Override
        public boolean isUpdated( String key ) {
            return updated.contains(key);
        }

        @Override
        public boolean isOverwritten( String key ) {
            return overwritten.contains(key);
        }

        @Override
        public Set<String> updatedKeys() {
            return Collections.unmodifiableSet(this.updated);
        }

        @Override
        public Set<String> overwrittenKeys() {
            return Collections.unmodifiableSet(this.overwritten);
        }

        @Override
        public Action getAction( String key ) {
            if (updated.contains(key)) return Action.UPDATED;
            if (overwritten.contains(key)) return Action.OVERWRITTEN;
            return Action.NONE;
        }
    }

    private BulkWriteReports() {
    }

}
