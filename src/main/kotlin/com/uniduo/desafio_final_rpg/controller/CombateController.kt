package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestClient

class CombateController(
    // @Value injeta o valor da propriedade "rival.url" do application.properties
    // Ex: rival.url=http://192.168.1.12:8080/ouvir
    // Isso evita hardcodar IPs/URLs no código
    @Value("\${rival.url}") private val rivalUrl: String,
    // RestClient é o cliente HTTP do Spring (substituto moderno do RestTemplate)
    // Usado para fazer requisições HTTP para outros servidores
    val restClient: RestClient,
    val personagemService: PersonagemService

) {

    @GetMapping("/atacar")
    fun atacar(poder: Int) {
        println("Estou atacando meu rival")
        try {
            restClient.post()
                .uri(rivalUrl).contentType(MediaType.TEXT_PLAIN)
                .body(poder.toString()).retrieve().toBodilessEntity()
        } catch (e: Exception) {
            println("Deu erro: ${e.message}")
        }
    }

    @PostMapping("/apanhar", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun apanhar(@RequestBody poder: Int) {
        println("Seu personagem perdeu: $poder de vida")
    }

}