package com.example.ejemplo1.api

import com.google.gson.annotations.SerializedName

data class RouteResponse(@SerializedName("features")val features:List<Feature>)
data class Feature(@SerializedName("geometry") val geometry:Geometry)
data class Geometry(@SerializedName("coordinates") val coordinates:List<List<Double>> )