package com.dylanbeebe.fuelbuddy

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.ui.theme.FuelBuddyTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val userDAO = FuelBuddyDB.getDatabase(applicationContext).userDAO()

        setContent {
            FuelBuddyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    GreetingScreen(
//                        userDAO = userDAO,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(userID: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = "Hello user:",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
        Row {
            Text(
                text = userID,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun GreetingScreen(modifier: Modifier = Modifier) {
//    var userID by remember { mutableStateOf("Loading...") }

    // TODO: Remove this, create {User,Vehicle,Mileage}Repository and implement ViewModel for each.
//    LaunchedEffect(Unit) {
//        val existing = userDAO.getAll()
//        userID = if (existing.isEmpty()) {
//            val newUser = User(userID = UUID.randomUUID().toString())
//            userDAO.insert(newUser)
//            newUser.userID
//        } else {
//            existing.joinToString { it.userID }
//        }
//    }
//    Greeting(userID = userID, modifier = modifier)
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "GreetingPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    FuelBuddyTheme() {
        Greeting(userID = "preview-user-id")
    }
}