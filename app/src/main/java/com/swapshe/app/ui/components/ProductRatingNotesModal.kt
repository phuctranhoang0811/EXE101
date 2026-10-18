package com.swapshe.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import java.util.Locale

@Composable
fun ProductRatingNotesModal(
    isVisible: Boolean,
    productTitle: String,
    imageUrl: String,
    initialRating: Float = 0f,
    initialNotes: String = "",
    onDismiss: () -> Unit,
    onSave: (rating: Float, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    var selectedRating by remember(isVisible, initialRating) { mutableFloatStateOf(initialRating) }
    var notesText by remember(isVisible, initialNotes) { mutableStateOf(initialNotes) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier = modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {}
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // Header: Title & Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = productTitle,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF18181B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 12.dp)
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF18181B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Content: Left Image + Right Rating & Notes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Left Thumbnail Image
                        Box(
                            modifier = Modifier
                                .width(115.dp)
                                .aspectRatio(0.85f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF4F4F5))
                                .border(0.5.dp, Color(0xFFE4E4E7), RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = productTitle,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Right Column: Rating & Notes
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            // 1. Rating
                            Text(
                                text = "Rating",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF18181B)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                (1..5).forEach { starIndex ->
                                    val isSelected = starIndex <= selectedRating
                                    Icon(
                                        imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "$starIndex stars",
                                        tint = if (isSelected) Color(0xFF18181B) else Color(0xFFCBD5E1),
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                selectedRating = if (selectedRating == starIndex.toFloat()) 0f else starIndex.toFloat()
                                            }
                                            .padding(end = 2.dp)
                                    )
                                }

                                Text(
                                    text = if (selectedRating == 0f) "0.0/5" else String.format(Locale.US, "%.1f/5", selectedRating),
                                    fontSize = 13.sp,
                                    color = Color(0xFF4B5563),
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 2. Notes
                            Text(
                                text = "Notes",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF18181B)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            BasicTextField(
                                value = notesText,
                                onValueChange = { notesText = it },
                                textStyle = TextStyle(
                                    fontSize = 13.sp,
                                    color = Color(0xFF18181B),
                                    fontFamily = FontFamily.SansSerif
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(86.dp)
                                    .background(Color.White, RoundedCornerShape(6.dp))
                                    .border(1.dp, Color(0xFFE4E4E7), RoundedCornerShape(6.dp))
                                    .padding(8.dp),
                                decorationBox = { innerTextField ->
                                    innerTextField()
                                }
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 3. Save Button
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Button(
                                    onClick = { onSave(selectedRating, notesText) },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF18181B),
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(
                                        text = "Save",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
