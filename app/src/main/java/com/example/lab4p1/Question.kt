package com.example.lab4p1

import androidx.annotation.StringRes

data class Question(@StringRes val textResId:
                    Int, val answer: Boolean)