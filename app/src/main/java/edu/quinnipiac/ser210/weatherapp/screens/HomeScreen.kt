package edu.quinnipiac.ser210.weatherapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    MainContent(navController = navController)
}

@Composable
fun MainContent (
    navController: NavController,
    countryList: List<Country> = CountryList
) {
    CountryColumn(
        countryList = countryList,
        navController = navController,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CountryColumn(
    countryList: List<Country>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        itemsIndexed(items = countryList) { index, item ->
            CountryCard(
                country = item,
                modifier = Modifier
                    .padding(8.dp)
            ) { country ->
                navController.navigate(route = CountryScreens.DetailScreen.name+"/$country")
            }
        }
    }
}

@Composable
fun CountryCard(country: Country, modifier: Modifier = Modifier, onItemClick: (String) -> Unit = {}) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 0.dp)
                .clickable {
                    onItemClick(country.name)
                }
        ) {
            FlagImage(country.flag, modifier)
            CountryInfo(country.name, country.currency, modifier)
        }
    }
}

@Composable
fun FlagImage(@DrawableRes flag: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(flag),
        contentDescription = "Flag",
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(56.dp)
    )
}

@Composable
fun CountryInfo(countryName: String, currency: String, modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(start = 8.dp)
            .height(48.dp)
    ) {
        Text(
            text = "Country: $countryName",
            fontSize = 22.sp,
            lineHeight = 16.sp
        )
        Text(
            text = "Currency: $currency",
            fontSize = 16.sp,
            lineHeight = 12.sp
        )
    }
}