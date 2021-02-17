package io.civis.blockchain.coop.rest.config

import io.civis.blockchain.coop.core.model.Endorser

class ChannelChaincode(
	val channelId: ChannelId,
	val chaincodeId: List<ChainCodeId>,
	val user: HeraclesConfigProps.UserConfig,
	val config: HeraclesConfigProps.FileConfig,
	val endorsers: List<Endorser>
) {
	companion object
}


class ChannelChaincodePair(
	val channelId: ChannelId,
	val chainCodeId: ChainCodeId
) {
	companion object {
		fun fromConfig(defaultValue: String): ChannelChaincodePair {
			val ccidByChannel = defaultValue.split("/")
			require(ccidByChannel.size == 2) { "Bad ccid argument[${defaultValue}]. Syntax must by channelId/chaincodeId" }
			return ChannelChaincodePair(
				channelId = ccidByChannel[0],
				chainCodeId = ccidByChannel[1]
			)
		}
	}
}

typealias ChannelId = String
typealias ChainCodeId = String

fun ChannelChaincode.Companion.fromConfig(
	lines: Array<String>,
	user: HeraclesConfigProps.UserConfig,
	config: HeraclesConfigProps.FileConfig,
	endorsers: List<Endorser>
): Map<ChannelId, ChannelChaincode> {
	return lines.map { line ->
		ChannelChaincodePair.fromConfig(line)
	}.groupBy(
		{ it.channelId }, { it.chainCodeId }
	).mapValues { (channelId, chaincodeIds) ->
		ChannelChaincode(
			channelId = channelId,
			chaincodeId = chaincodeIds,
			user = user,
			config = config,
			endorsers = endorsers
		)

	}
}