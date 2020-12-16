package io.civis.blockchain.coop.rest

import io.civis.blockchain.coop.core.exception.InvokeException
import io.civis.blockchain.coop.core.model.InvokeArgs
import io.civis.blockchain.coop.core.utils.JsonUtils
import io.civis.blockchain.coop.rest.config.ChannelId
import io.civis.blockchain.coop.rest.config.CoopConfigProps
import io.civis.blockchain.coop.rest.config.FabricClientBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
class HeraclesRestController(
    val coopConfigProps: CoopConfigProps,
    val fabricClientProvider: FabricClientProvider,
    val fabricClientBuilder: FabricClientBuilder
    ) {

    @GetMapping
    fun query(@RequestParam("channel") channelId: ChannelId?, cmd: Cmd, fcn: String, args: Array<String>): CompletableFuture<String>  = execute(channelId, InvokeParams(cmd, fcn, args))

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun invoke(@RequestParam("channel") channelId: ChannelId?, @ModelAttribute args: InvokeParams): CompletableFuture<String> = execute(channelId, args)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun invokeJson(@RequestParam("channel") channelId: ChannelId?,@RequestBody args: InvokeParams): CompletableFuture<String> = execute(channelId, args)

    fun execute(channelId: ChannelId?, args: InvokeParams): CompletableFuture<String> {
        val invokeArgs = InvokeArgs(args.fcn, args.args.iterator());
        if (Cmd.invoke.equals(args.cmd)) {
            return doInvoke(channelId, invokeArgs)
        } else {
            return doQuery(channelId, invokeArgs)
        }
    }

    @ExceptionHandler(*[InvokeException::class])
    fun handleException(invokeException: InvokeException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse("Chaincode invoke error: ${invokeException.message}")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    private fun doQuery(channelId: ChannelId? = coopConfigProps.defaultChannel, invokeArgs: InvokeArgs): CompletableFuture<String> {
        val chId = channelId ?: coopConfigProps.defaultChannel
        val client = fabricClientProvider.get(chId)
        val channelConfig = fabricClientBuilder.getChannelConfig(chId)
        val fabricChainCodeClient = fabricClientBuilder.getFabricChainCodeClient(chId)
        return CompletableFuture.completedFuture(
            fabricChainCodeClient.query(channelConfig.getEndorsers(), client, channelConfig.channel, channelConfig.ccid, invokeArgs)
        )
    }

    private fun doInvoke(channelId: ChannelId?, invokeArgs: InvokeArgs): CompletableFuture<String> {
        val chId = channelId ?: coopConfigProps.defaultChannel
        val client = fabricClientProvider.get(chId)
        val channelConfig = fabricClientBuilder.getChannelConfig(chId)
        val fabricChainCodeClient = fabricClientBuilder.getFabricChainCodeClient(chId)
        val future = fabricChainCodeClient.invoke(channelConfig.getEndorsers(), client, channelConfig.channel, channelConfig.ccid, invokeArgs)
        return future.thenApply {
            InvokeReturn("SUCCESS", "", it.transactionID).toJson()
        }
    }

    enum class Cmd {
        query, invoke
    }

    data class InvokeReturn(val status: String, val info: String, val transactionId: String){
        fun toJson() :String {
            return JsonUtils.toJson(this);
        }
    }

    data class InvokeParams(
        val cmd: Cmd,
        val fcn: String,
        val args: Array<String>
        )

}