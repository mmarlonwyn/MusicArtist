package com.example.musicartist.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppHeader() {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(34.dp).clip(CircleShape).background(MGreen), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.GraphicEq, null, tint = Color.Black, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text("Music Artist", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun MusicSearchBar(value: String, onChange: (String) -> Unit, onSearch: () -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onChange, singleLine = true,
        placeholder = { Text("Cari artis...", color = MMuted) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = MGreen) },
        trailingIcon = {
            if (value.isNotEmpty()) IconButton(onClick = { onChange("") }) {
                Icon(Icons.Default.Close, null, tint = MMuted)
            }
        },
        shape = RoundedCornerShape(50),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            focusedContainerColor = MSurface, unfocusedContainerColor = MSurface,
            focusedBorderColor = MGreen, unfocusedBorderColor = MSurface2, cursorColor = MGreen
        ),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun LoadingView(message: String? = null, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(color = MGreen)
        if (message != null) {
            Spacer(Modifier.height(16.dp))
            Text(message, color = MMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.ErrorOutline, null, tint = MRed, modifier = Modifier.size(48.dp))
        Spacer(Modifier.height(12.dp))
        Text("Terjadi Kendala", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        Text(message, color = MMuted, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry, shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MGreen, contentColor = Color.Black)) {
            Icon(Icons.Default.Refresh, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Coba Lagi", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Pill(text: String, modifier: Modifier = Modifier) {
    Text(text, color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
        modifier = modifier.clip(CircleShape).background(MSurface2).padding(horizontal = 12.dp, vertical = 6.dp))
}
