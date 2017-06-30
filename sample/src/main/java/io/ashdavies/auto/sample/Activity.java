package io.ashdavies.auto.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Activity extends AppCompatActivity implements Presenter.View {

  private Presenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    presenter = new Presenter();
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

  @Override
  public void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
