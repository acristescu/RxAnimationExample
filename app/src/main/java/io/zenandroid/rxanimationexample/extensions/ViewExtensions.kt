package io.zenandroid.rxanimationexample.extensions

/**
 * Created by alex on 21/11/2017.
 */
import android.view.View
import android.view.animation.OvershootInterpolator
import io.reactivex.Completable

fun View.showIf(show: Boolean) {
    if (show) {
        show()
    } else {
        hide()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.slideIn(offset: Float): Completable {
    return Completable.create {
        visibility = View.VISIBLE
        alpha = 0f
        scaleX = 0f
        scaleY = 0f
        translationY = offset
        animate().alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(200)
                .setInterpolator(OvershootInterpolator())
                .withEndAction(it::onComplete)
    }
}

fun View.slideOut(offset: Float): Completable {
    return Completable.create {
        animate().alpha(0f)
                .scaleX(0f)
                .scaleY(0f)
                .translationY(offset)
                .setDuration(200)
                .withEndAction {
                    visibility = View.GONE
                    it.onComplete()
                }
    }
}

fun View.fadeOut(duration: Long = 30): Completable {
    return Completable.create {
        animate().setDuration(duration)
                .alpha(0f)
                .withEndAction {
                    visibility = View.GONE
                    it.onComplete()
                }
    }
}

fun View.fadeIn(): Completable {
    return Completable.create {
        visibility = View.VISIBLE
        alpha = 0f
        animate().alpha(1f)
                .setDuration(200)
                .withEndAction(it::onComplete)
    }
}

fun View.rotate(degree: Float): Completable {
    return Completable.create {
        animate().rotation(degree)
                .setDuration(200)
                .withEndAction(it::onComplete)
    }
}