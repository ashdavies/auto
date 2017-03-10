package io.ashdavies.auto.strategy;

import io.ashdavies.auto.AutoDecorator;

public class BooleanStrategy implements AutoDecorator.Strategy<Boolean> {

  @Override
  public Class<Boolean> target() {
    return Boolean.class;
  }

  @Override
  public Boolean merge(Boolean... values) {
    for (Boolean value : values) {
      if (!value) {
        return false;
      }
    }

    return true;
  }
}
