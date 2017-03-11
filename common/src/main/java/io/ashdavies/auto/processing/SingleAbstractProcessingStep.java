package io.ashdavies.auto.processing;

import com.google.common.collect.SetMultimap;
import com.squareup.javapoet.JavaFile;
import io.ashdavies.auto.diagnostic.ProcessingException;
import io.ashdavies.auto.element.QualifiedTypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import static io.ashdavies.auto.diagnostic.DiagnosticPrinter.with;

abstract class SingleAbstractProcessingStep extends AbstractProcessingStep {

  SingleAbstractProcessingStep(ProcessingEnvironment environment) {
    super(environment);
  }

  @Override
  public Set<? extends Class<? extends Annotation>> annotations() {
    return Collections.singleton(annotation());
  }

  abstract Class<? extends Annotation> annotation();

  @Override
  public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elements) {
    for (Element element : elements.get(annotation())) {
      try {
        process(element).writeTo(getFiler());
      } catch (IOException | ProcessingException exception) {
        with(getMessager()).error(element, exception);
      }
    }

    return new HashSet<>();
  }

  private JavaFile process(Element element) throws ProcessingException {
    return process(QualifiedTypeElement.with(getElementUtils(), element));
  }

  abstract JavaFile process(QualifiedTypeElement element) throws ProcessingException;
}
