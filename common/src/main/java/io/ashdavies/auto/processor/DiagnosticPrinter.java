package io.ashdavies.auto.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import static io.ashdavies.auto.processor.ExceptionWriter.write;

class DiagnosticPrinter {

  private final Messager messager;

  DiagnosticPrinter(Messager messager) {
    this.messager = messager;
  }

  public void note(Element element, String message, Object... args) {
    print(Diagnostic.Kind.NOTE, element, message, args);
  }

  void error(Element element, Throwable throwable) {
    error(element, "Unable to process @%s.\n\n%s", element.getSimpleName(), write(throwable));
  }

  void error(Element element, String message, Object... args) {
    print(Diagnostic.Kind.ERROR, element, message, args);
  }

  void print(Diagnostic.Kind kind, Element element, String message, Object... args) {
    if (args.length > 0) {
      message = String.format(message, args);
    }

    messager.printMessage(kind, message, element);
  }
}
