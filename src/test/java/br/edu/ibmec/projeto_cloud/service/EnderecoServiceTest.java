package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Endereco;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarEnderecoComClienteExistente() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setCliente(cliente);
        endereco.setRua("Rua Exemplo");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        // Execução
        Endereco resultado = enderecoService.salvarEndereco(endereco);

        // Verificações
        assertNotNull(resultado);
        assertEquals("Rua Exemplo", resultado.getRua());
        assertEquals(1L, resultado.getCliente().getId());
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    public void deveLancarExcecaoQuandoClienteNaoExistir() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(2L);

        Endereco endereco = new Endereco();
        endereco.setCliente(cliente);

        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        // Execução e Verificação
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            enderecoService.salvarEndereco(endereco);
        });

        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    public void deveLancarExcecaoQuandoClienteForNulo() {
        Endereco endereco = new Endereco();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.salvarEndereco(endereco);
        });

        assertEquals("O cliente é obrigatório.", ex.getMessage());
    }

    @Test
    public void deveAtualizarEnderecoComSucesso() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(1L);
        enderecoExistente.setCliente(cliente);
        enderecoExistente.setRua("Rua Antiga");

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Atualizada");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoExistente));
        when(enderecoRepository.save(enderecoExistente)).thenReturn(enderecoExistente);

        // Execução
        Endereco resultado = enderecoService.atualizarEndereco(1L, 1L, enderecoAtualizado);

        // Verificações
        assertNotNull(resultado);
        assertEquals("Rua Atualizada", resultado.getRua());
        verify(enderecoRepository, times(1)).save(enderecoExistente);
    }

    @Test
    public void deveLancarExcecaoQuandoEnderecoNaoPertenceAoCliente() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cliente outroCliente = new Cliente();
        outroCliente.setId(2L);

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(1L);
        enderecoExistente.setCliente(outroCliente);

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Atualizada");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoExistente));

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.atualizarEndereco(1L, 1L, enderecoAtualizado);
        });

        assertEquals("Endereço não pertence ao cliente informado.", ex.getMessage());
    }

    @Test
    public void deveRemoverEnderecoComSucesso() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCliente(cliente);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Execução
        enderecoService.removerEnderecoPorCliente(1L, 1L);

        // Verificações
        verify(enderecoRepository, times(1)).delete(endereco);
    }

    @Test
    public void deveLancarExcecaoQuandoEnderecoNaoPertenceAoClienteAoRemover() {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cliente outroCliente = new Cliente();
        outroCliente.setId(2L);

        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCliente(outroCliente);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Execução e Verificação
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            enderecoService.removerEnderecoPorCliente(1L, 1L);
        });

        assertEquals("Endereço não pertence ao cliente informado.", ex.getMessage());
    }
}


// 1. Salvar Endereço com Cliente Existente: Testa se o endereço é salvo corretamente quando um cliente válido é associado.
// 2. Exceção quando Cliente Não Existe: Garante que uma exceção é lançada se o cliente associado ao endereço não existir no banco de dados.
// 3. Exceção quando Cliente é Nulo: Verifica se uma exceção é lançada quando o cliente não é fornecido.
// 4. Atualizar Endereço com Sucesso: Testa se o endereço é atualizado corretamente quando o cliente está associado.
// 5. Exceção quando Endereço Não Pertence ao Cliente: Garante que uma exceção é lançada se o endereço não pertencer ao cliente informado.
// 6. Remover Endereço com Sucesso: Testa se um endereço é removido corretamente para um cliente válido.
// 7. Exceção quando Remover Endereço de Outro Cliente: Garante que uma exceção é lançada se tentar remover um endereço que não pertence ao cliente informado.