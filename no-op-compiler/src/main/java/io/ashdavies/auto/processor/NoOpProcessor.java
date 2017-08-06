package io.ashdavies.auto.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.ashdavies.auto.AutoNoOp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
public class NoOpProcessor extends SingleProcessor<AutoNoOp> {

  private static final String NO_OP_SUFFIX = "NoOp";
  private static final String NO_OP_COMMENT = "no-op";

  private static final String INSTANCE_CONST = "INSTANCE";

  @Override
  Class<AutoNoOp> getSupportedAnnotation() {
    return AutoNoOp.class;
  }

  @Override
  JavaFile process(TypeElement element) throws AbortProcessingException {
    return process(EnrichedTypeElement.with(getElementUtils(), element));
  }

  private JavaFile process(EnrichedTypeElement element) throws AbortProcessingException {
    TypeSpec.Builder builder = TypeSpec.classBuilder(element.getClassName(NO_OP_SUFFIX));

    if (element.getAnnotation(AutoNoOp.class).instance()) {
      builder
          .addField(createInstanceField(element))
          .addMethod(createInstanceMethod(element));
    }

    if (element.isInterface()) {
      builder.addSuperinterface(element.getTypeName());
    } else if (element.isAbstract()) {
      builder.superclass(element.getTypeName());
    } else {
      throw new AbortProcessingException("Cannot generate implementation for final class");
    }

    builder
        .addModifiers(element.getAccessModifier())
        .addTypeVariables(element.getTypeVariables())
        .addMethods(createOverridingMethods(element));

    return JavaFile.builder(element.getPackageName(), builder.build())
        .build();
  }

  private static FieldSpec createInstanceField(EnrichedTypeElement element) {
    return FieldSpec.builder(element.getTypeName(), INSTANCE_CONST, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("new $L()", element.getClassName(NO_OP_SUFFIX))
        .build();
  }

  private static MethodSpec createInstanceMethod(EnrichedTypeElement element) {
    return MethodSpec.methodBuilder(INSTANCE_CONST.toLowerCase())
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addStatement("return $L", INSTANCE_CONST)
        .returns(element.getTypeName())
        .build();
  }

  private List<MethodSpec> createOverridingMethods(EnrichedTypeElement qualified) {
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
          .addComment(NO_OP_COMMENT)
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
