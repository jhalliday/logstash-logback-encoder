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

import java.io.IOException;

import net.logstash.logback.CachingAbbreviator;
import net.logstash.logback.NullAbbreviator;
import net.logstash.logback.composite.*;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.TargetLengthBasedClassNameAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.fasterxml.jackson.core.JsonGenerator;

public class LoggerNameJsonProvider extends AbstractSchemaAwareFieldJsonProvider<ILoggingEvent> implements FieldNamesAware<LogstashFieldNames> {

    public static final String FIELD_LOGGER_NAME = "logger_name";

    /**
     * When set to anything >= 0 we will try to abbreviate the logger name
     */
    private int shortenedLoggerNameLength = -1;

    /**
     * Abbreviator that will shorten the logger classname if shortenedLoggerNameLength is set
     */
    private Abbreviator abbreviator = NullAbbreviator.INSTANCE;
    
    public LoggerNameJsonProvider() {
        setFieldName(FIELD_LOGGER_NAME);
    }
    
    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        JsonWritingUtils.writeStringField(generator, getFieldName(), abbreviator.abbreviate(event.getLoggerName()));
    }
    
    @Override
    public void setFieldNames(LogstashFieldNames fieldNames) {
        setFieldName(fieldNames.getLogger());
    }

    public int getShortenedLoggerNameLength() {
        return shortenedLoggerNameLength;
    }

    public void setShortenedLoggerNameLength(int length) {
        this.shortenedLoggerNameLength = length;
        if (length >= 0) {
            abbreviator = new CachingAbbreviator(new TargetLengthBasedClassNameAbbreviator(this.shortenedLoggerNameLength));
        } else {
            abbreviator = NullAbbreviator.INSTANCE;
        }
    }

}
