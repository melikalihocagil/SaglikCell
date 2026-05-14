package com.melikalihocagil.saglikcell.presentetion.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Giriş ekranı arayüzü.
 * Turkcell kurumsal renkleri ve modern Compose bileşenleri kullanılır.
 */
@Composable
fun AuthScreen(
    onNavigateToOtp: (String) -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Efektleri dinle (Navigasyon vb.)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthEffect.NavigateToOtp -> onNavigateToOtp(effect.phoneNumber)
                is AuthEffect.ShowSnackbar -> { /* Snackbar gösterimi SaglikCellAppScreen'de yönetiliyor */ }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo veya Uygulama Adı
        Text(
            text = "SağlıkCell",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Hoş Geldiniz",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Telefon Numarası Girişi
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { if (it.length <= 10) viewModel.onEvent(AuthEvent.OnPhoneNumberChange(it)) },
            label = { Text("Telefon Numarası") },
            placeholder = { Text("5XX XXX XX XX") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            prefix = { Text("+90 ") },
            singleLine = true,
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Giriş Butonu
        Button(
            onClick = { viewModel.onEvent(AuthEvent.OnLoginClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = uiState.isButtonEnabled && !uiState.isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Giriş Yap", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bilgilendirme Metni
        Text(
            text = "Giriş yaparak kullanım koşullarını kabul etmiş olursunuz.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
