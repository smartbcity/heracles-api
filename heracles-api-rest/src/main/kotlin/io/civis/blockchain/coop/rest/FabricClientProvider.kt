package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.FabricUserClient
import io.civis.blockchain.coop.core.config.FabricConfig
import io.civis.blockchain.coop.core.factory.FabricClientFactory
import io.civis.blockchain.coop.rest.config.ChannelId
import io.civis.blockchain.coop.rest.config.CoopConfigProps
import io.civis.blockchain.coop.rest.config.FabricClientBuilder
import org.hyperledger.fabric.sdk.HFClient
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("singleton")
class FabricClientProvider(
	val fabricClientBuilder: FabricClientBuilder,
) {

	private var client: MutableMap<ChannelId, HFClient> = mutableMapOf()

	fun get(channelId: ChannelId): HFClient {
		return client.getOrPut(channelId, {
			build(channelId)
		})
	}

	private fun build(channelId: ChannelId): HFClient {
		val config = fabricClientBuilder.getChannelConfig(channelId)
		val clientFactory = fabricClientBuilder.getFabricClientFactory(channelId)
		val user = fabricClientBuilder.getFabricUserClient(channelId).enroll(config.user!!.name, config.user!!.password, config.user!!.org)
		return clientFactory.getHfClient(user)
	}

}

class ChannelConfigNotFoundException(channelId: ChannelId) : Exception(channelId)

