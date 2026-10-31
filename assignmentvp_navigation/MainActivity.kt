package com.vp.assignmentvp_navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vp.assignmentvp_navigation.navigation.CampusApp
import com.vp.assignmentvp_navigation.ui.theme.AssignmentVP_NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentVP_NavigationTheme {
                CampusApp()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CampusAppPreview() {
    AssignmentVP_NavigationTheme() {
        CampusApp()
    }
}
