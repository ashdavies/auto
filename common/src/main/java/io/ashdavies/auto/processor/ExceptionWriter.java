package io.ashdavies.auto.processor;

import java.io.PrintWriter;
import java.io.StringWriter;

class ExceptionWriter extends StringWriter {

  private ExceptionWriter() {
  }

  static ExceptionWriter write(Throwable throwable) {
    return write(new ExceptionWriter(), throwable);
  }

  static ExceptionWriter write(ExceptionWriter writer, Throwable throwable) {
    throwable.printStackTrace(new PrintWriter(writer));
    return writer;
  }
}
