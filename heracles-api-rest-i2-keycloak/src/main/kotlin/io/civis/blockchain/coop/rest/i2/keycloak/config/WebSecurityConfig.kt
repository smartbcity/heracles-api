package io.civis.blockchain.coop.rest.i2.keycloak.config

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authorization.AuthorizationContext
import reactor.core.publisher.Mono
import java.util.HashMap

@AutoConfigureBefore(ConditionalReactiveOAuth2ResourceServerAutoConfiguration::class)
@Configuration
class WebSecurityConfig {

    @Bean
    @ConfigurationProperties(prefix = "i2.filter")
    fun authFilter(): Map<String, String> = HashMap()

    @Bean("springSecurityFilterChain")
    @ConditionalOnExpression(NO_AUTHENTICATION_REQUIRED_EXPRESSION)
    fun dummyAuthenticationProvider(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange().anyExchange().permitAll()
        return http.build()
    }

    @Bean("springSecurityFilterChain")
    @ConditionalOnExpression(AUTHENTICATION_REQUIRED_EXPRESSION)
    fun oauthAuthenticationProvider(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange()
                .anyExchange()
                .access(::authenticate)
                .and()
                .oauth2ResourceServer()
                .jwt()
        return http.build()
    }

    private fun authenticate(authentication: Mono<Authentication>, context: AuthorizationContext): Mono<AuthorizationDecision> {
        return authentication.map { auth ->
            if (auth !is JwtAuthenticationToken || auth.token == null) {
                return@map false
            }

            val filters = authFilter()
            filters.isEmpty() || filters.all { (key, value) -> auth.token.claims[key] == value }
        }.map(::AuthorizationDecision)
    }
}