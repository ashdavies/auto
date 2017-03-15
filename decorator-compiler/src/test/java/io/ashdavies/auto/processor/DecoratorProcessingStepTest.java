package io.ashdavies.auto.processor;

import io.ashdavies.auto.AutoDecorator;

public class DecoratorProcessingStepTest extends AnnotationCompilerTest<AutoDecorator> {

  public DecoratorProcessingStepTest() {
    super(new DecoratorProcessor(), AutoDecorator.class);
  }
}
