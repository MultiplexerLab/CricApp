import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemAnimation : RecyclerView.ItemAnimator() {

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        viewHolder.itemView.alpha = 0f
        val animator = ObjectAnimator.ofFloat(viewHolder.itemView, "alpha", 0f, 1f)
        animator.apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    dispatchAnimationStarted(viewHolder)
                }

                override fun onAnimationEnd(animation: Animator) {
                    dispatchAnimationFinished(viewHolder)
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
        animator.start()
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        return false
    }

    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        return false
    }

    override fun runPendingAnimations() {}
    override fun endAnimation(item: RecyclerView.ViewHolder) {}
    override fun endAnimations() {}
    override fun isRunning(): Boolean {
        return false
    }
}
