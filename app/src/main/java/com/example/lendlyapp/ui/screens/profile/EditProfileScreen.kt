package com.example.lendlyapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.*
import com.example.lendlyapp.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    // Observamos los estados de los campos desde el ViewModel
    val firstName by viewModel.firstNameInput
    val lastName by viewModel.lastNameInput
    val address by viewModel.addressInput
    val city by viewModel.cityInput
    val postalCode by viewModel.postalCodeInput
    val phone by viewModel.phoneInput

    Scaffold(
        topBar = {
            LendlyTopAppBar(
                onBackClick = onBack,
                showInfoIcon = false
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp).background(Color.White)) {
                Button(
                    onClick = { viewModel.updateProfile(onSuccess) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaNeonGreen)
                ) {
                    Text("Save", color = OnPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Enter your personal details",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFamily,
                lineHeight = 38.sp,
                color = FigmaLightText
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario de edición
            EditField("Full legal first and middle name(s)", firstName) { viewModel.onFirstNameChange(it) }
            EditField("Full legal last name", lastName) { viewModel.onLastNameChange(it) }

            Text("Date of birth", color = FormLabel, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { EditField("", "08") { } }
                Box(Modifier.weight(1f)) { EditField("", "12") { } }
                Box(Modifier.weight(1.5f)) { EditField("", "1997") { } }
            }

            EditField("Address", address) { viewModel.onAddressChange(it) }
            EditField("City", city) { viewModel.onCityChange(it) }
            EditField("Postal Code", postalCode) { viewModel.onPostalCodeChange(it) }

            Text("Phone Number", color = FormLabel, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.width(80.dp)) { EditField("", "+65") { } }
                Box(Modifier.weight(1f)) { EditField("", phone) { viewModel.onPhoneChange(it) } }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun EditField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        if (label.isNotEmpty()) {
            Text(text = label, color = FormLabel, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FigmaNeonGreen,
                unfocusedBorderColor = FigmaLightBg
            )
        )
    }
}


