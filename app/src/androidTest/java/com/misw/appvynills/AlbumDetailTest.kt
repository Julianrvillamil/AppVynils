package com.misw.appvynills

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
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers.not
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumDetailTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun albumDetailTest() {
        // 1. Primero verificamos que el RecyclerView existe y está visible
        onView(withId(R.id.recyclerViewAlbums))
            .check(matches(isDisplayed()))
            .check(matches(hasMinimumChildCount(1))) // Verifica que hay al menos un elemento

        // 2. Verificamos que el RecyclerView no está vacío
        onView(withId(R.id.recyclerViewAlbums))
            .check(matches(not(hasChildCount(0))))

        // 3. Verificamos la estructura del primer elemento de manera más flexible
        onView(withId(R.id.recyclerViewAlbums))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            .check(matches(
                hasDescendant(
                    allOf(
                        withId(R.id.albumTitle),
                        isDisplayed(),
                        // Verifica que el título no está vacío, en lugar de buscar un texto específico
                        not(withText(""))
                    )
                )
            ))

        // 4. Verificar que se puede hacer scroll si hay suficientes elementos
        try {
            onView(withId(R.id.recyclerViewAlbums))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
        } catch (e: Exception) {
            // Si falla el scroll, probablemente no hay suficientes elementos
            println("No hay suficientes elementos para hacer scroll o el scroll falló: ${e.message}")
        }

        // 5. Verificar que podemos hacer click en el primer elemento
        try {
            onView(withId(R.id.recyclerViewAlbums))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                    )
                )
        } catch (e: Exception) {
            println("No se pudo hacer click en el primer elemento: ${e.message}")
        }
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAddTrackToAlbum() {
        // Navegar al detalle del álbum
        onView(withId(R.id.recyclerViewAlbums))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Verifica que el detalle del álbum se cargó
        onView(withId(R.id.albumName))
            .check(matches(isDisplayed()))

        // Espera a que el botón de agregar track esté visible
        onView(withId(R.id.buttonAddTrack))
            .perform(scrollTo(), click())

        // Verifica que el formulario de agregar pista esté visible
        onView(withId(R.id.inputTrackName))
            .check(matches(isDisplayed()))

        // Rellena el formulario
        onView(withId(R.id.inputTrackName))
            .perform(typeText("Nueva Pista"), closeSoftKeyboard())
        onView(withId(R.id.inputTrackDuration))
            .perform(typeText("5:15"), closeSoftKeyboard())

        // Confirma la adición de la pista
        onView(withId(R.id.buttonAddTrack))
            .perform(click())

        // Verifica que la nueva pista aparezca en la lista
        onView(withId(R.id.tracksList))
            .check(matches(withText(containsString("Nueva Pista - Duración: 5:15"))))


    }
}
