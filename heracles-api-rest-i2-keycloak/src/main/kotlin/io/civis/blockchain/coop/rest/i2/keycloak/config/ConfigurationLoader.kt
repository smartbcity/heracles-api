package io.civis.blockchain.coop.rest.i2.keycloak.config

import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Configuration
@AutoConfigureBefore(WebSecurityConfig::class)
@Import(ConfigurationLoaderOpenId::class, ConfigurationLoaderJwks::class)
class ConfigurationLoader

@Configuration
@ConditionalOnProperty(JWT_ISSUER_URI)
@PropertySource("classpath:i2-openid.properties")
class ConfigurationLoaderOpenId

@Configuration
@ConditionalOnProperty(JWK_SET_URI)
@PropertySource("classpath:i2-jwks.properties")
class ConfigurationLoaderJwks