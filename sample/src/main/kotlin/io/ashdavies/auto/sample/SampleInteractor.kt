package io.ashdavies.auto.sample

import io.ashdavies.auto.AutoDecorator
import io.ashdavies.auto.AutoNoOp

internal class SampleInteractor {

  private var listener: Listener = `KotlinInteractor$ListenerNoOp`()

  val state: State = `KotlinInteractor$StateNoOp`()

  fun addListener(listener: Listener) {
    this.listener = `KotlinInteractor$ListenerDecorator`(listener)
  }

  fun clearListeners() {
    this.listener = `KotlinInteractor$ListenerNoOp`()
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
