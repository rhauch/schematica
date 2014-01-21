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

package org.schematica.api;

import java.util.List;
import java.util.Set;
import javax.json.JsonObject;

/**
 * @author Randall Hauch (rhauch@redhat.com)
 */
public interface Document extends Iterable<Field> {

    String getKey();

    DocumentEditor edit();

    JsonObject getJsonObject();

    /**
     * Get the boolean value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The boolean field value, if found, or null if there is no such pair or if the value is not a boolean
     */
    Boolean getBoolean( String name );

    /**
     * Get the boolean value in this document for the given field name.
     *
     * @param name The name of the pair
     * @param defaultValue the default value to return if there is no such pair or if the value is not a boolean
     * @return The boolean field value if found, or <code>defaultValue</code> if there is no such pair or if the value is not a
     *         boolean
     */
    boolean getBoolean( String name,
                        boolean defaultValue );

    /**
     * Get the integer value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The integer field value, if found, or null if there is no such pair or if the value is not an integer
     */
    Integer getInteger( String name );

    /**
     * Get the integer value in this document for the given field name.
     *
     * @param name The name of the pair
     * @param defaultValue the default value to return if there is no such pair or if the value is not a integer
     * @return The integer field value if found, or <code>defaultValue</code> if there is no such pair or if the value is not a
     *         integer
     */
    int getInteger( String name,
                    int defaultValue );

    /**
     * Get the integer value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The long field value, if found, or null if there is no such pair or if the value is not a long value
     */
    Long getLong( String name );

    /**
     * Get the long value in this document for the given field name.
     *
     * @param name The name of the pair
     * @param defaultValue the default value to return if there is no such pair or if the value is not a long value
     * @return The long field value if found, or <code>defaultValue</code> if there is no such pair or if the value is not a long
     *         value
     */
    long getLong( String name,
                  long defaultValue );

    /**
     * Get the double value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The double field value, if found, or null if there is no such pair or if the value is not a double
     */
    Double getDouble( String name );

    /**
     * Get the double value in this document for the given field name.
     *
     * @param name The name of the pair
     * @param defaultValue the default value to return if there is no such pair or if the value is not a double
     * @return The double field value if found, or <code>defaultValue</code> if there is no such pair or if the value is not a
     *         double
     */
    double getDouble( String name,
                      double defaultValue );

    /**
     * Get the string value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The string field value, if found, or null if there is no such pair or if the value is not a string
     */
    String getString( String name );

    /**
     * Get the string value in this document for the given field name.
     *
     * @param name The name of the pair
     * @param defaultValue the default value to return if there is no such pair or if the value is not a string
     * @return The string field value if found, or <code>defaultValue</code> if there is no such pair or if the value is not a
     *         string
     */
    String getString( String name,
                      String defaultValue );

    /**
     * Get the array value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The array field value (as a list), if found, or null if there is no such pair or if the value is not an array
     */
    <T> T[] getArray( String name, Class<T> clazz);

    /**
     * Get the array value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The array field value (as a list), if found, or null if there is no such pair or if the value is not an array
     */
    <T> List<T> getArrayAsList( String name, Class<T> clazz);

    /**
     * Get the existing array value in this document for the given field name, or create a new array if there is no existing array
     * at this field.
     *
     * @param name The name of the pair
     * @return The editable array field value; never null
     */
    <T> T[] getOrCreateArray( String name,
                              Class<T> clazz );

    /**
     * Get the existing array value in this document for the given field name, or create a new array if there is no existing array
     * at this field.
     *
     * @param name The name of the pair
     * @return The editable array field value; never null
     */
    <T> List<T> getOrCreateArrayAsList( String name,
                                        Class<T> clazz );

    /**
     * Get the document value in this document for the given field name.
     *
     * @param name The name of the pair
     * @return The document field value, if found, or null if there is no such pair or if the value is not a document
     */
    Document getDocument( String name );

    /**
     * Get the existing document value in this document for the given field name, or create a new document if there is no existing
     * document at this field.
     *
     * @param name The name of the pair
     * @return The document field value; never null
     */
    Document getOrCreateDocument( String name );

    /**
     * Determine whether this object has a pair with the given the name and the value is null. This is equivalent to calling:
     *
     * <pre>
     * this.get(name) instanceof Null;
     * </pre>
     *
     * @param name The name of the pair
     * @return <code>true</code> if the field has been set to a {@code null} value, or false otherwise
     */
    boolean isNull( String name );

    /**
     * Checks if this object contains a field with the given name.
     *
     * @param name The name of the pair for which to check
     * @return true if this document contains a field with the supplied name, or false otherwise
     */
    boolean containsField( String name );

    /**
     * Returns this object's fields' names
     *
     * @return The names of the fields in this object
     */
    Set<String> keySet();

    /**
     * Return the number of name-value pairs in this object.
     *
     * @return the number of name-value pairs; never negative
     */
    int size();

    /**
     * Return whether this document contains no fields and is therefore empty.
     *
     * @return true if there are no fields in this document, or false if there is at least one.
     */
    boolean isEmpty();
}
