/*
 * Schematica (http://www.schematica.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.schematica.api;

import java.util.Date;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public interface DocumentEditor {

    /**
     * Set the value for the field with the given name to the supplied boolean value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setBoolean( String name,
                               boolean value );

    /**
     * Set the value for the field with the given name to the supplied integer value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setInt( String name,
                           int value );

    /**
     * Set the value for the field with the given name to the supplied long value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setLong( String name,
                            long value );

    /**
     * Set the value for the field with the given name to the supplied float value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setFloat( String name,
                             float value );

    /**
     * Set the value for the field with the given name to the supplied double value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setDouble( String name,
                              double value );

    /**
     * Set the value for the field with the given name to the supplied string value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setString( String name,
                              String value );


    /**
     * Set the value for the field with the given name to the supplied date value.
     *
     * @param name The name of the field
     * @param value the new value for the field
     * @return This document, to allow for chaining methods
     */
    DocumentEditor setDate( String name,
                            Date value );

    /**
     * Set the value for the field with the given name to be the supplied Document.
     *
     * @param name The name of the field
     * @param document the document
     * @return The document that was just set as the value for the named field; never null and may or may not be the same
     * instance as the supplied <code>document</code>.
     */
    DocumentEditor setDocument( String name,
                                Document document );

    /**
     * Set the value for the field with the given name to be the supplied array.
     *
     * @param name The name of the field
     * @param values the (valid) values for the array
     * @return The array that was just set as the value for the named field; never null and may or may not be the same
     * instance as the supplied <code>array</code>.
     */
    <T> DocumentEditor setArray( String name,
                                 T... values );

    ChangeSet changeSet();

    Document unwrap();
}
