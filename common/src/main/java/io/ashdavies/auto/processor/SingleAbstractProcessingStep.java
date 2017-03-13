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
    Set<Element> set = new HashSet<>();

    for (Element element : elements.get(annotation())) {
      try {
        process(element).writeTo(getFiler());
      } catch (Exception exception) {
        error(element, exception);
        set.add(element);
      }
    }

    return set;
  }

  private JavaFile process(Element element) {
    return process(QualifiedTypeElement.with(getElementUtils(), element));
  }

  abstract JavaFile process(QualifiedTypeElement element);

  private void error(Element element, Exception exception) {
    with(getMessager()).error(element, exception);
  }
}
