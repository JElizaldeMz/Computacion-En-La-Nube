package com.formacionbdi.springboot.app.zuul.filters;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ItemsFallback implements FallbackProvider {

    @Override
    public String getRoute() {
        return "servicio-items"; // nombre del servicio en Eureka
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }
            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }
            @Override
            public void close() {}
            @Override
            public InputStream getBody() throws IOException {
                String body = "{\"producto\":{\"id\":0,\"marca\":\"Sin marca - timeout Zuul\","
                        + "\"modelo\":\"Servicio lento, fallback de Zuul activado\","
                        + "\"precio\":0.0},\"cantidad\":1,\"total\":0.0}";
                return new ByteArrayInputStream(body.getBytes());
            }
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}