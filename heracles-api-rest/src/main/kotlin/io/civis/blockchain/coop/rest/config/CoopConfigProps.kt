package io.civis.blockchain.coop.rest.config

import io.civis.blockchain.coop.core.model.Endorser
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("coop")
class CoopConfigProps {
	lateinit var defaultChannel: String
	lateinit var channels: Map<ChannelId, ChannelConfig>

	class ChannelConfig {
		lateinit var endorsers: String
		lateinit var channel: String
		lateinit var ccid: String
		var user: UserConfig? = null
		var config: FileConfig? = null

		fun getEndorsers(): List<Endorser> {
			return endorsers.split(",").map {
				Endorser.fromStringPair(it)
			}
		}
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

typealias ChannelId = String
