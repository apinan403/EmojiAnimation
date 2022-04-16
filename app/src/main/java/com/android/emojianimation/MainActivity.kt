package com.android.emojianimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Path
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.emojianimation.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityMainBinding
    private var reactArr: IntArray = intArrayOf(0, 0, 0, 0)
//    var amountReact: Int = 0

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.ButtonSubmitAmountReact.setOnClickListener {
            if (viewBinding.EdtLikeAmountReact.text.toString().isNotEmpty() &&
                viewBinding.EdtLoveAmountReact.text.toString().isNotEmpty() &&
                viewBinding.EdtLaughAmountReact.text.toString().isNotEmpty() &&
                viewBinding.EdtAngryAmountReact.text.toString().isNotEmpty()
            ) {

                reactArr[0] = viewBinding.EdtLikeAmountReact.text.toString().toInt()
                reactArr[1] = viewBinding.EdtLoveAmountReact.text.toString().toInt()
                reactArr[2] = viewBinding.EdtLaughAmountReact.text.toString().toInt()
                reactArr[3] = viewBinding.EdtAngryAmountReact.text.toString().toInt()
                checkReactAmount()
            }
        }

        viewBinding.ButtonIcLike.setOnClickListener {
            viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_like))
            flyEmoji()
        }

        viewBinding.ButtonIcLove.setOnClickListener {
            viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_love))
            flyEmoji()
        }

        viewBinding.ButtonIcLaugh.setOnClickListener {
            viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_laugh))
            flyEmoji()
        }

        viewBinding.ButtonIcAngry.setOnClickListener {
            viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_angry))
            flyEmoji()
        }
    }

    // loop ค่าใน reactArr โดยแทน index 0 = Like, 1 = Love, 2 = Laugh, 3 = Angry
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkReactAmount() {
            for ((index, value) in reactArr.withIndex()) {
                when (index) {
                    0 ->  viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_like))
                    1 ->  viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_love))
                    2 ->  viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_laugh))
                    3 ->  viewBinding.imageViewShowIc.setImageDrawable(getDrawable(R.drawable.ic_angry))
                    else -> {}
                }
                showReact(value)
            }
    }

    // loop แสดง animation ตามจำนวนที่ส่งมา
    private fun showReact(amount: Int) {
        var i = 0
        while (i < amount){
            flyEmoji()
            i++
        }
    }

    private fun flyEmoji() {
        // Disable clips on all parent generations
        disableAllParentsClip(viewBinding.imageViewShowIc)

        // Create clone
        val imageClone = cloneImage()

        // Animate
        animateFlying(imageClone)
        animateFading(imageClone)
    }

    private fun cloneImage(): ImageView {
        val clone = ImageView(this)
        clone.layoutParams = viewBinding.imageViewShowIc.layoutParams
        clone.setImageDrawable(viewBinding.imageViewShowIc.drawable)
        viewBinding.cloneContainer.addView(clone)
        return clone
    }

    private fun animateFlying(image: ImageView) {
        val posX = Random.nextInt(0, 300).toFloat()
        val posY = 0f
        val rnd = Random.nextInt(1000, 5000)
//        val sweepAngle = 20f

        val pathAnimate = Path().apply {
            if (rnd % 2 == 0) {
                moveTo(posX, posY)
                lineTo(-posX, posY - rnd)
                lineTo(-800f, posY - rnd)
//                arcTo(RectF(posX, posY - rnd, posX + 3 * rnd, posY + rnd), 180f, sweepAngle)
            } else {
                moveTo(-posX, posY)
                lineTo(posX, posY - rnd)
                lineTo(800f, posY - rnd)
//                arcTo(RectF(-posX - 3 * rnd, posY - rnd, -posX, posY + rnd), 0f, -sweepAngle)
            }
        }

        ObjectAnimator.ofFloat(image, View.X, View.Y, pathAnimate).apply {
            duration = 4000
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewBinding.imageViewShowIc.isVisible = false
                    viewBinding.imageViewShowIc.setImageResource(android.R.color.transparent)
                }
            })
        }
    }

    private fun animateFading(image: ImageView) {
        ObjectAnimator.ofFloat(image, "alpha", 1f, 0f).apply {
            duration = 2000
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewBinding.cloneContainer.removeView(image)
                }
            })
        }
    }

    private fun disableAllParentsClip(view: View) {
        var setView = view
        setView.parent?.let {
            while (setView.parent is ViewGroup) {
                val viewGroup = setView.parent as ViewGroup
                viewGroup.clipChildren = false
                viewGroup.clipToPadding = false
                setView = viewGroup
            }
        }
    }
}


/*
    ObjectAnimator.ofFloat(image, View.X, View.Y, path).apply {
        duration = 1500
        start()
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewBinding.imageViewShowIc.isVisible = false
                viewBinding.imageViewShowIc.setImageResource(android.R.color.transparent)
            }
        })
    }
*/


/*        val positionX = Path().apply {
            if (rnd % 2 == 0) {
                moveTo(posX, posY)
            } else  {
                moveTo(-posX, posY)
            }
        }

        val setAnimatePos = AnimatorSet()
        setAnimatePos.play(ObjectAnimator.ofFloat(image, View.X, View.Y, positionX))

        val animate = AnimatorSet()
        animate.play(
            ObjectAnimator.ofFloat(image, View.X, View.Y, pathAnimate)
                .apply {
                    duration = 4000
            }
        )

        AnimatorSet().apply {
            playSequentially(setAnimatePos, animate)
            start()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    viewBinding.imageViewShowIc.isVisible = false
                    viewBinding.imageViewShowIc.setImageResource(android.R.color.transparent)
                }
            })
        }*/


/*        image.animate()
    .alpha(0f)
    .setDuration(1500)
    .setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            viewBinding.cloneContainer.removeView(image)
        }
    })
*/


/*
val path = Path().apply {
    arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
}*/


/*
val anime = AnimatorSet()
anime.playTogether(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, "alpha", 1f, 0f),
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.X, View.Y, path),
)
anime.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator) {
        viewBinding.imageViewShowIc.isVisible = false
    }
})
anime.duration = 1500
anime.start()
*/


/*
val alphaAnime = AnimatorSet()
alphaAnime.play(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, "alpha", 1f, 0f),
)

val moveAnime = AnimatorSet()
moveAnime.play(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.X, View.Y, path),
)

val set = AnimatorSet()
    set.playTogether(moveAnime, alphaAnime)
set.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator) {
        viewBinding.imageViewShowIc.isVisible = false
    }
})
set.duration = 1500
set.start()
*/


/*
val animSet1 = AnimatorSet()
animSet1.playTogether(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_X, 50f),
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_Y, -100f)
)

val animSet2 = AnimatorSet()
animSet2.playTogether(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_X, -150f),
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_Y, -200f)
)

val animSet3 = AnimatorSet()
animSet3.playTogether(
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_X, 100f),
ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, View.TRANSLATION_Y, -300f),
//            ObjectAnimator.ofFloat(viewBinding.imageViewShowIc, "alpha", 1f, 0f)
)

val animSet = AnimatorSet()
animSet.playSequentially(animSet1, animSet2, animSet3)
animSet.duration = 500
animSet.start()

animSet.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
        animation.duration = 0
        viewBinding.imageViewShowIc.isVisible = false
        viewBinding.imageViewShowIc.setImageResource(android.R.color.transparent);
        viewBinding.imageViewShowIc.animate().translationX(0f).translationY(0f).alpha(1f)
    }
})
*/
