package io.ashdavies.auto.diagnostic;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import static io.ashdavies.auto.diagnostic.ExceptionWriter.write;

public class DiagnosticPrinter {

  private final Messager messager;

  private DiagnosticPrinter(Messager messager) {
    this.messager = messager;
  }

  public static DiagnosticPrinter with(Messager messager) {
    return new DiagnosticPrinter(messager);
  }

  public void error(Element element, Throwable throwable) {
    error(element, "Unable to process @%s.\n\n%s", element.getSimpleName(), write(throwable));
  }

  public void error(Element element, String message, Object... args) {
    print(Diagnostic.Kind.ERROR, element, message, args);
  }

  public void note(Element element, String message, Object... args) {
    print(Diagnostic.Kind.NOTE, element, message, args);
  }

  public void print(Diagnostic.Kind kind, Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }

    messager.printMessage(kind, message, element);
  }
}
