package com.anna.entrebicisanna.Recompenses.UI.Screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anna.entrebicisanna.Core.Models.Recompensa
import com.anna.entrebicisanna.R
import com.anna.entrebicisanna.Usuaris.UI.ViewModel.UsuariViewModel

@Composable
fun RecompensaCard(
    recompensa: Recompensa,
    usuariViewModel: UsuariViewModel,
    onClickDetalls: () -> Unit // acció quan es clica el botó +
) {
    val blau = colorResource(id = R.color.blau)
    val imageBitmap = recompensa.imatgeRecompensa?.let {
        usuariViewModel.base64ToBitmap(it)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = blau),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                // Imatge
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Foto de recompensa",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder_perfil),
                        contentDescription = "Imatge no disponible",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Contingut textual
                    Column(
                        modifier = Modifier
                            .padding(end = 40.dp) // deixem espai a la dreta pel botó
                    ) {
                        Text(
                            text = recompensa.descripcio,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Punt bescanvi: ${recompensa.nomPuntBescanvi}")
                        Text("Saldo: ${recompensa.saldoNecessari} p.")
                        Text("Estat: ${recompensa.estatRecompensa}")
                    }

                    // Botó de detalls
                    IconButton(
                        onClick = onClickDetalls,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 16.dp) // abaixa el botó dins la card
                            .size(32.dp)
                            .background(Color(0xFFF8DADA), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Veure detalls",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }}

    fun base64ToBitmap(base64: String?): Bitmap? {
        return try {
            if (base64.isNullOrBlank()) return null
            val netBase64 = base64.substringAfter("base64,", base64) // per si porta prefix
            val imageBytes = Base64.decode(netBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        } catch (e: Exception) {
            Log.e("Base64", "Error decodificant imatge: ${e.message}")
            null
        }
    }