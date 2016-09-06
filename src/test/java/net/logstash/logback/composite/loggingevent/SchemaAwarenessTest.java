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
package net.logstash.logback.composite.loggingevent;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;

import net.logstash.logback.composite.BaseSchemaAwarenessTest;
import net.logstash.logback.composite.LogstashVersionJsonProvider;

import org.junit.Test;

import org.slf4j.MarkerFactory;

/**
 * Test suite to validate that output values from logging event providers is consistent with their self-declared schema.
 */
public class SchemaAwarenessTest extends BaseSchemaAwarenessTest {

    @Test
    public void testLoggingEventFormattedTimestampJsonProvider() {
        validateStaticSchemaProvider(new LoggingEventFormattedTimestampJsonProvider(), getEvent());
    }

    @Test
    public void testLogstashVersionJsonProvider() {
        validateStaticSchemaProvider(new LogstashVersionJsonProvider<ILoggingEvent>(), getEvent());
    }

    @Test
    public void testMessageJsonProvider() {
        validateStaticSchemaProvider(new MessageJsonProvider(), getEvent());
    }

    @Test
    public void testLoggerNameJsonProvider() {
        validateStaticSchemaProvider(new LoggerNameJsonProvider(), getEvent());
    }

    @Test
    public void testThreadNameJsonProvider() {
        validateStaticSchemaProvider(new ThreadNameJsonProvider(), getEvent());
    }

    @Test
    public void testLogLevelJsonProvider() {
        validateStaticSchemaProvider(new LogLevelJsonProvider(), getEvent());
    }

    @Test
    public void testLogLevelValueJsonProvider() {
        validateStaticSchemaProvider(new LogLevelValueJsonProvider(), getEvent());
    }

    @Test
    public void testCallerDataJsonProvider() {
        validateStaticSchemaProvider(new CallerDataJsonProvider(), getEvent());
    }

    @Test
    public void testStackTraceJsonProvider() {
        validateStaticSchemaProvider(new StackTraceJsonProvider(), getEvent());
    }

    @Test
    public void testContextNameJsonProvider() {
        validateStaticSchemaProvider(new ContextNameJsonProvider(), getEvent());
    }

    @Test
    public void testJsonMessageJsonProvider() {
        LoggingEvent event = getEvent();
        event.setMarker(MarkerFactory.getMarker("JSON"));
        validateStaticSchemaProvider(new JsonMessageJsonProvider(), event);
    }

    @Test
    public void testTagsJsonProvider() {
        LoggingEvent event = getEvent();
        event.setMarker(MarkerFactory.getMarker("tags"));
        validateStaticSchemaProvider(new TagsJsonProvider(), event);
    }


    private LoggingEvent getEvent() {
        LoggingEvent event = new LoggingEvent("fqcn", LOGGER, Level.DEBUG, "message", new RuntimeException(), new Object[]{"arg"});
        event.setCallerData(Thread.currentThread().getStackTrace());
        event.setTimeStamp(System.currentTimeMillis());
        return event;
    }
}
