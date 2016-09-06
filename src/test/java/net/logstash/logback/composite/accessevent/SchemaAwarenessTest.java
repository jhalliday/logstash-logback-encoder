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

import ch.qos.logback.access.spi.IAccessEvent;

import net.logstash.logback.composite.BaseSchemaAwarenessTest;
import net.logstash.logback.composite.LogstashVersionJsonProvider;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite to validate that output values from access event providers is consistent with their self-declared schema.
 */
public class SchemaAwarenessTest extends BaseSchemaAwarenessTest {

    @Test
    public void testAccessEventFormattedTimestampJsonProvider() {
        validateStaticSchemaProvider(new AccessEventFormattedTimestampJsonProvider(), getEvent());
    }

    @Test
    public void testLogstashVersionJsonProvider() {
        validateStaticSchemaProvider(new LogstashVersionJsonProvider<IAccessEvent>(), getEvent());
    }

    @Test
    public void testAccessMessageJsonProvider() {
        validateStaticSchemaProvider(new AccessMessageJsonProvider(), getEvent());
    }

    @Test
    public void testMethodJsonProvider() {
        validateStaticSchemaProvider(new MethodJsonProvider(), getEvent());
    }

    @Test
    public void testProtocolJsonProvider() {
        validateStaticSchemaProvider(new ProtocolJsonProvider(), getEvent());
    }

    @Test
    public void testStatusCodeJsonProvider() {
        validateStaticSchemaProvider(new StatusCodeJsonProvider(), getEvent());
    }

    @Test
    public void testRequestedUrlJsonProvider() {
        validateStaticSchemaProvider(new RequestedUrlJsonProvider(), getEvent());
    }

    @Test
    public void testRequestedUriJsonProvider() {
        validateStaticSchemaProvider(new RequestedUriJsonProvider(), getEvent());
    }

    @Test
    public void testRemoteHostJsonProvider() {
        validateStaticSchemaProvider(new RemoteHostJsonProvider(), getEvent());
    }

    @Test
    public void testHostnameJsonProvider() {
        validateStaticSchemaProvider(new HostnameJsonProvider(), getEvent());
    }

    @Test
    public void testRemoteUserJsonProvider() {
        validateStaticSchemaProvider(new RemoteUserJsonProvider(), getEvent());
    }

    @Test
    public void testContentLengthJsonProvider() {
        validateStaticSchemaProvider(new ContentLengthJsonProvider(), getEvent());
    }

    @Test
    public void testElapsedTimeJsonProvider() {
        validateStaticSchemaProvider(new ElapsedTimeJsonProvider(), getEvent());
    }

    @Test
    public void testRequestHeadersJsonProvider() {
        RequestHeadersJsonProvider requestHeadersJsonProvider = new RequestHeadersJsonProvider();
        requestHeadersJsonProvider.setFieldName("@fields.request_headers");
        validateStaticSchemaProvider(requestHeadersJsonProvider, getEvent());
    }

    @Test
    public void testResponseHeadersJsonProvider() {
        ResponseHeadersJsonProvider responseHeadersJsonProvider = new ResponseHeadersJsonProvider();
        responseHeadersJsonProvider.setFieldName("@fields.response_headers");
        validateStaticSchemaProvider(responseHeadersJsonProvider, getEvent());
    }


    private IAccessEvent getEvent() {

        IAccessEvent event = mock(IAccessEvent.class);

        when(event.getRemoteHost()).thenReturn("remoteHost");
        when(event.getRemoteUser()).thenReturn("remoteUser");
        when(event.getProtocol()).thenReturn("protocol");
        when(event.getMethod()).thenReturn("method");
        when(event.getRequestURI()).thenReturn("uri");
        when(event.getRequestURL()).thenReturn("url");

        Map<String, String> requestHeaders = new LinkedHashMap<String, String>();
        requestHeaders.put("key", "value");

        when(event.getRequestHeaderMap()).thenReturn(requestHeaders);
        when(event.getResponseHeaderMap()).thenReturn(requestHeaders);

        return event;
    }
}
