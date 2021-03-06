/*
 * Copyright The OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.instrumentation.spring.autoconfigure.exporters.jaeger;

import io.grpc.ManagedChannel;
import io.opentelemetry.exporters.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.instrumentation.spring.autoconfigure.TracerAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures {@link JaegerGrpcSpanExporter} for tracing.
 *
 * <p>Initializes {@link JaegerGrpcSpanExporter} bean if bean is missing.
 */
@Configuration
@AutoConfigureBefore(TracerAutoConfiguration.class)
@EnableConfigurationProperties(JaegerSpanExporterProperties.class)
@ConditionalOnProperty(
    prefix = "opentelemetry.trace.exporter.jaeger",
    name = "enabled",
    matchIfMissing = true)
@ConditionalOnClass({JaegerGrpcSpanExporter.class, ManagedChannel.class})
public class JaegerSpanExporterAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public JaegerGrpcSpanExporter otelJaegerSpanExporter(
      JaegerSpanExporterProperties jaegerSpanExporterProperties) {

    return JaegerGrpcSpanExporter.newBuilder()
        .setServiceName(jaegerSpanExporterProperties.getServiceName())
        .setDeadlineMs(jaegerSpanExporterProperties.getSpanTimeout().toMillis())
        .setEndpoint(jaegerSpanExporterProperties.getEndpoint())
        .build();
  }
}
