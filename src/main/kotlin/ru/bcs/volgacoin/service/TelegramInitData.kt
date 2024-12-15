package ru.bcs.volgacoin.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper


data class TelegramInitData(
    val hash:String,
    val data:Map<String,String>
){
    companion object{
        fun from(value:String):TelegramInitData{
            val initData = ObjectMapper().readValue(value, object : TypeReference<HashMap<String, String>>(){})
            val hash = initData.get("hash")?: throw Exception("No hash value provided")
            initData.remove("hash")
            return TelegramInitData(hash, initData)
        }
    }
}
