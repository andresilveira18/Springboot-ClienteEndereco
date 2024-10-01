package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    public void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("(11) 99999-9999");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 10));
    }

    @Test
    public void deveAdicionarClienteComSucesso() throws Exception {
        when(clienteService.salvarCliente(any(Cliente.class))).thenReturn(cliente);

        String json = objectMapper.writeValueAsString(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@gmail.com"));
    }

    @Test
    public void deveAtualizarClienteComSucesso() throws Exception {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Atualizado");
        clienteAtualizado.setEmail("joao.atualizado@gmail.com");
        clienteAtualizado.setCpf("123.456.789-00"); // Incluindo CPF
        clienteAtualizado.setTelefone("(11) 99999-9999"); // Incluindo telefone
        clienteAtualizado.setDataNascimento(LocalDate.of(1990, 5, 10)); // Incluindo data de nascimento
    
        when(clienteService.atualizarCliente(any(Long.class), any(Cliente.class))).thenReturn(clienteAtualizado);
    
        String json = objectMapper.writeValueAsString(clienteAtualizado);
    
        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao.atualizado@gmail.com"));
    }
    
    @Test
    public void deveListarTodosOsClientes() throws Exception {
        when(clienteService.listarClientes()).thenReturn(Arrays.asList(cliente));

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao.silva@gmail.com"));
    }

    @Test
    public void deveBuscarClientePorIdComSucesso() throws Exception {
        when(clienteService.buscarClientePorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@gmail.com"));
    }

    @Test
    public void deveRetornarNotFoundQuandoClienteNaoExistir() throws Exception {
        when(clienteService.buscarClientePorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveRemoverClienteComSucesso() throws Exception {
        mockMvc.perform(delete("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}


// 1. deveAdicionarClienteComSucesso: Testa se um novo cliente é adicionado corretamente com o status HTTP 201 Created.
// 2. deveAtualizarClienteComSucesso: Verifica se o cliente pode ser atualizado corretamente, garantindo que os campos modificados sejam refletidos no resultado.
// 3. deveListarTodosOsClientes: Testa a listagem de todos os clientes, verificando que o conteúdo da resposta contém as informações esperadas.
// 4. deveBuscarClientePorIdComSucesso: Garante que um cliente pode ser buscado corretamente pelo ID e retorna o status HTTP 200 OK.
// 5. deveRetornarNotFoundQuandoClienteNaoExistir: Verifica se o controller retorna 404 Not Found quando o cliente com o ID fornecido não existe.
// 6. deveRemoverClienteComSucesso: Testa a remoção de um cliente, garantindo que a resposta seja 204 No Content.