package com.uniduo.desafio_final_rpg.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
@RequestMapping("/chat")
class ChatController(
    // RestClient é o cliente HTTP do Spring (substituto moderno do RestTemplate)
    // Usado para fazer requisições HTTP para outros servidores
    val restClient: RestClient,
    // @Value injeta o valor da propriedade "rival.url" do application.properties
    // Ex: rival.url=http://192.168.1.12:8080/ouvir
    //Meu = 10.10.7.234 //Arthur = 10.10.7.160 //Luiz = 10.10.7.235
    // Isso evita hardcodar IPs/URLs no código
    @Value("\${rival.url}") private val rivalUrl: String,
) {
    // @GetMapping: este endpoint responde a requisições HTTP GET em "/msg"
    // Serve como o "gatilho" para enviar uma mensagem ao rival
    // Uso: GET http://localhost:8080/msg?mensagem=Olá
    @PostMapping("/msg", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun mandarMensagemParaPersonagemRival(@RequestBody mensagem: String): String {
        return try {
            restClient.post()
                .uri(rivalUrl)
                .contentType(MediaType.TEXT_PLAIN)
                .body(mensagem)
                .retrieve()
                .toBodilessEntity()

            "Mensagem enviada para o rival: $mensagem"
        } catch (e: Exception) {
            "Erro ao enviar mensagem: ${e.message}"
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
