package com.bancario.service;

import java.util.List;
import java.util.Optional;

import com.bancario.dto.ClienteDTO;

public interface ClienteService {

	ClienteDTO crearCliente(ClienteDTO clienteDTO);

    List<ClienteDTO> getAllClientes();

    Optional<ClienteDTO> getClienteById(Long id);

    Optional<ClienteDTO> getClienteByIdentificacion(String identificacion);

    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);

    void eliminarCliente(Long id);

    List<ClienteDTO> buscarPorNombre(String nombre);
}
