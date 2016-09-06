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
import net.logstash.logback.composite.JsonProviders;

/**
 * Extension API to allow customized handling of schema information at runtime.
 */
public interface SchemaPlugin<Event extends DeferredProcessingAware> {

    /**
     * Called by the Encoder after its own initialization, to allow the plugin to perform startup tasks.
     */
    void init(JsonProviders<Event> jsonProviders);
}
