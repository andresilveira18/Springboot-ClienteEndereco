package br.edu.ibmec.projeto_cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.projeto_cloud.repository.EnderecoRepository;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import br.edu.ibmec.projeto_cloud.model.Endereco;
import br.edu.ibmec.projeto_cloud.model.Cliente;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para salvar um novo endereço associado a um cliente
    public Endereco salvarEndereco(Endereco endereco) {
        if (endereco.getCliente() == null || endereco.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }
    
        Long clienteId = endereco.getCliente().getId();
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    
        // Associa o cliente ao endereço
        endereco.setCliente(cliente);
    
        return enderecoRepository.save(endereco);
    }
    
    // Método para atualizar um endereço associado a um cliente
    public Endereco atualizarEndereco(Long clienteId, Long enderecoId, Endereco enderecoAtualizado) {
        Endereco enderecoExistente = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        // Verifica se o endereço pertence ao cliente
        if (!enderecoExistente.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Endereço não pertence ao cliente informado.");
        }

        // Atualiza os dados do endereço
        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoRepository.save(enderecoExistente);
    }

    // Método para remover um endereço associado a um cliente
    public void removerEnderecoPorCliente(Long clienteId, Long enderecoId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        // Verifica se o endereço pertence ao cliente
        if (!endereco.getCliente().getId().equals(clienteId)) {
            throw new IllegalArgumentException("Endereço não pertence ao cliente informado.");
        }

        enderecoRepository.delete(endereco);
    }

    // Método para listar endereços associados a um cliente
    public List<Endereco> listarEnderecosPorCliente(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId);
    }

    // Método para remover um endereço pelo seu ID
    public void removerEndereco(Long id) {
        enderecoRepository.deleteById(id);
    }
}
