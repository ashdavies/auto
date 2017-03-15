package io.ashdavies.auto.processor;

import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.JavaFile;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static io.ashdavies.auto.processor.DiagnosticPrinter.with;

abstract class SingleProcessingStep<T extends Annotation> extends AbstractProcessingStep {

  SingleProcessingStep(ProcessingEnvironment environment) {
    super(environment);
  }

  @Override
  public Set<? extends Class<T>> annotations() {
    return Collections.singleton(annotation());
  }

  abstract Class<T> annotation();

  @Override
  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elements) {
    for (Element element : elements.get(annotation())) {
      try {
        EnrichedTypeElement qualified = EnrichedTypeElement.with(getElementUtils(), element);
        if (!valid(qualified)) {
          throw new AbortProcessingException();
        }

        process(qualified).writeTo(getFiler());
      } catch (Exception exception) {
        with(getMessager()).error(element, exception);
      }
    }

    return new HashSet<>();
  }

  abstract boolean valid(EnrichedTypeElement element);

  abstract JavaFile process(EnrichedTypeElement element);
}
