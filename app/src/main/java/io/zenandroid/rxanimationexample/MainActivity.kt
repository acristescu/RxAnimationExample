package io.zenandroid.rxanimationexample

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.reactivex.Completable
import io.zenandroid.rxanimationexample.extensions.*

class MainActivity : AppCompatActivity() {
    private enum class FabMenuState {
        OFF, SPEED, SIZE
    }

    @BindView(R.id.fab) lateinit var fab: FloatingActionButton
    @BindView(R.id.fade_out_mask) lateinit var fadeOutMask: View
    @BindView(R.id.long_fab) lateinit var longFab: FloatingActionButton
    @BindView(R.id.long_label) lateinit var longLabel: TextView
    @BindView(R.id.normal_fab) lateinit var normalFab: FloatingActionButton
    @BindView(R.id.normal_label) lateinit var normalLabel: TextView
    @BindView(R.id.blitz_fab) lateinit var blitzFab: FloatingActionButton
    @BindView(R.id.blitz_label) lateinit var blitzLabel: TextView
    @BindView(R.id.small_fab) lateinit var smallFab: FloatingActionButton
    @BindView(R.id.small_label) lateinit var smallLabel: TextView
    @BindView(R.id.medium_fab) lateinit var mediumFab: FloatingActionButton
    @BindView(R.id.medium_label) lateinit var mediumLabel: TextView
    @BindView(R.id.large_fab) lateinit var largeFab: FloatingActionButton
    @BindView(R.id.large_label) lateinit var largeLabel: TextView

    private var fabMiniSize: Float = 0f
    private var menuState = FabMenuState.OFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        fabMiniSize = resources.getDimension(R.dimen.fab_mini_with_margin)
    }

    @OnClick(R.id.fab)
    fun onFabClicked() {
        menuState = when(menuState) {
            FabMenuState.OFF -> {
                showSpeedMenu().subscribe()
                FabMenuState.SPEED
            }
            FabMenuState.SPEED -> {
                hideSpeedMenu().subscribe()
                FabMenuState.OFF
            }
            FabMenuState.SIZE -> {
                hideSizeMenu().subscribe()
                FabMenuState.OFF
            }
        }
        fadeOutMask.showIf(menuState != FabMenuState.OFF)
    }

    private fun showSpeedMenu() = Completable.mergeArray(
            fab.rotate(45f),
            longFab.slideIn(fabMiniSize).andThen(longLabel.fadeIn()),
            normalFab.slideIn(2 * fabMiniSize).andThen(normalLabel.fadeIn()),
            blitzFab.slideIn(3 * fabMiniSize).andThen(blitzLabel.fadeIn())
    )

    private fun showSizeMenu() = Completable.mergeArray(
            fab.rotate(45f),
            largeFab.slideIn(fabMiniSize).andThen(largeLabel.fadeIn()),
            mediumFab.slideIn(2 * fabMiniSize).andThen(mediumLabel.fadeIn()),
            smallFab.slideIn(3 * fabMiniSize).andThen(smallLabel.fadeIn())
    )

    private fun hideSpeedMenu() = Completable.mergeArray(
            fab.rotate(0f),
            longLabel.fadeOut().andThen(longFab.slideOut(fabMiniSize)),
            normalLabel.fadeOut().andThen(normalFab.slideOut(2 * fabMiniSize)),
            blitzLabel.fadeOut().andThen(blitzFab.slideOut(3 * fabMiniSize))
    )

    private fun hideSizeMenu() = Completable.mergeArray(
            fab.rotate(0f),
            largeLabel.fadeOut().andThen(largeFab.slideOut(fabMiniSize)),
            mediumLabel.fadeOut().andThen(mediumFab.slideOut(2 * fabMiniSize)),
            smallLabel.fadeOut().andThen(smallFab.slideOut(3 * fabMiniSize))
    )

    @OnClick(R.id.blitz_fab, R.id.long_fab, R.id.normal_fab)
    fun onSpeedClicked() {
        hideSpeedMenu()
                .doOnComplete { menuState = FabMenuState.SIZE }
                .andThen(showSizeMenu())
                .subscribe()
    }

    @OnClick(R.id.small_fab, R.id.medium_fab, R.id.large_fab)
    fun onSizeClicked() {
        hideSizeMenu().subscribe()
        menuState = FabMenuState.OFF
        fadeOutMask.showIf(false)
    }

}
