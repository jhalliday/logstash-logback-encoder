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

import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;

/**
 * Provides default schema awareness for field providers, assuming a String data type.
 * Field providers using other types may override this or implement StaticSchemaProvider directly.
 */
public abstract class AbstractSchemaAwareFieldJsonProvider<Event extends DeferredProcessingAware> extends AbstractFieldJsonProvider<Event> implements StaticSchemaProvider<Event> {

    @Override
    public void addToSchema(ObjectSchema topLevelSchema) {
        JsonWritingUtils.addToSchema(topLevelSchema, getFieldName(), new StringSchema());
    }
}
