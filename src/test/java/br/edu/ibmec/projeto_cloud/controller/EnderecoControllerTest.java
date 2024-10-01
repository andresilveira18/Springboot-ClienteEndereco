package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Endereco;
import br.edu.ibmec.projeto_cloud.service.EnderecoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
public class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnderecoService enderecoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Endereco endereco;
    private Cliente cliente;

    @BeforeEach
    public void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setBairro("Bairro Central");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
    }

    @Test
    public void deveAdicionarEnderecoComSucesso() throws Exception {
        when(enderecoService.salvarEndereco(any(Endereco.class))).thenReturn(endereco);

        String json = objectMapper.writeValueAsString(endereco);

        mockMvc.perform(post("/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rua").value("Rua Exemplo"))
                .andExpect(jsonPath("$.cliente.id").value(1L));
    }

    @Test
    public void deveRemoverEnderecoComSucesso() throws Exception {
        mockMvc.perform(delete("/enderecos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deveListarEnderecosPorCliente() throws Exception {
        List<Endereco> enderecos = Arrays.asList(endereco);
        when(enderecoService.listarEnderecosPorCliente(anyLong())).thenReturn(enderecos);

        mockMvc.perform(get("/enderecos/cliente/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rua").value("Rua Exemplo"))
                .andExpect(jsonPath("$[0].cliente.id").value(1L));
    }

    @Test
    public void deveAtualizarEnderecoComSucesso() throws Exception {
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");
        enderecoAtualizado.setNumero("321");
        enderecoAtualizado.setBairro("Bairro Atualizado");
        enderecoAtualizado.setCidade("Rio de Janeiro");
        enderecoAtualizado.setEstado("RJ");
        enderecoAtualizado.setCep("87654-321");
        enderecoAtualizado.setCliente(cliente);

        when(enderecoService.atualizarEndereco(anyLong(), anyLong(), any(Endereco.class))).thenReturn(enderecoAtualizado);

        String json = objectMapper.writeValueAsString(enderecoAtualizado);

        mockMvc.perform(put("/enderecos/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rua").value("Rua Nova"))
                .andExpect(jsonPath("$.bairro").value("Bairro Atualizado"))
                .andExpect(jsonPath("$.cidade").value("Rio de Janeiro"));
    }

    @Test
    public void deveRemoverEnderecoPorClienteComSucesso() throws Exception {
        mockMvc.perform(delete("/enderecos/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
