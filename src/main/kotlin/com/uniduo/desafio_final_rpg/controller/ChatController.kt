package com.uniduo.desafio_final_rpg.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class ChatController(
    // @Value injeta o valor da propriedade "rival.url" do application.properties
    // Ex: rival.url=http://192.168.1.12:8080/ouvir
    // Isso evita hardcodar IPs/URLs no código
    @Value("\${rival.url}") private val rivalUrl: String,

    ) {
    // @GetMapping: este endpoint responde a requisições HTTP GET em "/msg"
    // Serve como o "gatilho" para enviar uma mensagem ao rival
    // Uso: GET http://localhost:8080/msg?mensagem=Olá
    @GetMapping("/msg")
    fun mandarMensagemParaPersonagemRival(
        @RequestParam mensagem: String
    ): String { // Adicione o tipo de retorno String
        return try {
            restClient.post()
                .uri(rivalUrl)
                .contentType(MediaType.TEXT_PLAIN)
                .body(mensagem)
                .retrieve()
                .toBodilessEntity()

            "Mensagem enviada com sucesso!" // Retorno em caso de sucesso
        } catch (e: Exception) {
            "Erro ao enviar: ${e.message}" // Retorno em caso de erro
        }
    }

    // @PostMapping: este endpoint responde a requisições HTTP POST em "/ouvir"
    // É aqui que esta máquina RECEBE mensagens enviadas pelo rival
    // consumes: garante que só aceita requisições com Content-Type: text/plain
    // (rejeita outros formatos como JSON ou XML com erro 415 Unsupported Media Type)
    @PostMapping("/ouvir", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun receberMensagemDoPersonagemRival(
        // @RequestBody lê o corpo da requisição HTTP e mapeia para a variável
        @RequestBody mensagem: String
    ) {
        println("Mensagem recebida do rival: $mensagem")
    }
}