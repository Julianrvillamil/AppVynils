package com.misw.appvynills

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ColeccionistaDetailTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailColeccionistaTest() {
        // Esperar a que la actividad se inicialice
        SystemClock.sleep(3000)

        // Click en el botón de navegación a coleccionistas
        onView(
            allOf(
                withId(R.id.navigation_notifications),
                withContentDescription("Coleccionistas"),
                isDisplayed()
            )
        ).perform(click())

        // Esperar a que cargue la lista
        SystemClock.sleep(2000)

        // Verificar que el RecyclerView está visible
        onView(withId(R.id.recyclerViewCollectors))
            .check(matches(isDisplayed()))

        // Click en el primer elemento del RecyclerView
        onView(withId(R.id.recyclerViewCollectors))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Esperar a que cargue el detalle
        SystemClock.sleep(2000)

        // Verificar elementos del detalle
        onView(withId(R.id.collector_name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_email))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_telephone))
            .check(matches(isDisplayed()))
    }
}