package io.ashdavies.auto.processing;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.ashdavies.auto.AutoNoOp;
import io.ashdavies.auto.element.QualifiedTypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

class NoOpProcessingStep extends SingleAbstractProcessingStep {

  private static final String NO_OP_SUFFIX = "NoOp";

  NoOpProcessingStep(ProcessingEnvironment environment) {
    super(environment);
  }

  @Override
  protected Class<? extends Annotation> annotation() {
    return AutoNoOp.class;
  }

  @Override
  JavaFile process(QualifiedTypeElement element) throws Exception {
    return JavaFile.builder(element.getPackageName(), createClassSpec(element))
        .build();
  }

  private TypeSpec createClassSpec(QualifiedTypeElement element) {
    TypeSpec.Builder builder = TypeSpec.classBuilder(element.getClassName(NO_OP_SUFFIX));

    if (element.isInterface()) {
      builder.addSuperinterface(element.getTypeName());
    } else if (element.isAbstract()) {
      builder.superclass(element.getTypeName());
    } else {
      throw new UnsupportedOperationException();
    }

    return builder
        .addModifiers(element.getAccessModifier())
        .addTypeVariables(element.getTypeVariables())
        .addMethods(createOverridingMethods(element))
        .build();
  }

  private List<MethodSpec> createOverridingMethods(QualifiedTypeElement qualified) {
    List<ExecutableElement> executable = qualified.getExecutableElements();
    List<MethodSpec> methods = new ArrayList<>(executable.size());

    for (ExecutableElement element : executable) {
      if (!isAbstract(element)) {
        continue;
      }

      methods.add(createOverridingMethod(element));
    }

    return methods;
  }

  private static boolean isAbstract(ExecutableElement element) {
    for (Modifier modifier : element.getModifiers()) {
      if (modifier == Modifier.ABSTRACT) {
        return true;
      }
    }

    return false;
  }

  private static MethodSpec createOverridingMethod(ExecutableElement method) {
    MethodSpec.Builder builder = MethodSpec.overriding(method);

    if (method.getReturnType().getKind() == TypeKind.VOID) {
      return builder
          .addComment("no-op")
          .build();
    }

    return builder
        .addStatement("return $L", getDefaultReturnValue(method.getReturnType()))
        .build();
  }

  private static String getDefaultReturnValue(TypeMirror type) {
    switch (type.getKind()) {
      case BYTE:
      case CHAR:
      case SHORT:
      case INT:
        return "0";

      case LONG:
        return "0l";

      case FLOAT:
        return "0.0f";

      case DOUBLE:
        return "0.0d";

      case BOOLEAN:
        return "false";

      default:
        if (type.getKind().isPrimitive()) {
          throw new IllegalStateException("Unexpected primitive return type");
        }

        return "null";
    }
  }
}
