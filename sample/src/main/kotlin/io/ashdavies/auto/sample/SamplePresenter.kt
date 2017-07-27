package io.ashdavies.auto.sample

import io.ashdavies.auto.AutoDecorator
import io.ashdavies.auto.AutoNoOp

internal class SamplePresenter {

  private val interactor = SampleInteractor()

  fun attach(view: SamplePresenter.View) {
    attach(MainUseCaseListener(view))
  }

  private fun attach(listener: SampleInteractor.Listener) {
    interactor.addListener(listener)
    interactor.state.notify(listener)
  }

  fun detach() {
    interactor.clearListeners()
  }

  internal data class MainUseCaseListener(val view: View) : SampleInteractor.Listener {

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
