package io.civis.blockchain.coop.rest.i2.keycloak.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class WebSecurityConfig {
    companion object {
        const val conditionalProperty = "i2.keycloak.realm"
    }

    @Bean
    @ConditionalOnProperty(conditionalProperty, matchIfMissing = true, havingValue = "false")
    fun dummyAuthenticationProvider(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange().anyExchange().permitAll()
        return http.build()
    }

    @Bean
    @ConditionalOnProperty(conditionalProperty, havingValue = "server")
    fun oauthAuthenticationProvider(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange()
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
        return http.build()
    }
}