package com.uniduo.desafio_final_rpg.repository

import com.uniduo.desafio_final_rpg.model.Personagem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonagemRepository : JpaRepository<Personagem, String>