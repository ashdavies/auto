package io.ashdavies.auto.processor;

import io.ashdavies.auto.AutoNoOp;

public class NoOpProcessorTest extends AnnotationCompilerTest<AutoNoOp> {

  public NoOpProcessorTest() {
    super(new NoOpProcessor(), AutoNoOp.class);
  }
}
