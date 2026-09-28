package com.design.practice.domain.repository

import android.graphics.Bitmap
import com.design.practice.domain.model.Classification

interface LandmarkClassifier {

    fun classify(bitmap : Bitmap,rotation : Int) : List<Classification>
}