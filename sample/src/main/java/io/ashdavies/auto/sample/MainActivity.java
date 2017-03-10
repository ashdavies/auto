package io.ashdavies.auto.sample;

import android.os.Bundle;

public class MainActivity extends AbstractActivity implements MainPresenter.View {

  private MainPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    presenter = new MainPresenter();
  }

  @Override
  protected void onResume() {
    super.onResume();
    presenter.attach(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    presenter.detach();
  }
}
