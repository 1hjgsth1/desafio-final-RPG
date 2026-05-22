package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient

@RestController
@RequestMapping("/combate")
class CombateController(
    @Value("\${rival.combate.url}")
    private val rivalCombateUrl: String,

    val restClient: RestClient,
    val personagemService: PersonagemService
) {

    @GetMapping("/atacar/{id}")
    fun atacar(@PathVariable id: Long): String {
        val personagem = personagemService.buscarPorId(id)
        val dano = personagem.forca

        return try {
            restClient.post()
                .uri(rivalCombateUrl)
                .contentType(MediaType.TEXT_PLAIN)
                .body(dano.toString())
                .retrieve()
                .toBodilessEntity()

            "${personagem.nome} atacou o rival causando $dano de dano"
        } catch (e: Exception) {
            "Erro ao atacar rival: ${e.message}"
        }
    }

    @PostMapping("/apanhar", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun apanhar(@RequestBody danoTexto: String): String {
        val dano = danoTexto.toDouble()

        return "Seu personagem recebeu $dano de dano"
    }
}