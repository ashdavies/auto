package io.ashdavies.auto.element;

import com.google.auto.common.MoreTypes;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.util.Elements;

public class QualifiedTypeElement {

  private static final String CLASS_SEPARATOR = "$";

  private final PackageElement pkg;
  private final TypeElement type;

  private QualifiedTypeElement(PackageElement pkg, TypeElement type) {
    this.pkg = pkg;
    this.type = type;
  }

  public static QualifiedTypeElement with(Elements elements, Element element) {
    return new QualifiedTypeElement(elements.getPackageOf(element), (TypeElement) element);
  }

  public String getPackageName() {
    return pkg.getQualifiedName().toString();
  }

  public String getClassName(String suffix) {
    String name = type.getSimpleName() + suffix;

    for (Element enclosing : getEnclosingElements()) {
      name = enclosing.getSimpleName() + CLASS_SEPARATOR + name;
    }

    return name;
  }

  public TypeName getArrayTypeName() {
    return ArrayTypeName.of(getTypeName());
  }

  public TypeName getTypeName() {
    return TypeName.get(type.asType());
  }

  private List<Element> getEnclosingElements() {
    List<Element> elements = new ArrayList<>();
    Element enclosing = type.getEnclosingElement();

    while (enclosing != null && !(enclosing instanceof PackageElement)) {
      elements.add(enclosing);
      enclosing = enclosing.getEnclosingElement();
    }

    return elements;
  }

  public <T extends Annotation> T getAnnotation(Class<T> kls) {
    return type.getAnnotation(kls);
  }

  public Modifier[] getAccessModifier() {
    for (Modifier modifier : type.getModifiers()) {
      switch (modifier) {
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
          return new Modifier[] { modifier };
      }
    }

    return new Modifier[] {};
  }

  public List<TypeVariableName> getTypeVariables() {
    List<? extends TypeParameterElement> parameters = type.getTypeParameters();
    List<TypeVariableName> variables = new ArrayList<>(parameters.size());

    for (TypeParameterElement parameter : parameters) {
      variables.add(TypeVariableName.get(parameter));
    }

    return variables;
  }

  public List<ExecutableElement> getExecutableElements() {
    List<Element> enclosed = getEnclosedElements();
    List<ExecutableElement> executable = new ArrayList<>(enclosed.size());

    for (Element element : enclosed) {
      if (element.getKind() == ElementKind.METHOD) {
        executable.add((ExecutableElement) element);
      }
    }

    return executable;
  }

  public List<Element> getEnclosedElements() {
    List<Element> elements = new ArrayList<>(type.getEnclosedElements());

    for (Element element : MoreTypes.asTypeElements(type.getInterfaces())) {
      elements.addAll(element.getEnclosedElements());
    }

    return elements;
  }

  public boolean isInterface() {
    return type.getKind().isInterface();
  }

  public boolean isAbstract() {
    for (Modifier modifier : type.getModifiers()) {
      if (modifier == Modifier.ABSTRACT) {
        return true;
      }
    }

    return false;
  }

  public boolean isFinal() {
    for (Modifier modifier : type.getModifiers()) {
      if (modifier == Modifier.FINAL) {
        return true;
      }
    }

    return false;
  }
}
