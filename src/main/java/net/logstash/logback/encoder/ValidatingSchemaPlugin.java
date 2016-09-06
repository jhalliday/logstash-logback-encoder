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
package net.logstash.logback.encoder;

import ch.qos.logback.core.spi.DeferredProcessingAware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

import net.logstash.logback.composite.JsonProvider;
import net.logstash.logback.composite.JsonProviders;
import net.logstash.logback.composite.StaticSchemaProvider;

import java.io.File;
import java.io.IOException;

/**
 * SchemaPlugin implementation which validates that the output schema from the runtime logging configuration matches that from a provided schema file.
 * <br>
 * Example usage:
 * <br>
 *     <pre>
 * &lt;encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder"&gt;
 *   ...
 *   &lt;schemaPlugin class="net.logstash.logback.encoder.ValidatingSchemaPlugin"&gt;
 *     &lt;validationFile&gt;/tmp/schema.json&lt;/validationFile&gt;
 *   &lt;/schemaPlugin&gt;
 * &lt;/encoder&gt;
 *     </pre>
 */
public class ValidatingSchemaPlugin<Event extends DeferredProcessingAware> implements SchemaPlugin<Event> {

    private String validationFile;

    public String getValidationFile() {
        return validationFile;
    }

    public void setValidationFile(String validationFile) {
        this.validationFile = validationFile;
    }

    @Override
    public void init(JsonProviders<Event> jsonProviders) {

        final ObjectSchema topLevelSchema = new ObjectSchema();
        topLevelSchema.rejectAdditionalProperties();

        for(JsonProvider<Event> jsonProvider : jsonProviders.getProviders()) {

            if(!(jsonProvider instanceof StaticSchemaProvider)) {
                throw new IllegalArgumentException("configured provider "+jsonProvider.getClass().getName()+" is not schema aware");
            }

            final StaticSchemaProvider<Event> staticSchemaProvider = (StaticSchemaProvider<Event>)jsonProvider;
            staticSchemaProvider.addToSchema(topLevelSchema);
        }

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectSchema readSchema = objectMapper.readValue(new File(validationFile), ObjectSchema.class);

            if(!topLevelSchema.equals(readSchema)) {
                throw new IllegalStateException("runtime configured schema does not match provided validation schema from "+validationFile);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
