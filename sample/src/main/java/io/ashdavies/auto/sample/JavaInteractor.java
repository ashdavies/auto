package io.ashdavies.auto.sample;

import com.google.auto.value.AutoValue;
import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;

class JavaInteractor {

  private Listener listener;
  private State state;

  JavaInteractor() {
    this.listener = new JavaInteractor$ListenerDecorator();
    this.state = new JavaInteractor$StateNoOp();
  }

  void addListener(Listener listener) {
    this.listener = new JavaInteractor$ListenerDecorator(listener);
  }

  void clearListeners() {
    this.listener = new JavaInteractor$ListenerDecorator();
  }

  State getState() {
    return state;
  }

  void onError(Exception exception) {
    listener.onError(exception);
  }

  @AutoNoOp
  @AutoDecorator
  interface Listener {

    void onProgress(boolean progress);

    void onError(Throwable exception);
  }

  @AutoNoOp
  interface State {

    void notify(Listener listener);
  }

  @AutoValue
  abstract static class IdleState implements State {

    static State create() {
      return new AutoValue_JavaInteractor_IdleState();
    }

    @Override
    public void notify(Listener listener) {
      listener.onProgress(false);
    }
  }
}
