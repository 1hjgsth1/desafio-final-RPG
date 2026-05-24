package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.model.Guerreiro
import com.uniduo.desafio_final_rpg.model.Ladino
import com.uniduo.desafio_final_rpg.model.Mago
import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import kotlin.random.Random

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

        if (personagem.vida <= 0) {
            return "${personagem.nome} está derrotado e não pode mais atacar!"
        }

        val danoBase = when (personagem) {
            is Mago -> personagem.forca + personagem.magia
            else -> personagem.forca
        }

        var quantidadeAtaques = 1
        var mensagemEspecial = ""

        if (personagem is Ladino) {
            val chanceAtaqueDuplo = (personagem.sagacidade / 200.0).coerceIn(0.0, 0.80)

            if (Random.nextDouble() < chanceAtaqueDuplo) {
                quantidadeAtaques = 2
                mensagemEspecial = " A sagacidade ativou! O ladino atacou duas vezes."
            } else {
                mensagemEspecial = " A sagacidade não ativou dessa vez."
            }
        }

        val danoTotal = danoBase * quantidadeAtaques

        return try {
            restClient.post()
                .uri(rivalCombateUrl)
                .contentType(MediaType.TEXT_PLAIN)
                .body(danoTotal.toString())
                .retrieve()
                .toBodilessEntity()

            when (personagem) {
                is Mago -> "${personagem.nome} atacou como MAGO causando $danoTotal de dano. Força: ${personagem.forca} + Magia: ${personagem.magia}."
                is Ladino -> "${personagem.nome} atacou como LADINO causando $danoTotal de dano.$mensagemEspecial"
                is Guerreiro -> "${personagem.nome} atacou como GUERREIRO causando $danoTotal de dano."
                else -> "${personagem.nome} atacou causando $danoTotal de dano."
            }

        } catch (e: Exception) {
            "Erro ao atacar rival: ${e.message}"
        }
    }

    @PostMapping("/apanhar/{id}", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun apanhar(
        @PathVariable id: Long,
        @RequestBody danoTexto: String
    ): String {
        val personagem = personagemService.buscarPorId(id)

        if (personagem.vida <= 0) {
            return "${personagem.nome} já está derrotado. Nenhum dano adicional foi aplicado."
        }

        val danoRecebido = danoTexto.toDouble()

        val danoFinal = when (personagem) {
            is Guerreiro -> {
                val danoReduzido = danoRecebido - personagem.defesa

                if (danoReduzido < 0) {
                    0.0
                } else {
                    danoReduzido
                }
            }

            else -> danoRecebido
        }

        personagem.vida -= danoFinal

        if (personagem.vida < 0) {
            personagem.vida = 0.0
        }

        personagemService.salvar(personagem)

        return when {
            personagem is Guerreiro && personagem.vida <= 0 -> {
                "${personagem.nome} recebeu $danoRecebido de dano, mas sua defesa reduziu para $danoFinal. Mesmo assim, foi derrotado!"
            }

            personagem is Guerreiro -> {
                "${personagem.nome} recebeu $danoRecebido de dano, mas sua defesa de ${personagem.defesa} reduziu o dano para $danoFinal. Vida restante: ${personagem.vida}"
            }

            personagem.vida <= 0 -> {
                "${personagem.nome} recebeu $danoFinal de dano e foi derrotado!"
            }

            else -> {
                "${personagem.nome} recebeu $danoFinal de dano. Vida restante: ${personagem.vida}"
            }
        }
    }
}