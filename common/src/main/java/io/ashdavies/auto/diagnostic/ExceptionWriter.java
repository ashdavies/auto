package io.ashdavies.auto.diagnostic;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionWriter extends StringWriter {

  private ExceptionWriter() {
  }

  public static ExceptionWriter write(Exception exception) {
    return write(new ExceptionWriter(), exception);
  }

  public static ExceptionWriter write(ExceptionWriter writer, Exception exception) {
    exception.printStackTrace(new PrintWriter(writer));
    return writer;
  }
}
