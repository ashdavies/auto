package io.ashdavies.auto.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.ashdavies.auto.AutoDecorator;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import org.apache.commons.lang3.StringUtils;

class DecoratorProcessingStep extends SingleProcessingStep<AutoDecorator> {

  private static final String DECORATED_FIELD = "decorated";
  private static final String DECORATOR_SUFFIX = "Decorator";

  private static final String PARAMETER_SEPARATOR = ", ";

  DecoratorProcessingStep(ProcessingEnvironment environment) {
    super(environment);
  }

  @Override
  protected Class<AutoDecorator> annotation() {
    return AutoDecorator.class;
  }

  @Override
  boolean valid(EnrichedTypeElement element) {
    return !element.isFinal();
  }

  @Override
  JavaFile process(EnrichedTypeElement element) {
    TypeSpec typeSpec = TypeSpec.classBuilder(element.getClassName(DECORATOR_SUFFIX))
        .addModifiers(element.getAccessModifier())
        .addTypeVariables(element.getTypeVariables())
        .addSuperinterface(element.getTypeName())
        .addField(createDelegateFieldSpec(element))
        .addMethod(createConstructorMethod(element))
        .addMethods(createOverridingMethods(element))
        .build();

    return JavaFile.builder(element.getPackageName(), typeSpec)
        .build();
  }

  private FieldSpec createDelegateFieldSpec(EnrichedTypeElement element) {
    boolean iterable = element.getAnnotation(AutoDecorator.class).iterable();
    if (iterable) {
      return FieldSpec.builder(element.getArrayTypeName(), DECORATED_FIELD, getFieldModifiers())
          .build();
    }

    return FieldSpec.builder(element.getTypeName(), DECORATED_FIELD, getFieldModifiers())
        .build();
  }

  private Modifier[] getFieldModifiers() {
    return new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
  }

  private MethodSpec createConstructorMethod(EnrichedTypeElement element) {
    boolean iterable = element.getAnnotation(AutoDecorator.class).iterable();
    TypeName type = iterable ? element.getArrayTypeName() : element.getTypeName();

    MethodSpec.Builder builder = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterSpec.builder(type, DECORATED_FIELD).build())
        .addStatement("this.$L = $L", DECORATED_FIELD, DECORATED_FIELD);

    if (iterable) {
      builder.varargs();
    }

    return builder.build();
  }

  private List<MethodSpec> createOverridingMethods(EnrichedTypeElement qualified) {
    List<ExecutableElement> executable = qualified.getExecutableElements();
    List<MethodSpec> methods = new ArrayList<>(executable.size());

    for (ExecutableElement element : executable) {
      methods.add(createOverridingMethod(qualified, element));
    }

    return methods;
  }

  private MethodSpec createOverridingMethod(EnrichedTypeElement element, ExecutableElement method) {
    boolean iterable = element.getAnnotation(AutoDecorator.class).iterable();
    MethodSpec.Builder builder = MethodSpec.overriding(method);

    if (iterable) {
      builder.beginControlFlow("for ($T $L : this.$L)", element.getTypeName(), DECORATED_FIELD, DECORATED_FIELD);
    }

    if (method.getReturnType().getKind() == TypeKind.VOID) {
      builder.addStatement("$L.$L($L)", DECORATED_FIELD, method.getSimpleName(), getMethodParameters(method));
    } else if (!iterable) {
      builder.addStatement("return $L.$L($L)", DECORATED_FIELD, method.getSimpleName(), getMethodParameters(method));
    } else {
      builder.addStatement("throw new $T()", UnsupportedOperationException.class);
    }

    if (iterable) {
      builder.endControlFlow();
    }

    return builder.build();
  }

  private String getMethodParameters(ExecutableElement method) {
    List<? extends Element> parameters = method.getParameters();
    List<String> names = new ArrayList<>(parameters.size());

    for (Element parameter : parameters) {
      names.add(parameter.getSimpleName().toString());
    }

    return StringUtils.join(names, PARAMETER_SEPARATOR);
  }
}
