package com.example.lendlyapp.ui.screens.register

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.lendlyapp.ui.shared.LendlyTopAppBar
import com.example.lendlyapp.ui.theme.FigmaLightBg
import com.example.lendlyapp.ui.theme.FigmaLightText
import com.example.lendlyapp.ui.theme.FigmaNeonGreen
import com.example.lendlyapp.ui.theme.FormLabel
import com.example.lendlyapp.ui.theme.InterFamily
import com.example.lendlyapp.ui.theme.OnPrimaryGreen
import com.example.lendlyapp.ui.theme.SubtitleGray

// ─── Signature Screen ────────────────────────────────────────────────────────
// Figma node: 164:1532 "Signature page" — 393×1068dp
// Background: #FCF8F8 (FigmaLightBg)
//
// Layout:
//   Top App Bar → headline → interactive signature canvas
//   → legal text → "Next" button

@Composable
fun SignatureScreen(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    // Signature drawing state
    var paths by remember { mutableStateOf(listOf<Path>()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FigmaLightBg)
            .statusBarsPadding(),
    ) {
        // ── Top App Bar ────────────────────────────────────────────────────
        LendlyTopAppBar(onBackClick = onBackClick)

        // ── Content ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            // ── Headline section ───────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                // Title — 28sp SemiBold
                Text(
                    text = "Let's seal the deal!",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    color = FigmaLightText,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle — 16sp Normal
                Text(
                    text = "You can use your finger or a compatible stylus to write your signature",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = FormLabel,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Interactive signature canvas (Frame 179) ───────────────────
            // 393×357dp, white background, captures finger/stylus drawing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(357.dp),
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val newPath = Path().apply {
                                        moveTo(offset.x, offset.y)
                                    }
                                    currentPath = newPath
                                },
                                onDrag = { change, _ ->
                                    currentPath?.lineTo(
                                        change.position.x,
                                        change.position.y,
                                    )
                                    currentPath?.let {
                                        paths = paths + listOf(
                                            Path().apply { addPath(it) },
                                        )
                                    }
                                },
                                onDragEnd = {
                                    currentPath?.let { paths = paths + it }
                                    currentPath = null
                                },
                            )
                        },
                ) {
                    paths.forEach { path ->
                        drawPath(
                            path,
                            color = Color.Black,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                            ),
                        )
                    }
                }

                // Pen/edit icon at top-right corner — 40×40dp
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Signature pen",
                    tint = FormLabel,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(40.dp),
                )

                // ── Canvas hint label (centered inside the canvas) ─────────
                // 14sp Medium, centered — matches Figma positioning
                Text(
                    text = "Sign here\n(same signature as with the\ndocument you provided)",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = SubtitleGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp),
                )
            }
        }

        // ── Bottom section with legal text + button ────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Legal confirmation text — above the button
            Text(
                text = "By tapping \"Next\", you confirm that the information you provided is true and correct.",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = FormLabel,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Next" button — same style as LendlyBottomBar
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FigmaNeonGreen,
                    contentColor = OnPrimaryGreen,
                ),
            ) {
                Text(
                    text = "Next",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
            }


        }
    }
}
