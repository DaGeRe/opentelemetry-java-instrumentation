/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.awssdk.v2_2;

import static io.opentelemetry.javaagent.tooling.ClassLoaderMatcher.hasClassesNamed;

import com.google.auto.service.AutoService;
import io.opentelemetry.javaagent.tooling.InstrumentationModule;
import io.opentelemetry.javaagent.tooling.TypeInstrumentation;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.matcher.ElementMatcher;

@AutoService(InstrumentationModule.class)
public class AwsSdkInstrumentationModule extends InstrumentationModule {
  public AwsSdkInstrumentationModule() {
    super("aws-sdk", "aws-sdk-2.2");
  }

  @Override
  public String[] additionalHelperClassNames() {
    return new String[] {
      "io.opentelemetry.javaagent.instrumentation.awssdk.v2_2.TracingExecutionInterceptor",
      "io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdk",
      "io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkHttpClientTracer",
      "io.opentelemetry.instrumentation.awssdk.v2_2.AwsSdkInjectAdapter",
      "io.opentelemetry.instrumentation.awssdk.v2_2.RequestType",
      "io.opentelemetry.instrumentation.awssdk.v2_2.SdkRequestDecorator",
      "io.opentelemetry.instrumentation.awssdk.v2_2.DbRequestDecorator",
      "io.opentelemetry.instrumentation.awssdk.v2_2.TracingExecutionInterceptor",
      "io.opentelemetry.extension.trace.propagation.AwsXRayPropagator"
    };
  }

  /**
   * Injects resource file with reference to our {@link TracingExecutionInterceptor} to allow SDK's
   * service loading mechanism to pick it up.
   */
  @Override
  public String[] helperResourceNames() {
    return new String[] {
      "software/amazon/awssdk/global/handlers/execution.interceptors",
    };
  }

  @Override
  public ElementMatcher.Junction<ClassLoader> classLoaderMatcher() {
    // We don't actually transform it but want to make sure we only apply the instrumentation when
    // our key dependency is present.
    return hasClassesNamed("software.amazon.awssdk.core.interceptor.ExecutionInterceptor");
  }

  @Override
  public List<TypeInstrumentation> typeInstrumentations() {
    return Collections.singletonList(new AwsSdkInitializationInstrumentation());
  }
}
