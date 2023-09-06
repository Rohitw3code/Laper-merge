package com.lapperapp.laper.PSRequest

data class GetRequestModel(val _id:String,
                           val requiredTech:ArrayList<String>,
                           val requestTime:String,
                           val accepted:Boolean,
                           val clientId:String,
                           val expertId:String,
                           val problemSolved:Boolean,
                           val requestId:String,
                           val problemStatement:String,
                           val __v:Int
)