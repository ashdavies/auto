package io.ashdavies.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoDecorator {

  boolean iterable() default false;

  Class<? extends Strategy<?>>[] strategy() default {};

  interface Strategy<T> {

    Class<T> target();

    T merge(T... t);
  }
}
