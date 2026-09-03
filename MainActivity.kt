package com.dittodeneroui.footballgame

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.content.Context
import kotlin.math.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        hideSystemUi()
        setContentView(FootballView(this))
    }

    private fun hideSystemUi() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}

class FootballView(context: Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val text = Paint(Paint.ANTI_ALIAS_FLAG)
    private var ballX = 0f
    private var ballY = 0f
    private var playerX = 0f
    private var playerY = 0f
    private var initialized = false
    private var scoreHome = 0
    private var scoreAway = 0
    private var message = "KICK OFF"
    private var lastDown = false

    init {
        text.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isFocusable = true
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val w = width.toFloat()
        val h = height.toFloat()
        if (!initialized) {
            playerX = w * .25f
            playerY = h * .55f
            ballX = w * .50f
            ballY = h * .55f
            initialized = true
        }

        // Pitch
        c.drawColor(Color.rgb(20, 125, 45))
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.color = Color.WHITE
        c.drawRect(w*.08f, h*.12f, w*.92f, h*.88f, paint)
        c.drawLine(w*.5f, h*.12f, w*.5f, h*.88f, paint)
        c.drawCircle(w*.5f, h*.5f, h*.13f, paint)
        c.drawRect(w*.08f, h*.36f, w*.20f, h*.64f, paint)
        c.drawRect(w*.80f, h*.36f, w*.92f, h*.64f, paint)

        // Players
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(30, 70, 210)
        c.drawCircle(playerX, playerY, 22f, paint)
        paint.color = Color.rgb(220, 45, 45)
        c.drawCircle(w*.72f, h*.50f, 22f, paint)

        // Ball
        paint.color = Color.WHITE
        c.drawCircle(ballX, ballY, 11f, paint)
        paint.color = Color.BLACK
        c.drawCircle(ballX, ballY, 3f, paint)

        // Score
        text.color = Color.WHITE
        text.textSize = 30f
        text.textAlign = Paint.Align.CENTER
        c.drawText("$scoreHome  -  $scoreAway", w*.5f, h*.08f, text)
        text.textSize = 18f
        c.drawText(message, w*.5f, h*.97f, text)

        // Controls
        paint.color = Color.argb(150, 0, 0, 0)
        c.drawCircle(w*.14f, h*.78f, 55f, paint)
        c.drawCircle(w*.86f, h*.78f, 55f, paint)
        c.drawCircle(w*.14f, h*.63f, 35f, paint)
        text.textSize = 26f
        c.drawText("MOVE", w*.14f, h*.79f, text)
        c.drawText("KICK", w*.86f, h*.79f, text)
        text.textSize = 16f
        c.drawText("▲", w*.14f, h*.65f, text)
    }

    override fun onTouchEvent(e: android.view.MotionEvent): Boolean {
        if (e.action == android.view.MotionEvent.ACTION_DOWN ||
            e.action == android.view.MotionEvent.ACTION_MOVE) {
            val w = width.toFloat()
            val h = height.toFloat()
            if (e.x < w*.30f && e.y > h*.50f) {
                playerX = e.x.coerceIn(w*.10f, w*.48f)
                playerY = e.y.coerceIn(h*.15f, h*.85f)
                ballX = playerX + 35f
                ballY = playerY
                message = "MOVING"
            } else if (e.x > w*.70f && e.y > h*.50f) {
                ballX += 85f
                if (ballX > w*.88f) {
                    scoreHome++
                    ballX = w*.50f
                    ballY = h*.50f
                    message = "GOAL!"
                } else {
                    message = "SHOT!"
                }
            }
            invalidate()
            return true
        }
        if (e.action == android.view.MotionEvent.ACTION_UP) {
            message = "KICK OFF"
            invalidate()
        }
        return true
    }
}
