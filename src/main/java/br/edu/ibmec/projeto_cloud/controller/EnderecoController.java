package br.edu.ibmec.projeto_cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import br.edu.ibmec.projeto_cloud.service.EnderecoService;
import br.edu.ibmec.projeto_cloud.model.Endereco;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    // Endpoint para adicionar um novo endereço
    @PostMapping
    public ResponseEntity<Endereco> adicionarEndereco(@Valid @RequestBody Endereco endereco) {
        Endereco novoEndereco = enderecoService.salvarEndereco(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
    }

    // Endpoint para remover um endereço existente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerEndereco(@PathVariable Long id) {
        enderecoService.removerEndereco(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para listar endereços associados a um cliente
    @GetMapping("/cliente/{clienteId}")
    public List<Endereco> listarEnderecosPorCliente(@PathVariable Long clienteId) {
        return enderecoService.listarEnderecosPorCliente(clienteId);
    }

    // Endpoint para Atualizar um endereço específico
    @PutMapping("/{clienteId}/{enderecoId}")
    public ResponseEntity<Endereco> atualizarEndereco(
        @PathVariable Long clienteId, 
        @PathVariable Long enderecoId, 
        @Valid @RequestBody Endereco enderecoAtualizado) {
        
        Endereco endereco = enderecoService.atualizarEndereco(clienteId, enderecoId, enderecoAtualizado);
        return ResponseEntity.ok(endereco);
    }

    // Endpoint para Remover um endereço associado a um cliente
    @DeleteMapping("/{clienteId}/{enderecoId}")
    public ResponseEntity<Void> removerEnderecoPorCliente(
        @PathVariable Long clienteId, 
        @PathVariable Long enderecoId) {
        
        enderecoService.removerEnderecoPorCliente(clienteId, enderecoId);
        return ResponseEntity.noContent().build();
    }


}
