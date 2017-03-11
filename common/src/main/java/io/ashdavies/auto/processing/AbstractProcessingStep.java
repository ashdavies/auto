package io.ashdavies.auto.processing;

import com.google.auto.common.BasicAnnotationProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

abstract class AbstractProcessingStep implements BasicAnnotationProcessor.ProcessingStep {

  private final ProcessingEnvironment environment;

  AbstractProcessingStep(ProcessingEnvironment environment) {
    this.environment = environment;
  }

  Messager getMessager() {
    return environment.getMessager();
  }

  Filer getFiler() {
    return environment.getFiler();
  }

  Elements getElementUtils() {
    return environment.getElementUtils();
  }

  Types getTypeUtils() {
    return environment.getTypeUtils();
  }
}
