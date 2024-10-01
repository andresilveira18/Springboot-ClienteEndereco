package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarClienteComSucesso() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 10));

        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Execução
        Cliente resultado = clienteService.salvarCliente(cliente);

        // Verificações
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 10));

        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.salvarCliente(cliente);
        });

        assertEquals("Email já cadastrado", ex.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoCpfJaCadastrado() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 10));

        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.salvarCliente(cliente);
        });

        assertEquals("CPF já cadastrado", ex.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoIdadeMenorQue18Anos() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        // Define a data de nascimento que gera uma idade menor que 18
        cliente.setDataNascimento(LocalDate.now().minusYears(17));

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.salvarCliente(cliente);
        });

        assertEquals("O cliente deve ter no mínimo 18 anos", ex.getMessage());
    }

    @Test
    public void deveAtualizarClienteComSucesso() {
        // Dados de entrada
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNome("João Silva");
        clienteExistente.setEmail("joao.silva@gmail.com");
        clienteExistente.setCpf("123.456.789-00");
        clienteExistente.setDataNascimento(LocalDate.of(1990, 5, 10));

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Atualizado");
        clienteAtualizado.setEmail("joao.atualizado@gmail.com");
        clienteAtualizado.setCpf("987.654.321-00");
        clienteAtualizado.setDataNascimento(LocalDate.of(1985, 7, 15));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // Execução
        Cliente resultado = clienteService.atualizarCliente(1L, clienteAtualizado);

        // Verificações
        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
        assertEquals("joao.atualizado@gmail.com", resultado.getEmail());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void deveLancarExcecaoQuandoClienteNaoEncontradoParaAtualizacao() {
        // Dados de entrada
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Atualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.atualizarCliente(1L, clienteAtualizado);
        });

        assertEquals("Cliente não encontrado", ex.getMessage());
    }
}


// 1. Salvar Cliente com Sucesso: Verifica se um cliente é salvo corretamente quando não há conflitos de email ou CPF já cadastrados. 
// 2. Exceção quando Email Já Cadastrado: Garante que uma exceção é lançada se o email do cliente já estiver cadastrado no sistema. 
// 3. Exceção quando CPF Já Cadastrado: Garante que uma exceção é lançada se o CPF do cliente já estiver cadastrado no sistema. 
// 4. Exceção quando Idade Menor que 18 Anos: Verifica se uma exceção é lançada quando o cliente tem menos de 18 anos de idade. 
// 5. Atualizar Cliente com Sucesso: Testa se um cliente existente é atualizado corretamente com novos dados. 
// 6. Exceção quando Cliente Não Encontrado para Atualização: Garante que uma exceção é lançada se tentar atualizar um cliente inexistente.