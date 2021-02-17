package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.model.InvokeArgs
import io.civis.blockchain.coop.rest.config.ChainCodeId
import io.civis.blockchain.coop.rest.config.ChannelId
import io.civis.blockchain.coop.rest.config.FabricClientBuilder
import io.civis.blockchain.coop.rest.config.HeraclesConfigProps
import io.civis.blockchain.coop.rest.model.Cmd
import io.civis.blockchain.coop.rest.model.InvokeParams
import io.civis.blockchain.coop.rest.model.InvokeReturn
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class InvokeService(
	val coopConfigProps: HeraclesConfigProps,
	val fabricClientProvider: FabricClientProvider,
	val fabricClientBuilder: FabricClientBuilder
) {

	fun execute(channelId: ChannelId?, chainCodeId: ChainCodeId?, args: InvokeParams): CompletableFuture<String> {
		val chainCodePair = coopConfigProps.getChannelChaincodePair(channelId, chainCodeId)
		val invokeArgs = InvokeArgs(args.fcn, args.args.iterator())
		return when (args.cmd) {
			Cmd.invoke -> doInvoke(chainCodePair.channelId, chainCodePair.chainCodeId, invokeArgs)
			Cmd.query -> doQuery(chainCodePair.channelId, chainCodePair.chainCodeId, invokeArgs)
		}
	}

	private fun doQuery(
		channelId: ChannelId,
		chainCodeId: ChainCodeId,
		invokeArgs: InvokeArgs
	): CompletableFuture<String> {
		val client = fabricClientProvider.get(channelId)
		val channelConfig = fabricClientBuilder.getChannelConfig(channelId)
		val fabricChainCodeClient = fabricClientBuilder.getFabricChainCodeClient(channelId)
		return CompletableFuture.completedFuture(
			fabricChainCodeClient.query(channelConfig.endorsers, client, channelId, chainCodeId, invokeArgs)
		)
	}

	private fun doInvoke(
		channelId: ChannelId,
		chainCodeId: ChainCodeId,
		invokeArgs: InvokeArgs
	): CompletableFuture<String> {
		val client = fabricClientProvider.get(channelId)
		val channelConfig = fabricClientBuilder.getChannelConfig(channelId)
		val fabricChainCodeClient = fabricClientBuilder.getFabricChainCodeClient(channelId)
		val future = fabricChainCodeClient.invoke(channelConfig.endorsers, client, channelId, chainCodeId, invokeArgs)
		return future.thenApply {
			InvokeReturn("SUCCESS", "", it.transactionID).toJson()
		}
	}

}