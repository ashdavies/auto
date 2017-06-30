package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;

class JavaPresenter {

  private JavaInteractor interactor = new JavaInteractor();

  void attach(JavaPresenter.View view) {
    attach(new MainUseCaseListener(view));
  }

  private void attach(JavaInteractor.Listener listener) {
    interactor.addListener(listener);
    interactor.getState().notify(listener);
  }

  void detach() {
    interactor.clearListeners();
  }

  private static final class MainUseCaseListener implements JavaInteractor.Listener {

    private final JavaPresenter.View view;

    MainUseCaseListener(View view) {
      this.view = view;
    }

    @Override
    public void onProgress(boolean progress) {
      /* no op */
    }

    @Override
    public void onError(Throwable exception) {
      view.showMessage(exception.getMessage());
    }
  }

  @AutoNoOp(instance = true)
  @AutoDecorator(iterable = true)
  interface View {

    void showMessage(String message);
  }
}
