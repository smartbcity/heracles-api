package io.civis.blockchain.coop.rest.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(HeraclesConfigProps::class)
@ComponentScan("io.civis.blockchain.coop.rest")
class HeraclesAutoConfiguration(val coopConfig: HeraclesConfigProps) {

	@Bean
	fun builder(): FabricClientBuilder {
		return FabricClientBuilder(coopConfig)
	}

}
