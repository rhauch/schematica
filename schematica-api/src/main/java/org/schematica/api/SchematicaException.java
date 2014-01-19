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

/**
 * The top-level exception for Schematica.
 * 
 * @author Randall Hauch (rhauch@redhat.com)
 */
public class SchematicaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create an exception with the supplied message.
     * 
     * @param message the message
     */
    public SchematicaException( String message ) {
        super(message);
    }

    /**
     * /** Create an exception with the supplied cause.
     * 
     * @param cause the cause
     */
    public SchematicaException( Throwable cause ) {
        super(cause);

    }

    /**
     * Create an exception with the supplied message.
     * 
     * @param message the message
     * @param cause the cause
     */
    public SchematicaException( String message,
                                Throwable cause ) {
        super(message, cause);

    }
}
