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
import org.schematica.json.spi.JsonProvider;

/**
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public class SchematicaJsonProvider extends JsonProvider {

    private final javax.json.spi.JsonProvider defaultProvider;

    public SchematicaJsonProvider() {
        defaultProvider = javax.json.spi.JsonProvider.provider();
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new SchematicaObjectBuilder(defaultProvider.createObjectBuilder());
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new SchematicaArrayBuilder(defaultProvider.createArrayBuilder());
    }

    @Override
    public JsonParser createParser( Reader reader ) {
        return defaultProvider.createParser(reader);
    }

    @Override
    public JsonParser createParser( InputStream in ) {
        return defaultProvider.createParser(in);
    }

    @Override
    public JsonParserFactory createParserFactory( Map<String, ?> config ) {
        return defaultProvider.createParserFactory(config);
    }

    @Override
    public JsonGenerator createGenerator( Writer writer ) {
        return defaultProvider.createGenerator(writer);
    }

    @Override
    public JsonGenerator createGenerator( OutputStream out ) {
        return defaultProvider.createGenerator(out);
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory( Map<String, ?> config ) {
        return defaultProvider.createGeneratorFactory(config);
    }

    @Override
    public JsonReader createReader( Reader reader ) {
        return defaultProvider.createReader(reader);
    }

    @Override
    public JsonReader createReader( InputStream in ) {
        return defaultProvider.createReader(in);
    }

    @Override
    public JsonWriter createWriter( Writer writer ) {
        return defaultProvider.createWriter(writer);
    }

    @Override
    public JsonWriter createWriter( OutputStream out ) {
        return defaultProvider.createWriter(out);
    }

    @Override
    public JsonWriterFactory createWriterFactory( Map<String, ?> config ) {
        return defaultProvider.createWriterFactory(config);
    }

    @Override
    public JsonReaderFactory createReaderFactory( Map<String, ?> config ) {
        return defaultProvider.createReaderFactory(config);
    }

    @Override
    public JsonBuilderFactory createBuilderFactory( Map<String, ?> config ) {
        return defaultProvider.createBuilderFactory(config);
    }
}
