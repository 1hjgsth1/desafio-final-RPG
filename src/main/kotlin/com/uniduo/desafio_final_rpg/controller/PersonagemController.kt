package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

// @RestController combina @Controller + @ResponseBody
// Indica que essa classe é um controlador HTTP e que todos os métodos
// retornam dados direto no corpo da resposta (não renderiza páginas HTML)
@RestController
class PersonagemController(

    // Injeção de dependência via construtor (padrão do Spring/Kotlin)
    // O Spring cria e gerencia as instâncias de PersonagemService e RestClient
    val personagemService: PersonagemService,

    ) {

    @PostMapping("/salvar")
    fun salvarMeuPersonagem(@RequestBody personagem: Personagem) {
        personagemService.salvar(personagem)
    }
}