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

package org.schematica.db.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.JsonObject;
import org.schematica.db.BulkWriteReport;
import org.schematica.db.Document;
import org.schematica.db.Observation;
import org.schematica.db.Schemas;
import org.schematica.db.SchematicaException;
import org.schematica.db.Sequence;
import org.schematica.db.Store;
import org.schematica.db.core.BulkWriteReports;
import org.schematica.db.core.BulkWriteReports.ReportBuilder;
import org.schematica.db.core.Collectors;
import org.schematica.db.core.Collectors.Collector;
import org.schematica.db.core.DocumentFormat;
import org.schematica.db.core.SimpleDocument;
import org.schematica.db.core.Util;
import org.schematica.db.jdbc.Database.ResultSetProcessor;
import org.schematica.db.task.Filter;
import org.schematica.db.task.Mapper;
import org.schematica.db.task.Results;
import org.schematica.db.task.Task;
import org.schematica.db.task.TaskBuilder;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class JdbcStore implements Store {

    protected static final DocumentFormat JSON_FORMAT = null;
    protected static final DocumentFormat BSON_FORMAT = null;
    protected static final int JSON_FORMAT_CODE = JSON_FORMAT.getType();
    protected static final int BSON_FORMAT_CODE = BSON_FORMAT.getType();

    protected final Database database;
    private final DocumentFormat converter;

    public JdbcStore( Connection connection ) {
        this(connection, JSON_FORMAT);
    }

    public JdbcStore( Connection connection,
                      DocumentFormat defaultFormat ) {
        this.database = new Database(connection);
        this.converter = defaultFormat;
    }

    @Override
    public long size() {
        return Database.executeQuery(database.readAllDocumentsSql(), Database.asLong(1));
    }

    @Override
    public boolean has( String key ) {
        return Database.executeQuery(database.hasDocumentSql(key), Database.atLeastOneRow());
    }

    @Override
    public Sequence<Document> readMultiple( Iterable<String> keys ) {
        Collector<Document, List<Document>> documents = Collectors.listOfDocuments(-1);
        ResultSetProcessor<Void> processor = readDocumentsFromResultsSet(documents);
        for (Collection<String> batch : database.determineBatchesOf(keys.iterator())) {
            Database.executeQuery(database.readMultipleDocumentSql(batch), processor);
        }
        return documents.asSequence();
    }

    @Override
    public Document read( final String key ) {
        Document result = Database.executeQuery(database.readDocumentSql(key), readDocumentFromResultsSet());
        if (result == null) {
            String msg = Util.createString("Document with key {0} was not found", key);
            throw new SchematicaException(msg);
        }
        return result;
    }

    @Override
    public boolean write( String key,
                          JsonObject document ) {
        return write(new SimpleDocument(key, document));
    }

    @Override
    public boolean write( Document document ) {
        String key = document.getKey();
        try {
            PreparedStatement sql = database.writeDocumentSQL(key, null, converter.write(document), converter.getType());
            int affected = Database.executeUpdate(sql);
            return affected > 1;
        } catch (IOException e) {
            String msg = Util.createString("Error converting document {0}->{1} was not found: {2}", key, document, e);
            throw new SchematicaException(msg);
        }
    }

    @Override
    public BulkWriteReport writeMultiple( Iterable<Document> documents,
                                          boolean captureResults ) {
        ReportBuilder reportBuilder = BulkWriteReports.create(captureResults);
        for (Document document : documents) {
            String key = document.getKey();
            try {
                PreparedStatement sql = database.writeDocumentSQL(key, null, converter.write(document), converter.getType());
                int affected = Database.executeUpdate(sql);
                if (reportBuilder.isRecording()) {
                    if (affected > 1) reportBuilder.recordOverwritten(key);
                    else reportBuilder.recordUpdated(key);
                }
            } catch (IOException e) {
                String msg = Util.createString("Error converting document {0}->{1} was not found: {2}", key, document, e);
                throw new SchematicaException(msg);
            }
        }
        return reportBuilder.getReport();
    }

    @Override
    public boolean writeIfAbsent( String key,
                                  JsonObject document ) {
        return writeIfAbsent(new SimpleDocument(key, document));
    }

    @Override
    public boolean writeIfAbsent( Document document ) {
        String key = document.getKey();
        try {
            PreparedStatement sql = database.insertIfAbsentDocumentSQL(key, null, converter.write(document), converter.getType());
            int affected = Database.executeUpdate(sql);
            return affected > 0;
        } catch (IOException e) {
            String msg = Util.createString("Error converting document {0}->{1} was not found: {2}", key, document, e);
            throw new SchematicaException(msg);
        }
    }

    @Override
    public void merge( String key,
                       JsonObject document ) {
        // Read the existing document ...
        Document existing = read(key);
        if (existing != null) {
            // TODO: merge documents ...
            // document = merge(existing,document);
        }
        // then write ...
        write(key, document);
    }

    @Override
    public void remove( String key ) {
        Database.execute(database.removeDocumentSql(key));
    }

    @Override
    public void remove( Iterable<String> keys ) {
        for (Collection<String> batch : database.determineBatchesOf(keys.iterator())) {
            Database.execute(database.removeMultipleDocumentsSql(batch));
        }
    }

    @Override
    public TaskBuilder filter( Filter filter ) {
        return null;
    }

    @Override
    public TaskBuilder all() {
        return new TaskBuilder() {
            @Override
            public Task<Long> totalCount() {
                // Return a new task that, when called, queries for the number of documents and returns that count ...
                return new Task<Long>() {
                    @Override
                    public Results<Long> call() throws Exception {
                        final long value = size();
                        return new Results<Long>() {
                            @Override
                            public Long output() {
                                return value;
                            }

                            @Override
                            public void close() {
                            }
                        };
                    }
                };
            }

            @Override
            public Task<Sequence<Document>> documents() {
                // Return a new task that, when called, queries for all documents and returns a sequence ...
                return new Task<Sequence<Document>>() {
                    @Override
                    public Results<Sequence<Document>> call() throws Exception {
                        // Even though we're calling the database twice in this method, we don't need to do this in a single
                        // transaction because we just need an estimated count to know what kind of DocumentCollector to get ...
                        long estimatedCount = size();
                        Collector<Document, List<Document>> documents = Collectors.listOfDocuments(estimatedCount);
                        Database.executeQuery(database.readAllDocumentsSql(), readDocumentsFromResultsSet(documents));
                        return documents.asSequenceResult();
                    }
                };
            }

            @Override
            public Task<Map<String, Document>> documentsByKey() {
                // Return a new task that, when called, queries for all documents and returns a map ...
                return new Task<Map<String, Document>>() {
                    @Override
                    public Results<Map<String, Document>> call() throws Exception {
                        // Even though we're calling the database twice in this method, we don't need to do this in a single
                        // transaction because we just need an estimated count to know what kind of DocumentCollector to get ...
                        long estimatedCount = size();
                        final Collector<Document, Map<String, Document>> documents = Collectors.mapOfDocuments(estimatedCount);
                        Database.executeQuery(database.readAllDocumentsSql(), readDocumentsFromResultsSet(documents));
                        return documents;
                    }
                };
            }

            @Override
            public Task<Sequence<String>> keys() {
                // Return a new task that, when called, queries for all documents and returns a map ...
                return new Task<Sequence<String>>() {
                    @Override
                    public Results<Sequence<String>> call() throws Exception {
                        // Even though we're calling the database twice in this method, we don't need to do this in a single
                        // transaction because we just need an estimated count to know what kind of DocumentCollector to get ...
                        long estimatedCount = size();
                        final Collector<String, Set<String>> keys = Collectors.setOfKeys(estimatedCount);
                        Database.executeQuery(database.readAllDocumentKeysSql(), readKeysFromResultsSet(keys));
                        return keys.asSequenceResult();
                    }
                };
            }

            @Override
            public <Kout, Vout> Reducible<Kout, Vout> map( Mapper<Kout, Vout> mapper ) {
                return null;
            }
        };
    }

    @Override
    public Schemas getSchemas() {
        return null;
    }

    @Override
    public Observation getObservation() {
        return null;
    }

    @Override
    public void close() {
    }

    protected ResultSetProcessor<Document> readDocumentFromResultsSet() {
        return new ResultSetProcessor<Document>() {
            @Override
            public Document process( ResultSet resultSet ) throws SQLException, IOException, SchematicaException {
                if (resultSet.next()) {
                    String key = resultSet.getString(1);
                    String schemaKey = resultSet.getString(2);
                    DocumentFormat format = getFormat(resultSet.getInt(3));
                    InputStream stream = resultSet.getBinaryStream(4);
                    assert format != null;
                    assert stream != null;
                    try {
                        return format.parse(key, stream, schemaKey);
                    } finally {
                        stream.close();
                    }
                }
                return null;
            }
        };
    }

    protected ResultSetProcessor<Void> readDocumentsFromResultsSet( final Collector<Document, ?> documents ) {
        return new ResultSetProcessor<Void>() {
            @Override
            public Void process( ResultSet resultSet ) throws SQLException, IOException, SchematicaException {
                while (resultSet.next()) {
                    String key = resultSet.getString(1);
                    String schemaKey = resultSet.getString(2);
                    DocumentFormat format = getFormat(resultSet.getInt(3));
                    InputStream stream = resultSet.getBinaryStream(4);
                    assert format != null;
                    assert stream != null;
                    try {
                        documents.add(format.parse(key, stream, schemaKey));
                    } finally {
                        stream.close();
                    }
                }
                return null;
            }
        };
    }

    protected ResultSetProcessor<Void> readKeysFromResultsSet( final Collector<String, ?> keys ) {
        return new ResultSetProcessor<Void>() {
            @Override
            public Void process( ResultSet resultSet ) throws SQLException, SchematicaException {
                if (resultSet.next()) {
                    String key = resultSet.getString(1);
                    assert key != null;
                    keys.add(key);
                }
                return null;
            }
        };
    }

    protected DocumentFormat getFormat( int format ) throws SchematicaException {
        if (format == JSON_FORMAT_CODE) return JSON_FORMAT;
        if (format == BSON_FORMAT_CODE) return BSON_FORMAT;
        throw new SchematicaException(Util.createString("Unknown persistence format {0}", format));
    }

}
