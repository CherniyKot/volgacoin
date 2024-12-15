package ru.bcs.volgacoin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VolgacoinApplication

fun main(args: Array<String>) {
	runApplication<VolgacoinApplication>(*args)
}
