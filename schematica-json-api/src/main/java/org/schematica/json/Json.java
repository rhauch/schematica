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

package org.schematica.json;

import static org.schematica.json.spi.JsonProvider.PROVIDER_INSTANCE;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import javax.json.JsonBuilderFactory;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class Json  {

    /**
     * Creates a JSON object builder
     *
     * @return a JSON object builder
     */
    public static JsonObjectBuilder createObjectBuilder() {
        return PROVIDER_INSTANCE.createObjectBuilder();
    }

    /**
     * Creates a JSON array builder
     *
     * @return a JSON array builder
     */
    public static JsonArrayBuilder createArrayBuilder() {
        return PROVIDER_INSTANCE.createArrayBuilder();
    }


    /**
     * Creates a JSON parser from a character stream.
     *
     * @param reader i/o reader from which JSON is to be read
     * @return a JSON parser
     */
    public static JsonParser createParser(Reader reader) {
        return PROVIDER_INSTANCE.createParser(reader);
    }

    /**
     * Creates a JSON parser from a byte stream.
     * The character encoding of the stream is determined as specified in 
     * <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
     *
     * @param in i/o stream from which JSON is to be read
     * @throws javax.json.JsonException if encoding cannot be determined
     *         or i/o error (IOException would be cause of JsonException)
     * @return a JSON parser
     */
    public static JsonParser createParser(InputStream in) {
        return PROVIDER_INSTANCE.createParser(in);
    }

    /**
     * Creates a JSON generator for writing JSON to a character stream.
     *
     * @param writer a i/o writer to which JSON is written
     * @return a JSON generator
     */
    public static JsonGenerator createGenerator(Writer writer) {
        return PROVIDER_INSTANCE.createGenerator(writer);
    }

    /**
     * Creates a JSON generator for writing JSON to a byte stream.
     *
     * @param out i/o stream to which JSON is written
     * @return a JSON generator
     */
    public static JsonGenerator createGenerator(OutputStream out) {
        return PROVIDER_INSTANCE.createGenerator(out);
    }

    /**
     * Creates a parser factory for creating {@link JsonParser} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON parsers. The map may be empty or null
     * @return JSON parser factory
     */
    public static JsonParserFactory createParserFactory(Map<String, ?> config) {
        return PROVIDER_INSTANCE.createParserFactory(config);
    }

    /**
     * Creates a generator factory for creating {@link JsonGenerator} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON generators. The map may be empty or null
     * @return JSON generator factory
     */
    public static JsonGeneratorFactory createGeneratorFactory(
            Map<String, ?> config) {
        return PROVIDER_INSTANCE.createGeneratorFactory(config);
    }

    /**
     * Creates a JSON writer to write a
     * JSON {@link javax.json.JsonObject object} or {@link javax.json.JsonArray array}
     * structure to the specified character stream.
     *
     * @param writer to which JSON object or array is written
     * @return a JSON writer
     */
    public static JsonWriter createWriter(Writer writer) {
        return PROVIDER_INSTANCE.createWriter(writer);
    }

    /**
     * Creates a JSON writer to write a
     * JSON {@link javax.json.JsonObject object} or {@link javax.json.JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     *
     * @param out to which JSON object or array is written
     * @return a JSON writer
     */
    public static JsonWriter createWriter(OutputStream out) {
        return PROVIDER_INSTANCE.createWriter(out);
    }

    /**
     * Creates a JSON reader from a character stream.
     *
     * @param reader a reader from which JSON is to be read
     * @return a JSON reader
     */
    public static JsonReader createReader(Reader reader) {
        return PROVIDER_INSTANCE.createReader(reader);
    }

    /**
     * Creates a JSON reader from a byte stream. The character encoding of
     * the stream is determined as described in
     * <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
     *
     * @param in a byte stream from which JSON is to be read
     * @return a JSON reader
     */
    public static JsonReader createReader(InputStream in) {
        return PROVIDER_INSTANCE.createReader(in);
    }

    /**
     * Creates a reader factory for creating {@link JsonReader} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON readers. The map may be empty or null
     * @return a JSON reader factory
     */
    public static JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return PROVIDER_INSTANCE.createReaderFactory(config);
    }

    /**
     * Creates a writer factory for creating {@link JsonWriter} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON writers. The map may be empty or null
     * @return a JSON writer factory
     */
    public static JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return PROVIDER_INSTANCE.createWriterFactory(config);
    }

    /**
     * Creates a builder factory for creating {@link javax.json.JsonArrayBuilder}
     * and {@link javax.json.JsonObjectBuilder} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON builders. The map may be empty or null
     * @return a JSON builder factory
     */
    public static JsonBuilderFactory createBuilderFactory(
            Map<String, ?> config) {
        return PROVIDER_INSTANCE.createBuilderFactory(config);
    }
}
