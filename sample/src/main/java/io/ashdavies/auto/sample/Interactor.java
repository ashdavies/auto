package io.ashdavies.auto.sample;

import com.google.auto.value.AutoValue;
import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;

class Interactor {

  private Listener listener;
  private State state;

  Interactor() {
    this.listener = new Interactor$ListenerDecorator();
    this.state = Interactor$StateNoOp.instance();
  }

  void addListener(Listener listener) {
    this.listener = new Interactor$ListenerDecorator(listener);
  }

  void clearListeners() {
    this.listener = new Interactor$ListenerDecorator();
  }

  State getState() {
    return state;
  }

  void onError(Exception exception) {
    listener.onError(exception);
  }

  @AutoDecorator(iterable = true)
  interface Listener {

    void onProgress(boolean progress);

    void onError(Throwable exception);
  }

  @AutoNoOp(instance = true)
  interface State {

    void notify(Listener listener);
  }

  @AutoValue
  static abstract class IdleState implements State {

    static State create() {
      return new AutoValue_Interactor_IdleState();
    }

    @Override
    public void notify(Listener listener) {
      listener.onProgress(false);
    }
  }
}
