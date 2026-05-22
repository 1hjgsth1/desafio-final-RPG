package com.uniduo.desafio_final_rpg.service

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.repository.PersonagemRepository
import org.springframework.stereotype.Service

@Service
class PersonagemService(
    val personagemRepository: PersonagemRepository
) {

    fun salvar(personagem: Personagem): Personagem {
        return personagemRepository.save(personagem)
    }

    fun buscarTodos(): List<Personagem> {
        return personagemRepository.findAll()
    }

    fun buscarPorId(id: Long): Personagem {
        return personagemRepository.findById(id)
            .orElseThrow { RuntimeException("Personagem não encontrado com id: $id") }
    }

    fun editar(id: Long, personagemAtualizado: Personagem): Personagem {
        val personagemExistente = buscarPorId(id)

        personagemExistente.nome = personagemAtualizado.nome
        personagemExistente.forca = personagemAtualizado.forca
        personagemExistente.velocidade = personagemAtualizado.velocidade
        personagemExistente.vida = personagemAtualizado.vida

        return personagemRepository.save(personagemExistente)
    }

    fun excluirPersonagem(id: Long) {
        personagemRepository.deleteById(id)
    }
}