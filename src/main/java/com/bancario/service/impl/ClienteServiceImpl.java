package com.bancario.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancario.dto.ClienteDTO;
import com.bancario.model.Cliente;
import com.bancario.repository.ClienteRepository;
import com.bancario.service.ClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final ModelMapper mapper;

	private final Predicate<Cliente> clienteActivoPredicate = cliente -> cliente.getEstado() != null
			&& cliente.getEstado();

	@Transactional
	public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
		if (clienteRepository.existsByIdentificacion(clienteDTO.getIdentificacion())) {
			throw new RuntimeException("Identificación ya registrada");
		}

		Cliente cliente = mapper.map(clienteDTO, Cliente.class);
		Cliente clienteGuardado = clienteRepository.save(cliente);

		return mapper.map(clienteGuardado, ClienteDTO.class);
	}

	@Transactional(readOnly = true)
	public List<ClienteDTO> getAllClientes() {
		return clienteRepository.findAll().stream().filter(clienteActivoPredicate)
				.map(cliente -> mapper.map(cliente, ClienteDTO.class)).toList();
	}

	@Transactional(readOnly = true)
	public Optional<ClienteDTO> getClienteById(Long id) {
		return clienteRepository.findById(id).filter(clienteActivoPredicate)
				.map(cliente -> mapper.map(cliente, ClienteDTO.class));
	}

	@Transactional(readOnly = true)
	public Optional<ClienteDTO> getClienteByIdentificacion(String identificacion) {
		return clienteRepository.findByIdentificacion(identificacion).filter(clienteActivoPredicate)
				.map(cliente -> mapper.map(cliente, ClienteDTO.class));
	}

	@Transactional
	public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
		Cliente clienteActualizado = clienteRepository.findById(id).map(cliente -> {
			mapper.map(clienteDTO, cliente);
			if (clienteDTO.getContrasena() == null) {
				cliente.setContrasena(cliente.getContrasena()); // conservar la contraseña
			}
			return clienteRepository.save(cliente);
		}).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

		return mapper.map(clienteActualizado, ClienteDTO.class);
	}

	@Transactional
	public void eliminarCliente(Long id) {
		clienteRepository.findById(id).ifPresent(cliente -> {
			cliente.setEstado(false);
			clienteRepository.save(cliente);
		});
	}

	@Transactional(readOnly = true)
	public List<ClienteDTO> buscarPorNombre(String nombre) {
		return clienteRepository.buscarPorNombre(nombre).stream().filter(clienteActivoPredicate)
				.map(cliente -> mapper.map(cliente, ClienteDTO.class)).toList();
	}

}
