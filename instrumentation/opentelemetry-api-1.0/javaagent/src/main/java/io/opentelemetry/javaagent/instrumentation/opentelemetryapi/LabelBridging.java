/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.opentelemetryapi;

import io.opentelemetry.api.common.Labels;
import io.opentelemetry.api.common.LabelsBuilder;

/**
 * This class converts between Labels class that application brings and Labels class that agent
 * uses.
 *
 * <p>TODO probably not the most performant solution...
 */
public class LabelBridging {

  public static Labels toAgent(application.io.opentelemetry.api.common.Labels applicationLabels) {
    LabelsBuilder builder = Labels.builder();
    applicationLabels.forEach(builder::put);
    return builder.build();
  }
}
