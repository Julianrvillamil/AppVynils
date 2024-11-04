package com.misw.appvynills

import android.os.SystemClock
import android.view.View
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
class ListArtistsTest {


    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testBottomNavigationFlow() {
        // Esperar a que la actividad se inicialice
        SystemClock.sleep(2000)

        // Verificar que todos los items de navegación están presentes
        verifyNavigationItemsPresent()

        // Test de navegación a Artistas
        navigateToArtistsAndVerify()

        // Test de navegación a Notificaciones
        navigateToNotificationsAndVerify()

        // Test de navegación de vuelta a Home
        navigateToHomeAndVerify()
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
            // Navigate to Artists with wait for idle resources
            onView(withId(R.id.navigation_artists))
                .withFailureHandler { error, viewMatcher ->
                    throw AssertionError("Failed to find Artists navigation button: ${error.message}")
                }
                .perform(click())

            // Verify we're in the artists section with a proper wait
            onView(isRoot()).perform(waitForView(R.id.recyclerViewArtists, TimeUnit.SECONDS.toMillis(5)))

            // Verify navigation state
            onView(withId(R.id.navigation_artists))
                .check(matches(allOf(
                    isDisplayed(),
                    isSelected(),
                    isEnabled()
                )))

            // Wait for the RecyclerView to load
            onView(isRoot()).perform(waitForView(R.id.recyclerViewArtists, TimeUnit.SECONDS.toMillis(5)))

            // Verify RecyclerView is present and visible
            onView(withId(R.id.recyclerViewArtists))
                .check(matches(isDisplayed()))

            // Check first item structure with better error handling

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

                try {
                    onView(withId(R.id.recyclerViewArtists))
                        .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
                } catch (e: Exception) {
                    // Si falla el scroll, probablemente no hay suficientes elementos
                    println("No hay suficientes elementos para hacer scroll o el scroll falló: ${e.message}")
                }

        } catch (e: Exception) {
            throw AssertionError("Navigation test failed: ${e.message}", e)
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


}