package io.ashdavies.auto.processing;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.element.QualifiedTypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import org.apache.commons.lang3.StringUtils;

class DecoratorProcessingStep extends SingleAbstractProcessingStep {

  private static final String DECORATED_FIELD = "decorated";
  private static final String DECORATOR_SUFFIX = "Decorator";
  private static final String PARAMETER_SEPARATOR = ", ";

  DecoratorProcessingStep(ProcessingEnvironment environment) {
    super(environment);
  }

  @Override
  protected Class<? extends Annotation> annotation() {
    return AutoDecorator.class;
  }

  @Override
  JavaFile process(QualifiedTypeElement element) throws Exception {
    return JavaFile.builder(element.getPackageName(), createClassSpec(element))
        .build();
  }

  private TypeSpec createClassSpec(QualifiedTypeElement element) {
    return TypeSpec.classBuilder(element.getClassName(DECORATOR_SUFFIX))
        .addModifiers(element.getAccessModifier())
        .addTypeVariables(element.getTypeVariables())
        .addSuperinterface(element.getTypeName())
        .addField(createDelegateFieldSpec(element))
        .addMethod(createConstructor(element))
        .addMethods(createOverridingMethods(element))
        .build();
  }

  private FieldSpec createDelegateFieldSpec(QualifiedTypeElement element) {
    return FieldSpec.builder(element.getTypeName(), DECORATED_FIELD, getFieldModifiers())
        .build();
  }

  private Modifier[] getFieldModifiers() {
    return new Modifier[] { Modifier.PRIVATE, Modifier.FINAL };
  }

  private MethodSpec createConstructor(QualifiedTypeElement element) {
    return MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(ParameterSpec.builder(element.getTypeName(), DECORATED_FIELD).build())
        .addStatement("this.$L = $L", DECORATED_FIELD, DECORATED_FIELD)
        .build();
  }

  private List<MethodSpec> createOverridingMethods(QualifiedTypeElement qualified) {
    List<ExecutableElement> executable = qualified.getExecutableElements();
    List<MethodSpec> methods = new ArrayList<>(executable.size());

    for (ExecutableElement element : executable) {
      methods.add(createOverridingMethod(element));
    }

    return methods;
  }

  private MethodSpec createOverridingMethod(ExecutableElement method) {
    MethodSpec.Builder builder = MethodSpec.overriding(method);

    if (method.getReturnType().getKind() == TypeKind.VOID) {
      builder.addStatement("$L.$L($L)", DECORATED_FIELD, method.getSimpleName(), getMethodParameters(method));
    } else {
      builder.addStatement("return $L.$L($L)", DECORATED_FIELD, method.getSimpleName(), getMethodParameters(method));
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
