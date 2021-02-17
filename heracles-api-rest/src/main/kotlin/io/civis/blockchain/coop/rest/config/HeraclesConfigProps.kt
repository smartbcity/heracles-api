package io.civis.blockchain.coop.rest.config

import io.civis.blockchain.coop.core.exception.InvokeException
import io.civis.blockchain.coop.core.model.Endorser
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("coop")
class HeraclesConfigProps {
	companion object {
		const val CCID_SEPARATOR = "/"
	}
	lateinit var defaultCcid: String
	lateinit var ccid: Array<String>

	var user: UserConfig? = null
	var config: FileConfig? = null
	lateinit var endorsers: String

	fun getEndorsers(): List<Endorser> {
		return endorsers.split(",").map { endorserValue ->
			Endorser.fromStringPair(endorserValue)
		}
	}

	fun getChannelChaincodes(): Map<ChannelId, ChannelChaincode> {
		val user = requireNotNull(user) { "Bad user[${user}] in application.yml" }
		val config = requireNotNull(config) { "Bad config[${config}] in application.yml" }
		return ChannelChaincode.fromConfig(ccid, user, config, getEndorsers())
	}

	fun getChannelChaincodePair(channelId: String?, chainCodeId: String?): ChannelChaincodePair {
		if(channelId == null && chainCodeId == null) {
			 return ChannelChaincodePair.fromConfig(defaultCcid)
		}

		val givenCcid = "$channelId$CCID_SEPARATOR$chainCodeId"
		if(!ccid.contains(givenCcid)) {
			throw InvokeException(listOf("Invalid $givenCcid"))
		}
		return ChannelChaincodePair(
			channelId = channelId!!,
			chainCodeId = chainCodeId!!
		)
	}

	class UserConfig {
		lateinit var name: String
		lateinit var password: String
		lateinit var org: String
	}

	class FileConfig {
		lateinit var file: String
		lateinit var crypto: String
	}



}
