package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;
import io.ashdavies.auto.strategy.BooleanStrategy;

class MainPresenter implements AbstractPresenter<MainPresenter.View> {

  private MainUseCase mainUseCase = new MainUseCase();

  @Override
  public void attach(MainPresenter.View view) {
    mainUseCase.addListener(new MainUseCaseListener(view));
  }

  @Override
  public void detach() {
    mainUseCase.clearListeners();
  }

  private static final class MainUseCaseListener implements MainUseCase.Listener {

    private final MainPresenter.View view;

    MainUseCaseListener(View view) {
      this.view = view;
    }

    @Override
    public void onError(Throwable exception) {
      view.showMessage(exception.getMessage());
    }
  }

  @AutoNoOp
  @AutoDecorator(iterable = true, strategy = BooleanStrategy.class)
  interface View extends AbstractPresenter.View {

    void showMessage(String message);
  }
}
