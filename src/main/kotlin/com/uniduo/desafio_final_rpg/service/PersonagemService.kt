package com.uniduo.desafio_final_rpg.service

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.repository.PersonagemRepository
import org.springframework.stereotype.Service

@Service
class PersonagemService(
    val personagemRepository: PersonagemRepository
) {

    fun salvar(personagem: Personagem) {
        if (personagem.nome.isNotBlank() || personagem.nome.isNotEmpty()) {
            //Se não tenho um Primary Key vou criar uma nova entidade
            //Se ja tiver um nome igual no banco, somente vai editar
            //Se tenho um Primary Key somente vou editar
            personagemRepository.save(personagem)
        }
    }

    fun buscarTodos(): List<Personagem> {
        return personagemRepository.findAll()

    }

    fun buscarPorNome(nome: String): Personagem {
        return personagemRepository.findById(nome).get()
    }

    fun excluirPersonagem(nome: String) {
        personagemRepository.deleteById(nome)
    }

    //Passar exemplos da Controladora

}