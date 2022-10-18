package com.astro.labpadroesprojetospring.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.astro.labpadroesprojetospring.model.Endereco;

/**
 * Classe para consultar a API ViaCep, e fazer uma requisição de um cep.
 */
@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

    @RequestMapping(method = RequestMethod.GET, value = "/{cep}/json/") // Requisição feita a API.
    Endereco consultarCep(@PathVariable("cep") String cep); // Chegada da requisição e criação de um objeto do tipo Endereço.

}
