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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.schematica.db.SchematicaException;
import org.schematica.db.Store;
import org.schematica.db.core.Util;

/**
 * Helper class for manipulation with database.
 * <p>
 * This class looks for database SQL statements in properties files named "<code>schematica_{type}_database.properties</code>"
 * located within the "org/schematica/db" area of the classpath, where "<code>{type}</code>" is {@link #determineType()
 * determined} from the connection, and matches one of the following:
 * <ul>
 * <li><code>mysql</code></li>
 * <li><code>postgres</code></li>
 * <li><code>derby</code></li>
 * <li><code>hsql</code></li>
 * <li><code>h2</code></li>
 * <li><code>sqlite</code></li>
 * <li><code>db2</code></li>
 * <li><code>db2_390</code></li>
 * <li><code>informix</code></li>
 * <li><code>interbase</code></li>
 * <li><code>firebird</code></li>
 * <li><code>sqlserver</code></li>
 * <li><code>access</code></li>
 * <li><code>oracle</code></li>
 * <li><code>sybase</code></li>
 * </ul>
 * If the corresponding file is not found on the classpath, then the "<code>schematica_default_database.properties</code>" file is
 * used.
 * </p>
 * <p>
 * Each property file should contain the set of DDL and DML statements that are used by a Schematica {@link Store}, and the
 * database-specific file allows database-specific schemas and queries to be used.
 * </p>
 * <p>
 * Schematica provides out-of-the-box properties files for only some of the database types listed above. If you run into any
 * problems, you can override the statements by providing a property file that matches the naming pattern described above, and by
 * putting that file on the classpath. (If you want to override one of Schematica's out-of-the-box properties files, then be sure
 * to put your custom file first on the classpath.)
 * </p>
 * 
 * @author kulikov
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class Database {

    public static final String TABLE_NAME = "CONTENT_STORE";

    public static final String STATEMENTS_FILE_PATH = "org/schematica/db/";
    protected static final String STATEMENTS_FILE_PREFIX = "schematica_";
    protected static final String STATEMENTS_FILENAME_SUFFIX = "_database.properties";
    protected static final String DEFAULT_STATEMENTS_FILE_PATH = STATEMENTS_FILE_PATH + STATEMENTS_FILE_PREFIX + "default"
                                                                 + STATEMENTS_FILENAME_SUFFIX;

    public static enum Type {
        MYSQL,
        POSTGRES,
        DERBY,
        HSQL,
        H2,
        SQLITE,
        DB2,
        DB2_390,
        INFORMIX,
        INTERBASE,
        FIREBIRD,
        SQLSERVER,
        ACCESS,
        ORACLE,
        SYBASE,
        CASSANDRA,
        UNKNOWN;
    }

    private final Connection connection;
    private final Type databaseType;
    private final String prefix;
    private final String tableName;
    private final Properties statements;
    private PreparedStatement createTableSql;
    private PreparedStatement hasDocumentSql;
    private PreparedStatement readDocumentSql;
    private PreparedStatement read4DocumentsSql;
    private PreparedStatement read11DocumentsSql;
    private PreparedStatement read51DocumentsSql;
    private PreparedStatement writeDocumentSql;
    private PreparedStatement insertIfAbsentDocumentSql;
    private PreparedStatement removeDocumentSql;
    private PreparedStatement remove4DocumentsSql;
    private PreparedStatement remove11DocumentsSql;
    private PreparedStatement remove51DocumentsSql;
    private PreparedStatement readAllDocumentsSql;
    private PreparedStatement readAllKeysSql;
    private PreparedStatement countAllDocumentsSql;

    /**
     * Creates new instance of the database.
     * 
     * @param connection connection to a database
     * @throws SchematicaException if the database type cannot be determined
     */
    public Database( Connection connection ) throws SchematicaException {
        this(connection, null, null);
    }

    /**
     * Creates new instance of the database.
     * 
     * @param connection connection to a database
     * @param type the type of database; may be null if the type is to be determined
     * @param prefix the prefix for the table name; may be null or blank
     * @throws SchematicaException if the database type cannot be determined
     */
    public Database( Connection connection,
                     Type type,
                     String prefix ) throws SchematicaException {
        assert connection != null;
        this.connection = connection;
        this.databaseType = type != null ? type : determineType();
        this.prefix = prefix == null ? null : prefix.trim();
        this.tableName = this.prefix != null && this.prefix.length() != 0 ? this.prefix + TABLE_NAME : TABLE_NAME;
        // LOGGER.debug("Discovered DBMS type for binary store as '{0}' on {1}", databaseType, connection);

        try {
            // Load the default statements, which will be used if any db-specific statements are missing ...
            String statementsFilename = DEFAULT_STATEMENTS_FILE_PATH;
            InputStream statementStream = getClass().getClassLoader().getResourceAsStream(statementsFilename);
            Properties defaultStatements = new Properties();
            try {
                // LOGGER.trace("Loading default statement from '{0}'", statementsFilename);
                defaultStatements.load(statementStream);
            } finally {
                statementStream.close();
            }

            // Look for type-specific statements ...
            statementsFilename = statementsResourcePath(databaseType);
            statementStream = getClass().getClassLoader().getResourceAsStream(statementsFilename);
            if (statementStream != null) {
                // Try to read the type-specific statements ...
                try {
                    // LOGGER.trace("Loading DBMS-specific statement from '{0}'", statementsFilename);
                    statements = new Properties(defaultStatements);
                    statements.load(statementStream);
                } finally {
                    statementStream.close();
                }
            } else {
                // No type-specific statements, so just use the default statements ...
                statements = defaultStatements;
                // LOGGER.trace("No DBMS-specific statement found in '{0}'", statementsFilename);
            }

        } catch (IOException e) {
            throw new SchematicaException(e);
        }
    }

    protected String statementsResourcePath( Type databaseType ) {
        return STATEMENTS_FILE_PATH + STATEMENTS_FILE_PREFIX + databaseType.name().toLowerCase() + STATEMENTS_FILENAME_SUFFIX;
    }

    /**
     * Prepare this instance and the underlying database for usage.
     * 
     * @throws SchematicaException if there is a problem
     */
    public void initialize() throws SchematicaException {
        try {
            // First, prepare a statement to see if the table exists ...
            boolean createTable = true;
            try {
                PreparedStatement exists = prepareStatement("table_exists_query");
                // LOGGER.trace("Running statement: {0}", exists);
                exists.execute();
                exists.close();
                createTable = false;
            } catch (SQLException e) {
                // proceed to create the table ...
            }

            if (createTable) {
                // LOGGER.debug("Unable to find existing table. Attempting to create '{0}' table in {1}", tableName, connection);
                try {
                    PreparedStatement create = prepareStatement("create_table");
                    // LOGGER.trace("Running statement: {0}", create);
                    create.execute();
                    create.close();
                } catch (SQLException e) {
                    String msg = Util.createString("Error creating database table {0} in database {1} using connection {2}: {3}",
                                                   tableName,
                                                   databaseType,
                                                   connection,
                                                   e.getMessage());
                    throw new SchematicaException(msg);
                }
            }

            // Now prepare all of the SQL statements ...
            createTableSql = prepareStatement("create_table");
            hasDocumentSql = prepareStatement("has_document");
            readDocumentSql = prepareStatement("read_document");
            read4DocumentsSql = prepareStatement("read_4_documents");
            read11DocumentsSql = prepareStatement("read_11_documents");
            read51DocumentsSql = prepareStatement("read_51_documents");
            writeDocumentSql = prepareStatement("write_document");
            insertIfAbsentDocumentSql = prepareStatement("insert_document");
            removeDocumentSql = prepareStatement("remove_document");
            remove4DocumentsSql = prepareStatement("remove_4_documents");
            remove11DocumentsSql = prepareStatement("remove_11_documents");
            remove51DocumentsSql = prepareStatement("remove_51_documents");
            readAllDocumentsSql = prepareStatement("read_all_documents");
            readAllKeysSql = prepareStatement("read_all_keys");
            countAllDocumentsSql = prepareStatement("count_all_documents");
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    protected PreparedStatement prepareStatement( String statementKey ) throws SQLException {
        String statementString = statements.getProperty(statementKey);
        statementString = Util.createString(statementString, tableName);
        // LOGGER.trace("Preparing statement: {0}", statementString);
        return connection.prepareStatement(statementString);
    }

    protected Type determineType() throws SchematicaException {
        try {
            String name = connection.getMetaData().getDatabaseProductName().toLowerCase();
            if (name.toLowerCase().contains("mysql")) {
                return Type.MYSQL;
            } else if (name.contains("postgres")) {
                return Type.POSTGRES;
            } else if (name.contains("derby")) {
                return Type.DERBY;
            } else if (name.contains("hsql") || name.toLowerCase().contains("hypersonic")) {
                return Type.HSQL;
            } else if (name.contains("h2")) {
                return Type.H2;
            } else if (name.contains("sqlite")) {
                return Type.SQLITE;
            } else if (name.contains("db2")) {
                return Type.DB2;
            } else if (name.contains("informix")) {
                return Type.INFORMIX;
            } else if (name.contains("interbase")) {
                return Type.INTERBASE;
            } else if (name.contains("firebird")) {
                return Type.FIREBIRD;
            } else if (name.contains("sqlserver") || name.toLowerCase().contains("microsoft")) {
                return Type.SQLSERVER;
            } else if (name.contains("access")) {
                return Type.ACCESS;
            } else if (name.contains("oracle")) {
                return Type.ORACLE;
            } else if (name.contains("adaptive")) {
                return Type.SYBASE;
            } else if (name.contains("Cassandra")) {
                return Type.CASSANDRA;
            }
            return Type.UNKNOWN;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Closes connection with database.
     */
    public void disconnect() {
        if (connection != null) {
            boolean failed = false;
            try {
                if (createTableSql != null) createTableSql.close();
                if (hasDocumentSql != null) hasDocumentSql.close();
                if (readDocumentSql != null) readDocumentSql.close();
                if (read4DocumentsSql != null) read4DocumentsSql.close();
                if (read11DocumentsSql != null) read11DocumentsSql.close();
                if (read51DocumentsSql != null) read51DocumentsSql.close();
                if (writeDocumentSql != null) writeDocumentSql.close();
                if (insertIfAbsentDocumentSql != null) insertIfAbsentDocumentSql.close();
                if (removeDocumentSql != null) removeDocumentSql.close();
                if (remove4DocumentsSql != null) remove4DocumentsSql.close();
                if (remove11DocumentsSql != null) remove11DocumentsSql.close();
                if (remove51DocumentsSql != null) remove51DocumentsSql.close();
                if (readAllDocumentsSql != null) readAllDocumentsSql.close();
                if (readAllKeysSql != null) readAllKeysSql.close();
                if (countAllDocumentsSql != null) countAllDocumentsSql.close();
            } catch (SQLException e) {
                failed = true;
                throw new SchematicaException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    if (!failed) throw new SchematicaException(e);
                } finally {
                    createTableSql = null;
                    hasDocumentSql = null;
                    readDocumentSql = null;
                    read4DocumentsSql = null;
                    read11DocumentsSql = null;
                    read51DocumentsSql = null;
                    writeDocumentSql = null;
                    insertIfAbsentDocumentSql = null;
                    removeDocumentSql = null;
                    remove4DocumentsSql = null;
                    remove11DocumentsSql = null;
                    remove51DocumentsSql = null;
                    readAllDocumentsSql = null;
                    readAllKeysSql = null;
                    countAllDocumentsSql = null;
                }
            }
        }
    }

    /**
     * The connection that this object is using.
     * 
     * @return the connection; never null
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Shows type of this database.
     * 
     * @return database type identifier.
     */
    public Type getDatabaseType() {
        return databaseType;
    }

    /**
     * Current time.
     * 
     * @return current time in milliseconds
     */
    private long now() {
        return System.currentTimeMillis();
    }

    /**
     * Query statement to determine if a document with a given key exists.
     * 
     * @param key unique document identifier
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement hasDocumentSql( String key ) throws SchematicaException {
        try {
            hasDocumentSql.setString(1, key.toString());
            return hasDocumentSql;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Query statement that gets enough information to read a document with the given key.
     * 
     * @param key unique document identifier
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement readDocumentSql( String key ) throws SchematicaException {
        try {
            readDocumentSql.setString(1, key.toString());
            return readDocumentSql;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Query statement that gets enough information to read a document with the given key.
     * 
     * @param keys the unique identifiers of the documents to be read
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement readMultipleDocumentSql( Collection<String> keys ) throws SchematicaException {
        try {
            PreparedStatement statement = null;
            if (keys.size() == Batched.LARGE_BATCH) {
                statement = read51DocumentsSql;
            } else if (keys.size() == Batched.MEDIUM_BATCH) {
                statement = read11DocumentsSql;
            } else if (keys.size() == Batched.SMALL_BATCH) {
                statement = read4DocumentsSql;
            } else if (keys.size() == Batched.SINGLE_BATCH) {
                statement = readDocumentSql;
            } else {
                String msg = Util.createString("Invalid number of keys: {0} but should have been {1}, {2}, {3}, or {4}",
                                               keys.size(),
                                               Batched.SINGLE_BATCH,
                                               Batched.SMALL_BATCH,
                                               Batched.MEDIUM_BATCH,
                                               Batched.LARGE_BATCH);
                throw new IllegalArgumentException(msg);
            }
            Iterator<String> iter = keys.iterator();
            int index = 0;
            while (iter.hasNext()) {
                statement.setString(++index, iter.next());
            }
            return statement;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Create statement for inserting or updating a document to the store.
     * 
     * @param key unique document identifier
     * @param schemaKey unique identifier of the schema for this document; may be null
     * @param stream the document contents
     * @param format the format of the document representation
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement writeDocumentSQL( String key,
                                               String schemaKey,
                                               InputStream stream,
                                               int format ) throws SchematicaException {
        java.sql.Timestamp now = new java.sql.Timestamp(now());
        try {
            writeDocumentSql.setString(1, key);
            writeDocumentSql.setString(2, schemaKey);
            writeDocumentSql.setInt(3, format);
            writeDocumentSql.setTimestamp(4, now); // created
            writeDocumentSql.setTimestamp(5, now); // modified
            writeDocumentSql.setBinaryStream(6, stream);
            return writeDocumentSql;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Create statement for inserting a document to the store only if it does not already exist.
     * 
     * @param key unique document identifier
     * @param schemaKey unique identifier of the schema for this document; may be null
     * @param stream the document contents
     * @param format the format of the document representation
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement insertIfAbsentDocumentSQL( String key,
                                                        String schemaKey,
                                                        InputStream stream,
                                                        int format ) throws SchematicaException {
        java.sql.Timestamp now = new java.sql.Timestamp(now());
        try {
            insertIfAbsentDocumentSql.setString(1, key);
            insertIfAbsentDocumentSql.setString(2, schemaKey);
            insertIfAbsentDocumentSql.setInt(3, format);
            insertIfAbsentDocumentSql.setTimestamp(4, now); // created
            insertIfAbsentDocumentSql.setTimestamp(5, now); // modified
            insertIfAbsentDocumentSql.setBinaryStream(6, stream);
            return insertIfAbsentDocumentSql;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Statement that removes the documents with the supplied key.
     * 
     * @param key the unique identifier of the document to be removed
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement removeDocumentSql( String key ) throws SchematicaException {
        try {
            removeDocumentSql.setString(1, key);
            return removeDocumentSql;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Statement that removes all of the documents identified by the keys in the supplied set.
     * 
     * @param keys the unique identifiers of the documents to be removed
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement removeMultipleDocumentsSql( Collection<String> keys ) throws SchematicaException {
        try {
            PreparedStatement statement = null;
            if (keys.size() == Batched.LARGE_BATCH) {
                statement = remove51DocumentsSql;
            } else if (keys.size() == Batched.MEDIUM_BATCH) {
                statement = remove11DocumentsSql;
            } else if (keys.size() == Batched.SMALL_BATCH) {
                statement = remove4DocumentsSql;
            } else if (keys.size() == Batched.SINGLE_BATCH) {
                statement = removeDocumentSql;
            } else {
                String msg = Util.createString("Invalid number of keys: {0} but should have been {1}, {2}, {3}, or {4}",
                                               keys.size(),
                                               Batched.SINGLE_BATCH,
                                               Batched.SMALL_BATCH,
                                               Batched.MEDIUM_BATCH,
                                               Batched.LARGE_BATCH);
                throw new IllegalArgumentException(msg);
            }
            Iterator<String> iter = keys.iterator();
            int index = 0;
            while (iter.hasNext()) {
                statement.setString(++index, iter.next());
            }
            return statement;
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Statement that selects all of the documents.
     * 
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement readAllDocumentsSql() throws SchematicaException {
        return readAllDocumentsSql;
    }

    /**
     * Statement that selects all of the unique document keys.
     * 
     * @return SQL statement.
     * @throws SchematicaException
     */
    public PreparedStatement readAllDocumentKeysSql() throws SchematicaException {
        return readAllKeysSql;
    }

    /**
     * Executes specifies statement.
     * 
     * @param sql the statement to execute
     * @throws SchematicaException
     */
    public static void execute( PreparedStatement sql ) throws SchematicaException {
        try {
            // LOGGER.trace("Running statement: {0}", sql);
            sql.execute();
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Runs SQL statement as a query, returning the result set.
     * 
     * @param sql the prepared SQL statement; may not be null
     * @return result of statement execution, which <em>must</em> be closed when no longer needed
     * @throws SchematicaException if a problem occurs during execution
     */
    public static ResultSet executeQuery( PreparedStatement sql ) throws SchematicaException {
        try {
            // LOGGER.trace("Running statement: {0}", sql);
            return sql.executeQuery();
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Runs SQL statement as a query and processes the result set, returning the output of the processing and <em>always</em>
     * closing the result set.
     * 
     * @param sql the prepared SQL statement; may not be null
     * @param processor the result set processor; may not be null
     * @return result of the processor
     * @throws SchematicaException if a problem occurs during execution
     */
    public static <T> T executeQuery( PreparedStatement sql,
                                      ResultSetProcessor<T> processor ) throws SchematicaException {
        try {
            // LOGGER.trace("Running statement: {0}", sql);
            ResultSet rs = sql.executeQuery();
            return rs == null ? null : process(rs, processor); // always closes result set
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * Executes specifies update statement.
     * 
     * @param sql the statement to execute
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that
     *         return nothing
     * @throws SchematicaException
     */
    public static int executeUpdate( PreparedStatement sql ) throws SchematicaException {
        try {
            // LOGGER.trace("Running statement: {0}", sql);
            return sql.executeUpdate();
        } catch (SQLException e) {
            throw new SchematicaException(e);
        }
    }

    /**
     * A component that processes a {@link ResultSet}.
     * 
     * @param <T> the return type of the processor
     * @author Randall Hauch (rhauch@redhat.com)
     */
    public static interface ResultSetProcessor<T> {
        /**
         * Process the result set and return a value.
         * 
         * @param resultSet the result set; never null
         * @return the result
         * @throws SQLException if there is a problem operating against the result set
         * @throws IOException if there is a problem streaming data in the result set
         * @throws SchematicaException if there is a problem processing the results
         */
        T process( ResultSet resultSet ) throws SQLException, IOException, SchematicaException;
    }

    /**
     * Process the result set using the supplied processor. The result set will always be closed after the processing has been
     * done.
     * 
     * @param resultSet the result set; may not be null
     * @param processor the processor; may not be null
     * @return the result of the processing
     * @throws SchematicaException if there is a problem processing the results
     */
    public static <T> T process( ResultSet resultSet,
                                 ResultSetProcessor<T> processor ) throws SchematicaException {
        boolean error = false;
        try {
            return processor.process(resultSet);
        } catch (RuntimeException e) { // includes SchematicaException
            error = true;
            throw e; // just rethrow ...
        } catch (SQLException | IOException e) {
            error = true;
            // wrap the exception ...
            throw new SchematicaException(e);
        } finally {
            // Always close the result set ...
            try {
                resultSet.close();
            } catch (SQLException e) {
                if (!error) throw new SchematicaException(e);
            }
        }
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that simply determines if the result set has at least one (more) row.
     * 
     * @return true if there is one (more) row, or false otherwise
     */
    public static ResultSetProcessor<Boolean> atLeastOneRow() {
        return new ResultSetProcessor<Boolean>() {
            @Override
            public Boolean process( ResultSet resultSet ) throws SQLException {
                return resultSet.next();
            }
        };
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that simply determines how many rows are in the result set.
     * 
     * @return the number of rows
     */
    public static ResultSetProcessor<Long> rowCount() {
        return new ResultSetProcessor<Long>() {
            @Override
            public Long process( ResultSet resultSet ) throws SQLException {
                long rows = 0L;
                while (resultSet.next()) {
                    ++rows;
                }
                return rows;
            }
        };
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains an {@link InputStream} to the binary value in the first column
     * of the next row in the result set.
     * 
     * @return the input stream to the binary value in the first column of the next row
     */
    public static ResultSetProcessor<InputStream> asStream() {
        return asStream(1);
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains an {@link InputStream} to the binary value in the specified
     * column of the next row in the result set.
     * 
     * @param columnNumber the 1-based column number
     * @return the input stream to the binary value in the specified column of the next row
     */
    public static ResultSetProcessor<InputStream> asStream( final int columnNumber ) {
        return new ResultSetProcessor<InputStream>() {
            @Override
            public InputStream process( ResultSet resultSet ) throws SQLException {
                return resultSet.next() ? resultSet.getBinaryStream(columnNumber) : null;
            }
        };
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains the string value in the first column of the next row in the
     * result set.
     * 
     * @return the string value in the first column of the next row
     */
    public static ResultSetProcessor<String> asString() {
        return asString(1);
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains the long value in the specified column of the next row in the
     * result set.
     * 
     * @param columnNumber the 1-based column number
     * @return the long value in the specified column of the next row
     */
    public static ResultSetProcessor<Long> asLong( final int columnNumber ) {
        return new ResultSetProcessor<Long>() {
            @Override
            public Long process( ResultSet resultSet ) throws SQLException {
                return resultSet.next() ? resultSet.getLong(columnNumber) : null;
            }
        };
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains the string value in the specified column of the next row in the
     * result set.
     * 
     * @param columnNumber the 1-based column number
     * @return the string value in the specified column of the next row
     */
    public static ResultSetProcessor<String> asString( final int columnNumber ) {
        return new ResultSetProcessor<String>() {
            @Override
            public String process( ResultSet resultSet ) throws SQLException {
                return resultSet.next() ? resultSet.getString(columnNumber) : null;
            }
        };
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains the string value in the first column of each row in the result
     * set.
     * 
     * @return the string values in the first column of the result set
     */
    public static ResultSetProcessor<List<String>> asStringList() {
        return asStringList(1);
    }

    /**
     * Obtain a {@link ResultSetProcessor processor} that obtains the string value in the specified column of each row in the
     * result set.
     * 
     * @param columnNumber the 1-based column number
     * @return the string values in the specified column of the result set
     */
    public static ResultSetProcessor<List<String>> asStringList( final int columnNumber ) {
        return new ResultSetProcessor<List<String>>() {
            @Override
            public List<String> process( ResultSet resultSet ) throws SQLException {
                List<String> result = new ArrayList<String>();
                while (resultSet.next()) {
                    result.add(resultSet.getString(columnNumber));
                }
                return result;
            }
        };
    }

    public <T> Iterable<Collection<T>> determineBatchesOf( Iterator<T> values ) {
        return new Batched<T>(values);
    }

    protected static final class Batched<T> implements Iterable<Collection<T>> {

        public static final int SINGLE_BATCH = 1;
        public static final int SMALL_BATCH = 4;
        public static final int MEDIUM_BATCH = 11;
        public static final int LARGE_BATCH = 51;

        private final ArrayList<T> currentBatch = new ArrayList<T>();
        private final Iterator<T> values;
        protected Collection<T> nextBatch;

        protected Batched( Iterator<T> values ) {
            this.values = values;
        }

        @Override
        public Iterator<Collection<T>> iterator() {
            return new Iterator<Collection<T>>() {
                @Override
                public boolean hasNext() {
                    findNextBatch();
                    return nextBatch != null;
                }

                @Override
                public Collection<T> next() {
                    findNextBatch();
                    if (nextBatch == null) throw new NoSuchElementException();
                    try {
                        return nextBatch;
                    } finally {
                        nextBatch = null;
                    }
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        protected void findNextBatch() {
            if (nextBatch != null) return;

            // If there are at least some in the current batch ...
            if (currentBatch.isEmpty()) {
                // We stopped at the largest batch size and consumed all of the 'currentBatch' values,
                // so just start reading more ...
                for (int i = 0; i != LARGE_BATCH; ++i) {
                    if (!this.values.hasNext()) break;
                    currentBatch.add(this.values.next());
                }
            } else {
                // Otherwise, we still have some in the 'currentBatch', which means we used a sublist last time and we processed
                // all of the remaining values ...
                assert currentBatch.size() < LARGE_BATCH;
            }

            if (currentBatch.size() == LARGE_BATCH) {
                nextBatch = currentBatch;
            } else if (currentBatch.size() > MEDIUM_BATCH) {
                nextBatch = currentBatch.subList(0, MEDIUM_BATCH);
            } else if (currentBatch.size() > SMALL_BATCH) {
                nextBatch = currentBatch.subList(0, SMALL_BATCH);
            } else {
                nextBatch = currentBatch.subList(0, SINGLE_BATCH);
            }
        }
    }
}
