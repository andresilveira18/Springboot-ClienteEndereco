package br.edu.ibmec.projeto_cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.model.Cliente;

import java.util.Optional;
import java.util.List;

import java.time.LocalDate;
import java.time.Period;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Método para salvar um novo cliente
    public Cliente salvarCliente(Cliente cliente) {
        validarIdade(cliente.getDataNascimento());
        verificarUnicidade(cliente.getEmail(), cliente.getCpf());
        return clienteRepository.save(cliente);
    }

    // Método para atualizar um cliente existente
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());

        return clienteRepository.save(clienteExistente);
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Método para buscar um cliente por ID
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Método para remover um cliente
    public void removerCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    // Valida a idade mínima de 18 anos
    private void validarIdade(LocalDate dataNascimento) {
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade < 18) {
            throw new IllegalArgumentException("O cliente deve ter no mínimo 18 anos");
        }
    }

    // Verifica se o email e o CPF já estão cadastrados
    private void verificarUnicidade(String email, String cpf) {
        Optional<Cliente> clienteComEmail = clienteRepository.findByEmail(email);
        Optional<Cliente> clienteComCpf = clienteRepository.findByCpf(cpf);

        if (clienteComEmail.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (clienteComCpf.isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    }
}
