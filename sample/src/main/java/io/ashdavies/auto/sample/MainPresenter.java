package io.ashdavies.auto.sample;

import io.ashdavies.auto.AutoDecorator;
import io.ashdavies.auto.AutoNoOp;
import io.ashdavies.auto.strategy.BooleanStrategy;

class MainPresenter implements AbstractPresenter<MainPresenter.View> {

  private AbstractPresenter.View view = new MainPresenter$ViewNoOp();

  @Override
  public void attach(MainPresenter.View view) {
    this.view = new MainPresenter$ViewDecorator(this.view, view);
  }

  @Override
  public void detach() {
    this.view = new MainPresenter$ViewNoOp();
  }

  @AutoNoOp
  @AutoDecorator(iterable = true, strategy = BooleanStrategy.class)
  public interface View extends AbstractPresenter.View {
  }
}
