package io.ashdavies.auto.processor;

import javax.lang.model.element.Element;

class ProcessingException extends Throwable {

  private final transient Element element;

  ProcessingException(Element element) {
    this.element = element;
  }

  Element getElement() {
    return element;
  }
}
