package com.uniduo.desafio_final_rpg.controller

import com.uniduo.desafio_final_rpg.model.Personagem
import com.uniduo.desafio_final_rpg.service.PersonagemService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient


// @RestController combina @Controller + @ResponseBody
// Indica que essa classe é um controlador HTTP e que todos os métodos
// retornam dados direto no corpo da resposta (não renderiza páginas HTML)
@RestController
class PersonagemController(
    // @Value injeta o valor da propriedade "rival.url" do application.properties
    // Ex: rival.url=http://192.168.1.12:8080/ouvir
    //Meu = 10.10.7.234 //Arthur = 10.10.7.160 //Luiz = 10.10.7.235
    // Isso evita hardcodar IPs/URLs no código
    @Value("\${rival.url}") private val rivalUrl: String,

    // Injeção de dependência via construtor (padrão do Spring/Kotlin)
    // O Spring cria e gerencia as instâncias de PersonagemService e RestClient
    val personagemService: PersonagemService,

    // RestClient é o cliente HTTP do Spring (substituto moderno do RestTemplate)
    // Usado para fazer requisições HTTP para outros servidores
    val restClient: RestClient
) {
    // @GetMapping: este endpoint responde a requisições HTTP GET em "/msg"
    // Serve como o "gatilho" para enviar uma mensagem ao rival
    // Uso: GET http://localhost:8080/msg?mensagem=Olá
    @GetMapping("/msg")
    fun mandarMensagemParaPersonagemRival(
        // @RequestParam lê o parâmetro da URL: /msg?mensagem=...
        @RequestParam mensagem: String
    ) {
        try {
            restClient.post()                      // Define que será uma requisição HTTP POST
                .uri(rivalUrl)                     // Define o destino: URL do rival (application.properties)
                .contentType(MediaType.TEXT_PLAIN) // Informa ao servidor rival que o corpo é texto puro (text/plain)
                .body(mensagem)                    // Define o corpo da requisição com a mensagem
                .retrieve()                        // Dispara a requisição e prepara para ler a resposta
                .toBodilessEntity()                // Lê apenas os headers/status, ignora o corpo da resposta
        } catch (e: Exception) {
            // Captura qualquer erro de rede ou HTTP (ex: rival offline, connection refused)
            println("Deu erro: ${e.message}")
        }
    }

    // @PostMapping: este endpoint responde a requisições HTTP POST em "/ouvir"
    // É aqui que esta máquina RECEBE mensagens enviadas pelo rival
    // consumes: garante que só aceita requisições com Content-Type: text/plain
    // (rejeita outros formatos como JSON ou XML com erro 415 Unsupported Media Type)
    @PostMapping("/ouvir", consumes = [MediaType.TEXT_PLAIN_VALUE])
    fun receberMensagemDoPersonagemRival(
        // @RequestBody lê o corpo da requisição HTTP e mapeia para a variável
        @RequestBody mensagem: String
    ) {
        println("Mensagem recebida do rival: $mensagem")
    }

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

    @PostMapping("/salvar")
    fun salvarMeuPersonagem(@RequestBody personagem: Personagem) {
        personagemService.salvar(personagem)
    }
}