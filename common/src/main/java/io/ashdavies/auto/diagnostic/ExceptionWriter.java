package io.ashdavies.auto.diagnostic;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWriter extends StringWriter {

  private ExceptionWriter() {
  }

  public static ExceptionWriter write(Throwable throwable) {
    return write(new ExceptionWriter(), throwable);
  }

  public static ExceptionWriter write(ExceptionWriter writer, Throwable throwable) {
    throwable.printStackTrace(new PrintWriter(writer));
    return writer;
  }
}
