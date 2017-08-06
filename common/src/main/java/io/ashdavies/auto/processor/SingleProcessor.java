package io.ashdavies.auto.processor;

import com.google.auto.common.SuperficialValidation;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

abstract class SingleProcessor<T extends Annotation> extends AbstractProcessor {

  private DiagnosticPrinter printer;

  @Override
  public synchronized void init(ProcessingEnvironment environment) {
    super.init(environment);

    printer = new DiagnosticPrinter(environment.getMessager());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
    Set<TypeElement> elements = getTypeElements(environment);

    if (environment.processingOver()) {
      for (TypeElement element : elements) {
        printer.error(element, "Could not generate implementation for class %s", element.getQualifiedName());
      }

      return false;
    }

    for (TypeElement element : elements) {
      if (!SuperficialValidation.validateElement(element)) {
        continue;
      }

      write(element);
    }

    return false;
  }

  private Set<TypeElement> getTypeElements(RoundEnvironment environment) {
    return ElementFilter.typesIn(environment.getElementsAnnotatedWith(getSupportedAnnotation()));
  }

  private void write(TypeElement element) {
    try {
      process(element).writeTo(getFiler());
    } catch (AbortProcessingException | IOException exception) {
      printer.error(element, exception);
    }
  }

  abstract JavaFile process(TypeElement element) throws AbortProcessingException;

  private Filer getFiler() {
    return processingEnv.getFiler();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return ImmutableSet.of(getSupportedAnnotation().getName());
  }

  abstract Class<T> getSupportedAnnotation();

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  Elements getElementUtils() {
    return processingEnv.getElementUtils();
  }

  public DiagnosticPrinter getDiagnosticPrinter() {
    return printer;
  }
}
