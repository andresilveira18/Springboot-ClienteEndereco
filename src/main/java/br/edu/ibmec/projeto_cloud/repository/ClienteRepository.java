package br.edu.ibmec.projeto_cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.projeto_cloud.model.Cliente;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Método para buscar cliente por email
    Optional<Cliente> findByEmail(String email);

    // Método para buscar cliente por CPF
    Optional<Cliente> findByCpf(String cpf);
}
