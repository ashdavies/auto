package io.ashdavies.auto.sample

import io.ashdavies.auto.AutoDecorator
import io.ashdavies.auto.AutoNoOp

internal class KotlinPresenter {

  private val interactor = KotlinInteractor()

  fun attach(view: KotlinPresenter.View) {
    attach(MainUseCaseListener(view))
  }

  private fun attach(listener: KotlinInteractor.Listener) {
    interactor.addListener(listener)
    interactor.state.notify(listener)
  }

  fun detach() {
    interactor.clearListeners()
  }

  internal data class MainUseCaseListener(val view: View) : KotlinInteractor.Listener {

    override fun onError(exception: Throwable) {
      view.showMessage(exception.message)
    }

    override fun onProgress(progress: Boolean) {
      /* no op */
    }
  }

  @AutoNoOp
  @AutoDecorator
  internal interface View {

    fun showMessage(message: String?)
  }
}
