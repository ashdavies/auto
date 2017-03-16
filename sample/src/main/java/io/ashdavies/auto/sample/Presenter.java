package io.ashdavies.auto.sample;

class Presenter {

  private Interactor interactor = new Interactor();

  public void attach(Presenter.View view) {
    attach(new MainUseCaseListener(view));
  }

  private void attach(Interactor.Listener listener) {
    interactor.addListener(listener);
    interactor.getState().notify(listener);
  }

  public void detach() {
    interactor.clearListeners();
  }

  private static final class MainUseCaseListener implements Interactor.Listener {

    private final Presenter.View view;

    MainUseCaseListener(View view) {
      this.view = view;
    }

    @Override
    public void onError(Throwable exception) {
      view.showMessage(exception.getMessage());
    }
  }

  interface View {

    void showMessage(String message);
  }
}
