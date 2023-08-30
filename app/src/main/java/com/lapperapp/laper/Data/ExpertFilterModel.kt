package com.lapperapp.laper.Data

data class ExpertFilterModel (
    val field:String,
    val value:String,
    val sortField:String,
    val sort:Int,
    val lim:Int)