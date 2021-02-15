package io.civis.blockchain.coop.rest.model

import io.civis.blockchain.coop.core.utils.JsonUtils

data class InvokeReturn(val status: String, val info: String, val transactionId: String) {
	fun toJson(): String {
		return JsonUtils.toJson(this);
	}
}
