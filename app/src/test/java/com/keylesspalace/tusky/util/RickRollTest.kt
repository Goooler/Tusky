package com.keylesspalace.tusky.util

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@Config(sdk = [28])
@RunWith(AndroidJUnit4::class)
class RickRollTest {
    private lateinit var activity: Activity

    @Before
    fun setupActivity() {
        val controller = Robolectric.buildActivity(Activity::class.java)
        activity = controller.get()
    }

    @Test
    fun testShouldRickRoll() {
        listOf("gab.Com", "social.gab.ai", "whatever.GAB.com").forEach {
                rollableDomain ->
            assertThat(shouldRickRoll(activity, rollableDomain)).isTrue()
        }

        listOf("chaos.social", "notgab.com").forEach {
                notRollableDomain ->
            assertThat(shouldRickRoll(activity, notRollableDomain)).isFalse()
        }
    }
}
