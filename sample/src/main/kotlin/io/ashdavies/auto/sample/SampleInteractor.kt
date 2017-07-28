package io.ashdavies.auto.sample

import io.ashdavies.auto.AutoDecorator
import io.ashdavies.auto.AutoNoOp

internal class SampleInteractor {

  private var listener: Listener = `SampleInteractor$ListenerNoOp`()

  val state: State = `SampleInteractor$StateNoOp`()

  fun addListener(listener: Listener) {
    this.listener = `SampleInteractor$ListenerDecorator`(listener)
  }

  fun clearListeners() {
    this.listener = `SampleInteractor$ListenerNoOp`()
  }

  fun onError(exception: Exception) {
    listener.onError(exception)
  }

  @AutoNoOp
  @AutoDecorator
  internal interface Listener {

    fun onProgress(progress: Boolean)

    fun onError(exception: Throwable)
  }

  @AutoNoOp
  internal interface State {

    fun notify(listener: Listener)
  }

  internal class IdleState : State {

    override fun notify(listener: Listener) {
      listener.onProgress(false)
    }
  }
}
