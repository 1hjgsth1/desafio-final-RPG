package com.uniduo.desafio_final_rpg.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

// @Configuration indica ao Spring que essa classe é uma fonte de configurações
// O Spring a processa na inicialização para registrar os @Beans definidos nela
// É equivalente a um arquivo XML de configuração, mas em código Kotlin/Java
@Configuration
class ClientConfig {

    @Bean
    fun restClient(): RestClient {
        // RestClient.builder() oferece uma API fluente (encadeamento de métodos)
        // para configurar o cliente antes de construí-lo, ao contrário do
        // RestClient.create() que não permite nenhuma configuração prévia
        return RestClient.builder()
            // defaultHeader define um header enviado automaticamente em TODAS as requisições
            // "Accept" informa ao servidor rival qual formato de resposta esta máquina aceita
            // TEXT_PLAIN_VALUE = "text/plain" — combinando com o consumes do @PostMapping("/ouvir")
            .defaultHeader("Accept", MediaType.TEXT_PLAIN_VALUE)
            // build() finaliza a configuração e cria a instância do RestClient
            // A partir daqui o objeto está pronto para ser injetado pelo Spring
            .build()
    }
}