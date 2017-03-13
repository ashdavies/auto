package io.ashdavies.auto.processor;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;
import java.util.Collections;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;

@AutoService(Processor.class)
public class NoOpProcessor extends BasicAnnotationProcessor {

  @Override
  protected Iterable<? extends ProcessingStep> initSteps() {
    return Collections.singletonList(new NoOpProcessingStep(processingEnv));
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
