package com.icl.additivelist

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.icl.additivelist.data.PreferencesUtils
import com.icl.additivelist.testing.EspressoIdlingResource
import com.icl.additivelist.usescase.splash.SplashActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test para el flujo del tutorial en el primer arranque.
 * Este test garantiza que la secuencia Splash -> Tutorial -> Main funciona correctamente.
 */
@RunWith(AndroidJUnit4::class)
class TutorialFlowTest {

    @Before
    fun setUp() {
        // 1. Limpiamos TODAS las preferencias para simular un estado de "recién instalado".
        val context = ApplicationProvider.getApplicationContext<Context>()
        PreferencesUtils(context).clearAll()

        // 2. Registramos nuestro "semáforo" (IdlingResource) para que Espresso espere
        //    a la descarga de red antes de continuar.
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        // 3. Después del test, limpiamos el registro para no afectar a otros tests.
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun primerArranque_muestraTutorial_yLuegoNavegaAlMenuPrincipal() {
        // 4. Lanzamos la SplashActivity MANUALMENTE. Esto asegura que el @Before ya ha terminado.
        val scenario = ActivityScenario.launch(SplashActivity::class.java)

        // 5. Espresso ahora esperará automáticamente a que la descarga termine.
        //    Cuando continúa, verificamos que la pantalla del tutorial es visible.
        onView(withId(R.id.continueButton)).check(matches(isDisplayed()))

        // 6. Hacemos clic en el botón.
        onView(withId(R.id.continueButton)).perform(click())

        // 7. Verificamos que hemos llegado a la pantalla del menú principal.
        onView(withId(R.id.additivesImg)).check(matches(isDisplayed()))

        // Cerramos el escenario para limpiar recursos.
        scenario.close()
    }
}