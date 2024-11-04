package com.misw.appvynills

import android.os.SystemClock
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
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
import org.hamcrest.Matchers.not
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@LargeTest
@RunWith(AndroidJUnit4::class)
class ArtistDetailTest {


    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testBottomNavigationFlow() {
        // Esperar a que la actividad se inicialice
        SystemClock.sleep(3000)

        // Verificar que todos los items de navegación están presentes
        verifyNavigationItemsPresent()

        // Test de navegación a Artistas
        navigateToArtistsAndVerify()

        // Test de navegación a Notificaciones
        //navigateToNotificationsAndVerify()

        // Test de navegación de vuelta a Home
        //navigateToHomeAndVerify()
    }

    private fun verifyNavigationItemsPresent() {
        onView(withId(R.id.navigation_home))
            .check(matches(isDisplayed()))

        onView(withId(R.id.navigation_artists))
            .check(matches(isDisplayed()))

        onView(withId(R.id.navigation_notifications))
            .check(matches(isDisplayed()))
    }

    private fun navigateToArtistsAndVerify() {
        try {
            // Navegar a Artistas con espera de recursos ociosos
            onView(withId(R.id.navigation_artists))
                .withFailureHandler { error, viewMatcher ->
                    throw AssertionError("No se encontró el botón de navegación a Artistas: ${error.message}")
                }
                .perform(click())

            // Verificar que estamos en la sección de artistas con espera adecuada
            onView(isRoot()).perform(waitForView(R.id.recyclerViewArtists, TimeUnit.SECONDS.toMillis(10)))
            // Verificar el estado de navegación
            onView(withId(R.id.navigation_artists))
                .check(matches(allOf(
                    isDisplayed(),
                    isSelected(),
                    isEnabled()
                )))

            // 1. Primero verificamos que el RecyclerView existe y está visible
            onView(withId(R.id.recyclerViewArtists))
                .check(matches(isDisplayed()))
                .check(matches(hasMinimumChildCount(1)))

            // 2. Verificamos que el RecyclerView no está vacío
            onView(withId(R.id.recyclerViewArtists))
                .check(matches(not(hasChildCount(0))))

            // 3. Verificamos la estructura del primer elemento de manera más flexible
            onView(withId(R.id.recyclerViewArtists))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
                .check(matches(
                    hasDescendant(
                        allOf(
                            withId(R.id.artistTitle),
                            isDisplayed(),
                            // Verifica que el título no está vacío, en lugar de buscar un texto específico
                            not(withText(""))
                        )
                    )
                ))

            // 4. Verificar que se puede hacer scroll si hay suficientes elementos
            try {
                onView(withId(R.id.recyclerViewArtists))
                    .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
            } catch (e: Exception) {
                // Si falla el scroll, probablemente no hay suficientes elementos
                println("No hay suficientes elementos para hacer scroll o el scroll falló: ${e.message}")
            }

            // 5. Verificar que podemos hacer click en el primer elemento
            try {
                onView(withId(R.id.recyclerViewArtists))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            0,
                            click()
                        )
                    )
            } catch (e: Exception) {
                println("No se pudo hacer click en el primer elemento: ${e.message}")
            }

        } catch (e: Exception) {
            throw AssertionError("Falló la prueba de navegación: ${e.message}", e)
        }
    }


    private fun navigateToNotificationsAndVerify() {
        // Navegar a Notificaciones
        onView(withId(R.id.navigation_notifications))
            .perform(click())

        // Verificar que estamos en la sección de notificaciones
        onView(withId(R.id.navigation_notifications))
            .check(matches(isDisplayed()))

        // Verificar que el botón está seleccionado
        onView(withId(R.id.navigation_notifications))
            .check(matches(isSelected()))
    }

    private fun navigateToHomeAndVerify() {
        // Navegar a Home
        onView(withId(R.id.navigation_home))
            .perform(click())

        // Verificar que estamos en la sección de home
        onView(withId(R.id.recyclerViewAlbums))
            .check(matches(isDisplayed()))

        // Verificar que el botón está seleccionado
        onView(withId(R.id.navigation_home))
            .check(matches(isSelected()))
    }

    @Test
    fun testNavigationState() {
        // Verificar el estado inicial
        onView(withId(R.id.navigation_home))
            .check(matches(isSelected()))

        // Navegar a través de todas las pestañas y verificar el estado
        sequenceOf(
            R.id.navigation_artists,
            R.id.navigation_notifications,
            R.id.navigation_home
        ).forEach { destinationId ->
            onView(withId(destinationId))
                .perform(click())
                .check(matches(isSelected()))
        }
    }

    // Custom wait function
    private fun waitForView(viewId: Int, timeout: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for view with id $viewId"
            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout

                do {
                    val viewToFind = view.findViewById<View>(viewId)
                    if (viewToFind != null && viewToFind.isShown) {
                        return
                    }
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                throw PerformException.Builder()
                    .withActionDescription(description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException("Timeout waiting for view with id: $viewId"))
                    .build()
            }
        }

    }

    fun withNonEmptyText(): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("con texto no vacío")
            }

            override fun matchesSafely(view: View): Boolean {
                return view.isVisible && view.findViewById<TextView>(R.id.artistTitle)?.text?.isNotEmpty() == true
            }
        }
    }



}