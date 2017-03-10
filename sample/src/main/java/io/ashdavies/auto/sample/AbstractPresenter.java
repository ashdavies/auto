package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoNoOp;

interface AbstractPresenter<T extends AbstractPresenter.View> {

  void attach(T t);

  void detach();

  @AutoNoOp
  interface View {
  }
}
