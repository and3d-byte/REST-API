package com.algaworks.awpag.api.controller;

import com.algaworks.awpag.domain.exception.NegocioException;
import com.algaworks.awpag.domain.model.Cliente;
import com.algaworks.awpag.domain.repository.ClienteRepository;
import com.algaworks.awpag.domain.service.CadastroClienteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final CadastroClienteService cadastroClienteService;
    private final ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> listar(){
        return clienteRepository.findAll();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long clientId){
        Optional<Cliente> cliente = clienteRepository.findById(clientId);

        if(cliente.isPresent()){
            return ResponseEntity.ok(cliente.get());
        }

        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Cliente adicionar(@Valid @RequestBody Cliente cliente){
        return cadastroClienteService.salvar(cliente);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long clienteId, @Valid @RequestBody Cliente cliente){
        if (!clienteRepository.existsById(clienteId)){
            return ResponseEntity.notFound().build();
        }

        cliente.setId(clienteId);
        cliente = cadastroClienteService.salvar(cliente);

        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId){
        if (!clienteRepository.existsById(clienteId)){
            return ResponseEntity.notFound().build();
        }

        cadastroClienteService.excluir(clienteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<String> capturar(NegocioException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
