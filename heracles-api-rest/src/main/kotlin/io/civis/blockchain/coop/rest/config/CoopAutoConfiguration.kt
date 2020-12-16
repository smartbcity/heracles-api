package io.civis.blockchain.coop.rest.config

import io.civis.blockchain.coop.core.FabricChainCodeClient
import io.civis.blockchain.coop.core.FabricUserClient
import io.civis.blockchain.coop.core.config.FabricConfig
import io.civis.blockchain.coop.core.factory.FabricChannelFactory
import io.civis.blockchain.coop.core.factory.FabricClientFactory
import io.civis.blockchain.coop.rest.ChannelConfigNotFoundException
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CoopConfigProps::class)
@ComponentScan("io.civis.blockchain.coop.rest")
class CoopAutoConfiguration(val coopConfig: CoopConfigProps) {

	@Bean
	fun builder(): FabricClientBuilder {
		return FabricClientBuilder(coopConfig)
	}

}
