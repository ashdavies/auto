package io.ashdavies.auto.processor;

import com.google.testing.compile.JavaFileObjects;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public abstract class AnnotationCompilerTest<T extends Annotation> {

  private static final String PACKAGE_NAME = "io.ashdavies.auto";
  private static final String CLASS_NAME = PACKAGE_NAME + ".Invoker";

  private final Processor processor;
  private final Class<T> kls;

  AnnotationCompilerTest(Processor processor, Class<T> kls) {
    this.processor = processor;
    this.kls = kls;
  }

  @Test
  public void shouldGenerateInterface() throws Exception {
    compilesWithoutError(
        "public interface Invoker {",
        "  void invoke(String string);",
        "}"
    );
  }

  @Test
  public void shouldGenerateAbstractClass() throws Exception {
    compilesWithoutError(
        "public abstract class Invoker {",
        "  public abstract void invoke(String string);",
        "}"
    );
  }

  @Test
  public void shouldGenerateExternalClass() throws Exception {
    compilesWithoutError(
        "interface Invoker {",
        "  void invoke();",
        "}"
    );
  }

  @Test
  public void shouldGenerateInnerClass() throws Exception {
    compilesWithoutError(JavaFileObjects.forSourceLines(CLASS_NAME, Arrays.asList(
        String.format("package %s;", PACKAGE_NAME),
        "  public class Invoker {",
        String.format("  @%s", name(kls)),
        "  public interface Inner {",
        "    void invoke();",
        "  }",
        "}"
    )));
  }

  @Test
  public void shouldGenerateExtendedInterface() throws Exception {
    compilesWithoutError(
        "public interface Invoker extends CharSequence {",
        "}"
    );
  }

  @Test
  public void shouldGenerateImplementingClass() throws Exception {
    compilesWithoutError(
        "public abstract class Invoker implements CharSequence {",
        "}"
    );
  }

  @Test
  public void shouldNotGenerateInvalidClass() throws Exception {
    failsToCompile(
        "public class Invoker {",
        "  public void invoke() {",
        "  }",
        "}"
    );
  }

  @Test
  @Ignore
  public void shouldGeneratePackageMethod() throws Exception {
    compilesWithoutError(
        "public abstract class Invoker {",
        "  abstract void invoke();",
        "}"
    );
  }

  @Test
  public void shouldGenerateParametrizedMethod() throws Exception {
    compilesWithoutError(
        "public interface Invoker {",
        "  void invoke(java.util.List<String> list);",
        "  void invoke(java.util.Map<String, String> map);",
        "}"
    );
  }

  @Test
  public void shouldGeneratePrimitiveDefaults() throws Exception {
    compilesWithoutError(
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
  }

  @Test
  public void shouldGenerateProtectedMethod() throws Exception {
    compilesWithoutError(
        "public abstract class Invoker {",
        "  protected abstract void invoke();",
        "}"
    );
  }

  @Test
  public void shouldGenerateSimpleMethod() throws Exception {
    compilesWithoutError(
        "public interface Invoker {",
        "  void invoke();",
        "}"
    );
  }

  private void compilesWithoutError(String... lines) {
    compilesWithoutError(forSourceLines(lines));
  }

  private void compilesWithoutError(JavaFileObject target) {
    assertAbout(javaSource()).that(target)
        .processedWith(processor)
        .compilesWithoutError();
  }

  private void failsToCompile(String... lines) {
    assertAbout(javaSource())
        .that(forSourceLines(lines))
        .processedWith(processor)
        .failsToCompile();
  }

  private JavaFileObject forSourceLines(String... lines) {
    return JavaFileObjects.forSourceLines(CLASS_NAME, ArrayUtils.addAll(
        new String[] {
            String.format("package %s;", PACKAGE_NAME), StringUtils.EMPTY,
            String.format("import %s;", kls.getCanonicalName()), StringUtils.EMPTY,
            String.format("@%s", name(kls))
        },
        lines
    ));
  }

  private String name(Class<T> kls) {
    String[] split = kls.getCanonicalName().split("\\.");
    if (split.length == 0) {
      throw new IllegalArgumentException();
    }

    return split[split.length - 1];
  }
}
