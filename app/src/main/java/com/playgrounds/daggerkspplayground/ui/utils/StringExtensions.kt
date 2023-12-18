package com.playgrounds.daggerkspplayground.ui.utils

import androidx.compose.ui.unit.LayoutDirection

val String.detectDirection get() = if (this.contains(Regex("[\u0590-\u05FF]"))) LayoutDirection.Rtl else LayoutDirection.Ltr