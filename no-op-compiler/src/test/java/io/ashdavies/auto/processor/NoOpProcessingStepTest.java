package io.ashdavies.auto.processor;

import io.ashdavies.auto.AutoNoOp;

public class NoOpProcessingStepTest extends AnnotationCompilerTest<AutoNoOp> {

  NoOpProcessingStepTest() {
    super(new NoOpProcessor(), AutoNoOp.class);
  }
}
