package com.uniduo.desafio_final_rpg.service

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.repository.PersonagemRepository
import org.springframework.stereotype.Service

@Service
class PersonagemService(
    val personagemRepository: PersonagemRepository
) {
    /**
     * CRUD do personagem
     * */
    //Salvar
    fun salvar(personagem: Personagem): Personagem {
        //Se não tenho um Primary Key vou criar uma nova entidade
        //se já tiver um nome igual no banco, somente vai editar
        //Se tenho um Primary Key somente vou editar
        return personagemRepository.save(personagem)
    }

    //Buscar
    fun buscarTodos(): List<Personagem> {
        return personagemRepository.findAll()
    }

    fun buscarPorNome(nome: String): Personagem {
        return personagemRepository.findById(nome).get()
    }

    //Excluir
    fun excluirPersonagem(nome: String) {
        personagemRepository.deleteById(nome)
    }

    //PASSAR OS EXEMPLOS DA CONTROLADORA
}