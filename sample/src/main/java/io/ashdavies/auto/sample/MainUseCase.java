package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;

class MainUseCase {

  private Listener listener = MainUseCase$ListenerNoOp.instance();

  void addListener(Listener listener) {
    this.listener = new MainUseCase$ListenerDecorator(listener);
  }

  void clearListeners() {
    this.listener = new MainUseCase$ListenerNoOp();
  }

  @AutoNoOp(instance = true)
  @AutoDecorator(iterable = true)
  interface Listener {

    void onError(Throwable exception);
  }
}
