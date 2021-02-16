package io.civis.blockchain.coop.rest.i2.keycloak.config

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata

class SecurityExclusionFilter : AutoConfigurationImportFilter {
	private val SHOULD_SKIP = setOf(
		"org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration",
		"org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration"
	)

	override fun match(classNames: Array<String?>, metadata: AutoConfigurationMetadata?): BooleanArray {
		return classNames.map { className -> className !in SHOULD_SKIP }
			.toBooleanArray()
	}
}
