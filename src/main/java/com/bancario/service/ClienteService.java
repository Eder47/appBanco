package com.bancario.service;

import com.bancario.dto.ClienteDTO;
import com.bancario.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente crearCliente(ClienteDTO clienteDTO);

    List<Cliente> getAllClientes();

    Optional<Cliente> getClienteById(Long id);

    Optional<Cliente> getClienteByIdentificacion(String identificacion);

    Cliente actualizarCliente(Long id, ClienteDTO clienteDTO);

    void eliminarCliente(Long id);

    List<Cliente> buscarPorNombre(String nombre);
}
