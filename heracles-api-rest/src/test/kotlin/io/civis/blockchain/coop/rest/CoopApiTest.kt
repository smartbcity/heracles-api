package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.rest.model.Cmd
import io.civis.blockchain.coop.rest.model.ErrorResponse
import io.civis.blockchain.coop.rest.model.InvokeParams
import io.civis.blockchain.coop.rest.model.InvokeReturn
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap


class CoopApiTest : WebBaseTest() {

    @Test
    fun shouldReturnNotEmptyValue_WhenExecuteQuery() {
        val uri = baseUrl().pathSegment("/")
                .queryParam("cmd", "query")
                .queryParam("fcn", "query")
                .queryParam("args", "a")
                .build().toUri()

        val res = this.restTemplate.getForEntity(uri,String::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotEmpty
    }

    @Test
    fun shouldSUCCESSMessage_WhenInvokeWithFormUrlEncoded() {
        val uri = baseUrl().pathSegment("/").build().toUri()
        val map = LinkedMultiValueMap<String, String>()
        map.add("cmd", "invoke")
        map.add("fcn", "invoke")
        map.add("args", "a")
        map.add("args", "b")
        map.add("args", "10")
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val request = HttpEntity(map, headers)
        val res = this.restTemplate.postForEntity(uri, request, InvokeReturn::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.status).isEqualTo("SUCCESS")
        assertThat(res.body!!.transactionId).isNotEmpty
    }

    @Test
    fun shouldSUCCESSMessage_WhenInvokeWithJSON() {
        val uri = baseUrl().pathSegment("/").build().toUri()
        val params = InvokeParams(Cmd.invoke, "invoke", arrayOf("a", "b", "10"))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(params, headers)
        val res = this.restTemplate.postForEntity(uri, request, InvokeReturn::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.status).isEqualTo("SUCCESS")
        assertThat(res.body!!.transactionId).isNotEmpty
    }

    @Test
    fun `should fail when requesting invalid channelId`() {
        val channelId = "INVALID_CHANNEL_ID"
        val chainCodeId = "ex02"
        val uri = baseUrl().pathSegment("/").query("channelid=$channelId&chaincodeid=$chainCodeId").build().toUri()
        val params = InvokeParams(Cmd.invoke, "invoke", arrayOf("a", "b", "10"))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(params, headers)
        val res = this.restTemplate.postForEntity(uri, request, ErrorResponse::class.java)
        assertThat(res.statusCodeValue).isEqualTo(400)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.message).isEqualTo("Chaincode invoke error: Invalid INVALID_CHANNEL_ID/ex02")
    }

    @Test
    fun `should fail when requesting invalid chainCodeId`() {
        val channelId = "sandbox"
        val chainCodeId = "INVALID_CHAINCODE_ID"
        val uri = baseUrl().pathSegment("/").query("channelid=$channelId&chaincodeid=$chainCodeId").build().toUri()
        val params = InvokeParams(Cmd.invoke, "invoke", arrayOf("a", "b", "10"))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(params, headers)
        val res = this.restTemplate.postForEntity(uri, request, ErrorResponse::class.java)
        assertThat(res.statusCodeValue).isEqualTo(400)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.message).isEqualTo("Chaincode invoke error: Invalid sandbox/INVALID_CHAINCODE_ID")
    }

    @Test
    fun `should success when requesting valid channelId and chainCodeId`() {
        val channelId = "sandbox"
        val chainCodeId = "ex02"
        val uri = baseUrl().pathSegment("/").query("channelid=$channelId&chaincodeid=$chainCodeId").build().toUri()
        val params = InvokeParams(Cmd.invoke, "invoke", arrayOf("a", "b", "10"))
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val request = HttpEntity(params, headers)
        val res = this.restTemplate.postForEntity(uri, request, InvokeReturn::class.java)
        assertThat(res.statusCodeValue).isEqualTo(200)
        assertThat(res.body).isNotNull
        assertThat(res.body!!.status).isEqualTo("SUCCESS")
        assertThat(res.body!!.transactionId).isNotEmpty
    }

}