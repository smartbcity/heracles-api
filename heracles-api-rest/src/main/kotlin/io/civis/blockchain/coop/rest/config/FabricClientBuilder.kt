package io.civis.blockchain.coop.rest.config

import io.civis.blockchain.coop.core.FabricChainCodeClient
import io.civis.blockchain.coop.core.FabricUserClient
import io.civis.blockchain.coop.core.config.FabricConfig
import io.civis.blockchain.coop.core.factory.FabricChannelFactory
import io.civis.blockchain.coop.core.factory.FabricClientFactory
import io.civis.blockchain.coop.rest.ChannelConfigNotFoundException

class FabricClientBuilder(val coopConfig: HeraclesConfigProps) {

	fun getChannelConfig(channelId: ChannelId): ChannelChaincode {
		return coopConfig.getChannelChaincodes().get(channelId)
			?: throw ChannelConfigNotFoundException(channelId)
	}

	fun getFabricConfig(channelId: ChannelId): FabricConfig {
		val channelConfig = getChannelConfig(channelId)
		return FabricConfig.loadFromFile(channelConfig.config.file)
	}

	fun getFabricClientFactory(channelId: ChannelId): FabricClientFactory {
		val channelConfig = getChannelConfig(channelId)
		val fabricConfig = getFabricConfig(channelId)
		return FabricClientFactory.factory(fabricConfig, channelConfig.config.crypto)
	}

	fun getFabricChannelFactory(channelId: ChannelId): FabricChannelFactory {
		val channelConfig = getChannelConfig(channelId)
		val fabricConfig = getFabricConfig(channelId)
		return FabricChannelFactory.factory(fabricConfig, channelConfig.config.crypto)
	}

	fun getFabricChainCodeClient(channelId: ChannelId): FabricChainCodeClient {
		val fabricChannelFactory = getFabricChannelFactory(channelId)
		return FabricChainCodeClient(fabricChannelFactory) }

	fun getFabricUserClient(channelId: ChannelId): FabricUserClient {
		val fabricConfig = getFabricConfig(channelId)
		val fabricClientFactory = getFabricClientFactory(channelId)
		return FabricUserClient(fabricConfig, fabricClientFactory)
	}

}