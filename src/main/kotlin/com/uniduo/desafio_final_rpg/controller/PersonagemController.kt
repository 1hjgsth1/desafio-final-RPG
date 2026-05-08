package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient

@RestController
class PersonagemController(
    @Value("\${rival.url}") private val rivalUrl: String,
    val personagemService: PersonagemService,
    val restClient: RestClient //Essa injeção é necessario para MANDAR mensagem
) {

    @GetMapping("/msg")
    fun mandarMensagemParaPersonagemRival(@RequestParam mensagem: String): ResponseEntity<String> {
        //Para mandar algo preciso configurar as rotas
        //Meu = 10.10.7.234
        //Arthur = 10.10.7.160
        //Luiz = 10.10.7.235

        try {
            restClient.post().uri(rivalUrl)
                .body(mensagem)
                .retrieve()
                .toBodilessEntity()
        } catch (e: Exception) {
            println("Deu erro: ${e.message}")
        }
        return TODO("Provide the return value")
    }

    @PostMapping("/ouvir")
    fun receberMensagemDoPersonagemRival(@RequestBody mensagem: String) {
        println("Mensagem recebida do rival: $mensagem")
    }

    @PostMapping("/ataque")
    fun atacar() {
        /**
         * Logica aqui
         */
    }
}