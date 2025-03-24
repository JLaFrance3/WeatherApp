/**
 * Weather App
 * Authors: Jean LaFrance and Grant Foody
 * Class: SER210 - Software Engineering Design and Development
 * Weather application designed to allow for searching locations to obtain
 * current weather and forecasts
 */

package edu.quinnipiac.ser210.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.quinnipiac.ser210.weatherapp.navigation.WeatherAppNavigation
import edu.quinnipiac.ser210.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                WeatherAppNavigation()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    WeatherAppTheme {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    MyApp {
        WeatherAppNavigation()
    }
}