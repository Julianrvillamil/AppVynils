package com.misw.appvynills

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test

class AlbumAddTrack {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

   /* @Test
    fun testAddAlbum() {
        // Presionar botón flotante
        onView(withId(R.id.fabCreateAlbum))
            .perform(click())

        // Verifica que el formulario de agregar álbum aparece
        onView(withId(R.id.inputName))
            .check(matches(isDisplayed()))

        // Rellenar campos del álbum
        onView(withId(R.id.inputName))
            .perform(typeText("Nuevo Álbum"), closeSoftKeyboard())
        onView(withId(R.id.inputCover))
            .perform(typeText("http://example.com/cover.jpg"), closeSoftKeyboard())
        onView(withId(R.id.inputReleaseDate))
            .perform(typeText("2024-01-01"), closeSoftKeyboard())
        onView(withId(R.id.inputDescription))
            .perform(typeText("Descripción del álbum"), closeSoftKeyboard())

        // Confirmar agregar álbum
        onView(withId(R.id.buttonSubmit))
            .perform(click())

        // Verifica que el nuevo álbum aparece en la lista
        onView(withId(R.id.recyclerViewAlbums))
            .check(matches(hasDescendant(withText("Nuevo Álbum"))))
    }*/

}