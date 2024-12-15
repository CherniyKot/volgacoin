package ru.bcs.volgacoin.api

import org.springframework.web.bind.annotation.RestController


@RestController
interface ClickController {
    fun addClicks(){

    }
}

class ClickControllerImpl:ClickController{

}