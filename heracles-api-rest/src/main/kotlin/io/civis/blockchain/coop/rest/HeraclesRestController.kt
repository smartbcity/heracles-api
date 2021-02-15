package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.exception.InvokeException
import io.civis.blockchain.coop.rest.config.ChainCodeId
import io.civis.blockchain.coop.rest.config.ChannelId
import io.civis.blockchain.coop.rest.model.Cmd
import io.civis.blockchain.coop.rest.model.ErrorResponse
import io.civis.blockchain.coop.rest.model.InvokeParams
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
class HeraclesRestController(
	private val invokeService: InvokeService
) {
	companion object {
		const val CHANNEL_ID_URL_PARAM = "channelid"
		const val CHAINCODE_ID_URL_PARAM = "chaincodeid"
	}

	@GetMapping
	fun query(
		@RequestParam(CHANNEL_ID_URL_PARAM) channel: ChannelId?,
		@RequestParam(CHAINCODE_ID_URL_PARAM) chaincode: ChainCodeId?,
		cmd: Cmd,
		fcn: String,
		args: Array<String>
	): CompletableFuture<String> = invokeService.execute(channel, chaincode, InvokeParams(cmd, fcn, args))

	@PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
	fun invoke(
		@RequestParam(CHANNEL_ID_URL_PARAM) channel: ChannelId?,
		@RequestParam(CHAINCODE_ID_URL_PARAM) chaincode: ChainCodeId?,
		@ModelAttribute args: InvokeParams
	): CompletableFuture<String> = invokeService.execute(channel, chaincode, args)

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
	fun invokeJson(
		@RequestParam(CHANNEL_ID_URL_PARAM) channel: ChannelId?,
		@RequestParam(CHAINCODE_ID_URL_PARAM) chaincode: ChainCodeId?,
		@RequestBody args: InvokeParams
	): CompletableFuture<String> = invokeService.execute(channel, chaincode, args)

	@ExceptionHandler(InvokeException::class)
	fun handleException(invokeException: InvokeException): ResponseEntity<ErrorResponse> {
		val error = ErrorResponse("Chaincode invoke error: ${invokeException.message}")
		return ResponseEntity(error, HttpStatus.BAD_REQUEST)
	}

}