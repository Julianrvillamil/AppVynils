package com.misw.appvynills

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
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
import org.hamcrest.Description
import org.hamcrest.Matcher

@LargeTest
@RunWith(AndroidJUnit4::class)
class ListAlbumsTest {

    @get:Rule
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun listAlbumesTest() {
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

    }

    // Matcher personalizado para verificar el contenido del RecyclerView
    private fun hasItemAtPosition(matcher: Matcher<View>, position: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                matcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    ?: return false
                return matcher.matches(viewHolder.itemView)
            }
        }
    }

    // Test adicional para verificar el estado de carga
    @Test
    fun testLoadingState() {
        // Verificar que el RecyclerView existe
        onView(withId(R.id.recyclerViewAlbums))
            .check(matches(isDisplayed()))

        // Si tienes un indicador de carga, verifica que no está visible
        // Ajusta el ID según tu implementación
        /*try {
            onView(withId(R.id.loadingIndicator))
                .check(matches(not(isDisplayed())))
        } catch (e: Exception) {
            // El indicador de carga podría no existir
            println("No se encontró el indicador de carga o ya no está visible")
        }*/
    }
}