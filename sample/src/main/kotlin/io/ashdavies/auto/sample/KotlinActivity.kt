package io.ashdavies.auto.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class KotlinActivity : AppCompatActivity(), KotlinPresenter.View {

  private lateinit var presenter: KotlinPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    presenter = KotlinPresenter()
  }

  override fun onResume() {
    super.onResume()
    presenter.attach(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.detach()
  }

  override fun showMessage(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
