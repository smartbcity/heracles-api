package io.civis.blockchain.coop.rest.i2.keycloak.config

// properties
const val I2_PREFIX = "i2"
const val JWT_ISSUER_URI = "$I2_PREFIX.jwt-issuer-uri"
const val JWK_SET_URI = "$I2_PREFIX.jwk-set-uri"

// conditional expressions
const val OPENID_REQUIRED_EXPRESSION = "!'\${${JWT_ISSUER_URI}:}'.isEmpty()"
const val JWKS_REQUIRED_EXPRESSION = "!'\${${JWK_SET_URI}:}'.isEmpty()"

const val AUTHENTICATION_REQUIRED_EXPRESSION = "($OPENID_REQUIRED_EXPRESSION) || ($JWKS_REQUIRED_EXPRESSION)"
const val NO_AUTHENTICATION_REQUIRED_EXPRESSION = "!($AUTHENTICATION_REQUIRED_EXPRESSION)"
