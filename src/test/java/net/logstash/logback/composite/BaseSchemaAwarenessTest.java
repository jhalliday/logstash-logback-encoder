/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.logstash.logback.composite;

import ch.qos.logback.classic.Logger;

import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import com.github.fge.jsonschema.main.JsonValidator;

import org.junit.Assert;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Foundation utility methods for test suites which validate that output values from event providers is consistent with their self-declared schema.
 */
public class BaseSchemaAwarenessTest {

    protected static final Logger LOGGER = (Logger) LoggerFactory.getLogger(BaseSchemaAwarenessTest.class);

    private static final JsonFactory FACTORY = new MappingJsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ValidationConfiguration VALIDATION_CONFIGURATION = ValidationConfiguration.newBuilder().setDefaultVersion(SchemaVersion.DRAFTV3).freeze();
    private static final JsonValidator VALIDATOR = JsonSchemaFactory.newBuilder().setValidationConfiguration(VALIDATION_CONFIGURATION).freeze().getValidator();

    protected <Event extends DeferredProcessingAware> String writeLogAsString(JsonProvider<Event> provider, Event event) throws IOException {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = FACTORY.createGenerator(writer);
        generator.writeStartObject();
        provider.writeTo(generator, event);
        generator.writeEndObject();
        generator.flush();
        String logString = writer.toString();
        return logString;
    }

    protected <Event extends DeferredProcessingAware> void validateStaticSchemaProvider(StaticSchemaProvider<Event> provider, Event event) {

        provider.start();

        try {

            String logString = writeLogAsString(provider, event);

            // don't allow empty output, as it doesn't tell us anything useful about the data types
            Assert.assertTrue(logString.length() > 2);

            ObjectSchema topLevelSchema = new ObjectSchema();
            topLevelSchema.rejectAdditionalProperties();
            provider.addToSchema(topLevelSchema);

            // we need a schema
            JsonNode schemaNode = MAPPER.convertValue(topLevelSchema, JsonNode.class);

            // and we need an example output document
            JsonNode instance = MAPPER.readValue(logString, JsonNode.class);

            // check the exemplar conforms to the schema
            ProcessingReport processingReport = VALIDATOR.validate(schemaNode, instance, true);

            Assert.assertTrue(provider.getClass().getName() + ": " + processingReport.toString(), processingReport.isSuccess());

        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }
}
