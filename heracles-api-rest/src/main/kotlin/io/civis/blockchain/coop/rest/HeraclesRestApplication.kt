package io.civis.blockchain.coop.rest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [HeraclesRestApplication::class] )
class HeraclesRestApplication

fun main(args: Array<String>) {
	runApplication<HeraclesRestApplication>(*args)
}