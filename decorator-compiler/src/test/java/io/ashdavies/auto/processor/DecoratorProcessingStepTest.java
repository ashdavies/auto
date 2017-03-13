package io.ashdavies.auto.processor;

import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class DecoratorProcessingStepTest {

  @Test
  public void shouldGenerateAbstractClass() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "import java.lang.String;",
        "",
        "@AutoNoOp",
        "public abstract class Invoker {",
        "  public abstract void invoke(String string);",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "import java.lang.String;",
        "",
        "public class InvokerDecorator extends Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke(String string) {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateExternalClass() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.external.Invoker",
        "package io.ashdavies.external;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "interface Invoker {",
        "  void invoke();",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.external.InvokerNoOp",
        "package io.ashdavies.external;",
        "",
        "import java.lang.Override;",
        "",
        "class InvokerDecorator implements Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateInnerClass() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Outer",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "public class Outer {",
        "",
        "  @AutoNoOp",
        "  public interface Invoker {",
        "    void invoke();",
        "  }",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Outer$InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "",
        "public class Outer$InvokerDecorator implements Outer.Invoker {",
        "  private static final Outer.Invoker INSTANCE = new Outer$InvokerNoOp();",
        "",
        "  public static Outer.Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateExtendedInnerClass() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Outer",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "public class Outer {",
        "",
        "  @AutoNoOp",
        "  public interface Extended extends Invoker {",
        "  }",
        "",
        "  public interface Invoker {",
        "    void invoke();",
        "  }",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Outer$ExtendedNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "",
        "public class Outer$ExtendedDecorator implements Outer.Extended {",
        "  private static final Outer.Extended INSTANCE = new Outer$ExtendedNoOp();",
        "",
        "  public static Outer.Extended instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldNotGenerateInvalidClass() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.AbstractInvoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "public class Invoker {",
        "  public void invoke() {",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .failsToCompile();
  }

  @Test
  public void shouldGeneratePackageMethod() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "public abstract class Invoker {",
        "  abstract void invoke();",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "",
        "public class InvokerDecorator extends Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateParametrizedMethod() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "import java.lang.String;",
        "import java.util.List;",
        "import java.util.Map;",
        "",
        "@AutoNoOp",
        "public interface Invoker {",
        "  void invoke(List<String> list);",
        "",
        "  void invoke(Map<String, String> map);",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "import java.lang.String;",
        "import java.util.List;",
        "import java.util.Map;",
        "",
        "public class InvokerDecorator implements Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke(List<String> list) {",
        "    /* no op */",
        "  }",
        "",
        "  @Override",
        "  public void invoke(Map<String, String> map) {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGeneratePrimitiveDefaults() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "public interface Invoker {",
        "  byte invoke(byte ignored);",
        "",
        "  short invoke(short ignored);",
        "",
        "  int invoke(int ignored);",
        "",
        "  long invoke(long ignored);",
        "",
        "  float invoke(float ignored);",
        "",
        "  double invoke(double ignored);",
        "",
        "  char invoke(char ignored);",
        "",
        "  boolean invoke(boolean ignored);",
        "",
        "  Object[] invoke(Object[] ignored);",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Object;",
        "import java.lang.Override;",
        "",
        "public class InvokerDecorator implements Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public byte invoke(byte ignored) {",
        "    return 0;",
        "  }",
        "",
        "  @Override",
        "  public short invoke(short ignored) {",
        "    return 0;",
        "  }",
        "",
        "  @Override",
        "  public int invoke(int ignored) {",
        "    return 0;",
        "  }",
        "",
        "  @Override",
        "  public long invoke(long ignored) {",
        "    return 0l;",
        "  }",
        "",
        "  @Override",
        "  public float invoke(float ignored) {",
        "    return 0.0f;",
        "  }",
        "",
        "  @Override",
        "  public double invoke(double ignored) {",
        "    return 0.0d;",
        "  }",
        "",
        "  @Override",
        "  public char invoke(char ignored) {",
        "    return 0;",
        "  }",
        "",
        "  @Override",
        "  public boolean invoke(boolean ignored) {",
        "    return false;",
        "  }",
        "",
        "  @Override",
        "  public Object[] invoke(Object[] ignored) {",
        "    return null;",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateProtectedMethod() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "public abstract class Invoker {",
        "  protected abstract void invoke();",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "",
        "public class InvokerDecorator extends Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  protected void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }

  @Test
  public void shouldGenerateSimpleMethod() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.Invoker",
        "package io.ashdavies.auto;",
        "",
        "import io.ashdavies.auto.AutoDecorator;",
        "",
        "@AutoNoOp",
        "public interface Invoker {",
        "  void invoke();",
        "}"
    );

    JavaFileObject output = JavaFileObjects.forSourceLines(
        "io.ashdavies.auto.InvokerNoOp",
        "package io.ashdavies.auto;",
        "",
        "import java.lang.Override;",
        "",
        "public class InvokerDecorator implements Invoker {",
        "  private static final Invoker INSTANCE = new InvokerNoOp();",
        "",
        "  public static Invoker instance() {",
        "    return INSTANCE;",
        "  }",
        "",
        "  @Override",
        "  public void invoke() {",
        "    /* no op */",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(new DecoratorProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(output);
  }
}
