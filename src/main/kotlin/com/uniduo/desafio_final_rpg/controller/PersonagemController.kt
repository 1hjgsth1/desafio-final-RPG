package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/personagens")
class PersonagemController(
    val personagemService: PersonagemService
) {

    @PostMapping
    fun salvarMeuPersonagem(@RequestBody personagem: Personagem): Personagem {
        return personagemService.salvar(personagem)
    }

    @GetMapping
    fun listarPersonagens(): List<Personagem> {
        return personagemService.buscarTodos()
    }

    @GetMapping("/{id}")
    fun buscarPersonagemPorId(@PathVariable id: Long): Personagem {
        return personagemService.buscarPorId(id)
    }

    @PutMapping("/{id}")
    fun editarPersonagem(
        @PathVariable id: Long,
        @RequestBody personagem: Personagem
    ): Personagem {
        return personagemService.editar(id, personagem)
    }

    @DeleteMapping("/{id}")
    fun excluirPersonagem(@PathVariable id: Long): String {
        personagemService.excluirPersonagem(id)
        return "Personagem excluído com sucesso"
    }
}