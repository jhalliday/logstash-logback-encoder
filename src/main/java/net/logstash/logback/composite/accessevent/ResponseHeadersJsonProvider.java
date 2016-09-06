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
package net.logstash.logback.composite.accessevent;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

import ch.qos.logback.access.spi.IAccessEvent;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import net.logstash.logback.composite.*;
import net.logstash.logback.fieldnames.LogstashAccessFieldNames;

public class ResponseHeadersJsonProvider extends AbstractSchemaAwareFieldJsonProvider<IAccessEvent> implements FieldNamesAware<LogstashAccessFieldNames> {

    /**
     * When true, names of headers will be written to JSON output in lowercase. 
     */
    private boolean lowerCaseHeaderNames;

    @Override
    public void writeTo(JsonGenerator generator, IAccessEvent event) throws IOException {
        JsonWritingUtils.writeMapStringFields(generator, getFieldName(), event.getResponseHeaderMap(), lowerCaseHeaderNames);
    }
    
    @Override
    public void setFieldNames(LogstashAccessFieldNames fieldNames) {
        setFieldName(fieldNames.getFieldsResponseHeaders());
    }

    public boolean getLowerCaseHeaderNames() {
        return lowerCaseHeaderNames;
    }

    public void setLowerCaseHeaderNames(boolean lowerCaseHeaderNames) {
        this.lowerCaseHeaderNames = lowerCaseHeaderNames;
    }

    @Override
    public void addToSchema(ObjectSchema topLevelSchema) {
        JsonWritingUtils.addToSchema(topLevelSchema, getFieldName(), new ObjectSchema());
    }
}
