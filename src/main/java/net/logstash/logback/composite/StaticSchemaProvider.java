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

/**
 * Provides self-describing capability for JsonProviders
 * whose output structure can be determined entirely by configuration.
 */
public interface StaticSchemaProvider<Event extends DeferredProcessingAware> extends JsonProvider<Event> {

    /**
     * Adds a description of field(s) output by this Provider to the overall schema for the composed event record.
     * A provider may add zero or more properties of various types, depending on its runtime configuration.
     *
     * @param topLevelSchema The ObjectSchema for the [Logging|Access]Event as a whole.
     */
    void addToSchema(ObjectSchema topLevelSchema);
}
