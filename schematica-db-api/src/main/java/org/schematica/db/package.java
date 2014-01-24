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

/**
 * Schematica is a simple library to store, update, merge and query JSON documents in a relational database. Schematica is ideal 
 * for services that send JSON to clients that then modify the document and send it back to the service, which must then 
 * update/merge the changes made by the client into the stored JSON document -- which may have changed since the client first saw
 * the document.
 * <p>
 * Schematica works with JSON documents via the <a href="https://jcp.org/en/jsr/detail?id=353">JSON-P standard API (JSR-353)</a>, 
 * and it even extends the standard API with useful behavior, including merging documents and support for BSON datatypes and its 
 * binary serialization format.
 * </p>
 */
package org.schematica.db;

