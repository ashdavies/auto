package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;

class MainUseCase {

  private Listener listener;
  private State state;

  MainUseCase() {
    this.listener = new MainUseCase$ListenerDecorator();
    this.state = MainUseCase$StateNoOp.instance();
  }

  void addListener(Listener listener) {
    this.listener = new MainUseCase$ListenerDecorator(listener);
  }

  void clearListeners() {
    this.listener = new MainUseCase$ListenerDecorator();
  }

  State getState() {
    return state;
  }

  void onError(Exception exception) {
    listener.onError(exception);
  }

  @AutoDecorator(iterable = true)
  interface Listener {

    void onError(Throwable exception);
  }

  @AutoNoOp(instance = true)
  interface State {

    void notify(Listener listener);
  }
}
