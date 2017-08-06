package io.ashdavies.auto.processor;

import io.ashdavies.auto.AutoDecorator;

public class DecoratorProcessorTest extends AnnotationCompilerTest<AutoDecorator> {

  public DecoratorProcessorTest() {
    super(new DecoratorProcessor(), AutoDecorator.class);
  }
}
