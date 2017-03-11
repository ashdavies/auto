package io.ashdavies.auto.diagnostic;

import javax.lang.model.element.Element;

public class ProcessingException extends Throwable {

  private final transient Element element;

  public ProcessingException(Element element) {
    this.element = element;
  }

  public Element getElement() {
    return element;
  }
}
