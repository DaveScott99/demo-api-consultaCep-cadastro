package com.astro.labpadroesprojetospring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astro.labpadroesprojetospring.model.Cliente;
import com.astro.labpadroesprojetospring.model.Endereco;
import com.astro.labpadroesprojetospring.repository.ClienteRepository;
import com.astro.labpadroesprojetospring.repository.EnderecoRepository;
import com.astro.labpadroesprojetospring.service.ClienteService;
import com.astro.labpadroesprojetospring.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;

	
	/**
	 * Método para listar todos os registros no banco de dados.
	 *  @return registros cadastrados no banco.
	 */
    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

	/**
	 * Método para buscar um resgistro pelo id fornecido pelo usuário, caso não exista retornará um objeto nulo.
	 * @return o registro caso seja encontrado.
	 */
    @Override
    public Cliente buscarPorId(Long id) {
    	Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente == null){
			return null;
		}
		else {
			return cliente.get();
		}

    }

	/**
	 * Método para adicionar um novo registro ao banco de dados.
	 * @param cliente que será inserido.
	 */
    @Override
    public void inserir(Cliente cliente) { 
        salvarClienteComCep(cliente);
    }

    /**
	 * Método para atualizar um registro do banco de dados.
	 * @param id do registro que será alterado.
	 * @param cliente que será atualizado.
	 */
    @Override
    public void atualizar(Long id, Cliente cliente) {
    	
    	// Buscar Cliente por ID, caso exista: 
    	Optional<Cliente> clienteBd = clienteRepository.findById(id);
    	
    	if (clienteBd.isPresent()) {
    		salvarClienteComCep(cliente);
    	}
        
    }

	/**
	 * Método para deletar um registro do banco de dados.
	 * @param id do registro que será deletado.
	 */
    @Override
    public void deletar(Long id) {
    	clienteRepository.deleteById(id);
    }
    
	/**
	 * Método auxiliar para validar se já existe um cep e cliente cadastrado no banco de dados,
	 * se não é feito um novo cadastro desse cep e juntado ao cliente.
	 * @param cliente que será cadastrado no banco de dados.
	 */
	private void salvarClienteComCep(Cliente cliente) {

		
    	String cep = cliente.getEndereco().getCep(); // Verificar se o Endereco do Cliente já existe (pelo CEP).

    	Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
    		
        	// Caso não exista, integar com o ViaCEP e persistir o retorno.
    		Endereco novoEndereco = viaCepService.consultarCep(cep);
    		enderecoRepository.save(novoEndereco);
    		return novoEndereco;
    	});
    	
    	cliente.setEndereco(endereco);
    	
    	
    	// Inserir Cliente, vinculando o Endereço (novo ou existente).
    	clienteRepository.save(cliente);
	}
}
