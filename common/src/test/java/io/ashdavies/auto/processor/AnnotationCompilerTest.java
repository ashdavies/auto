package io.ashdavies.auto.processor;

import com.google.testing.compile.JavaFileObjects;
import java.lang.annotation.Annotation;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public abstract class AnnotationCompilerTest<T extends Annotation> {

  private static final String PACKAGE = "io.ashdavies.auto";
  private static final String INVOKER = PACKAGE + ".Invoker";

  private static final String DIVIDER = ".";
  private static final String IMPORT = "import";
  private static final String EMPTY = "";

  private final Processor processor;
  private final Class<T> kls;

  AnnotationCompilerTest(Processor processor, Class<T> kls) {
    this.processor = processor;
    this.kls = kls;
  }

  @Test
  public void shouldGenerateAbstractClass() throws Exception {
    JavaFileObject source = forSourceLines(
        "public abstract class Invoker {",
        "  public abstract void invoke(String string);",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateExternalClass() throws Exception {
    JavaFileObject source = forSourceLines(
        "interface Invoker {",
        "  void invoke();",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateInnerClass() throws Exception {
    JavaFileObject source = forSourceLines(
        "public class Invoker {",
        "  @AutoNoOp",
        "  public interface Inner {",
        "    void invoke();",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateExtendedInnerClass() throws Exception {
    JavaFileObject source = forSourceLines(
        "public class Invoker {",
        "  @AutoNoOp",
        "  public interface Extended extends Inner {",
        "  }",
        "  public interface Inner {",
        "    void invoke();",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldNotGenerateInvalidClass() throws Exception {
    JavaFileObject source = forSourceLines(
        "public class Invoker {",
        "  public void invoke() {",
        "  }",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .failsToCompile();
  }

  @Test
  public void shouldGeneratePackageMethod() throws Exception {
    JavaFileObject source = forSourceLines(
        "@AutoNoOp",
        "public abstract class Invoker {",
        "  abstract void invoke();",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateParametrizedMethod() throws Exception {
    JavaFileObject source = forSourceLines(
        "public interface Invoker {",
        "  void invoke(List<String> list);",
        "  void invoke(Map<String, String> map);",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGeneratePrimitiveDefaults() throws Exception {
    JavaFileObject source = forSourceLines(
        "public interface Invoker {",
        "  byte invoke(byte ignored);",
        "  short invoke(short ignored);",
        "  int invoke(int ignored);",
        "  long invoke(long ignored);",
        "  float invoke(float ignored);",
        "  double invoke(double ignored);",
        "  char invoke(char ignored);",
        "  boolean invoke(boolean ignored);",
        "  Object[] invoke(Object[] ignored);",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateProtectedMethod() throws Exception {
    JavaFileObject source = forSourceLines(
        "public abstract class Invoker {",
        "  protected abstract void invoke();",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  @Test
  public void shouldGenerateSimpleMethod() throws Exception {
    JavaFileObject source = forSourceLines(
        "public interface Invoker {",
        "  void invoke();",
        "}"
    );

    assertAbout(javaSource()).that(source)
        .processedWith(processor)
        .compilesWithoutError();
  }

  private JavaFileObject forSourceLines(String... lines) {
    return JavaFileObjects.forSourceLines(
        INVOKER,
        ArrayUtils.addAll(new String[] {
                PACKAGE,
                EMPTY,
                String.format("%s %s", IMPORT, kls.getCanonicalName()),
                EMPTY,
                name(kls.getCanonicalName())
            },
            lines
        )
    );
  }

  private String name(String full) {
    String[] split = full.split(DIVIDER);

    if (split.length == 0) {
      throw new IllegalArgumentException();
    }

    return split[split.length - 1];
  }
}
