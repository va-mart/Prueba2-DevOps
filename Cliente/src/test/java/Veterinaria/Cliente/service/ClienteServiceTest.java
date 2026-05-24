package Veterinaria.Cliente.service;

import Veterinaria.Cliente.DTO.ClienteRequest;
import Veterinaria.Cliente.Exception.ClienteNoEncontradoException;
import Veterinaria.Cliente.Model.Cliente;
import Veterinaria.Cliente.Repository.ClienteRepository;
import Veterinaria.Cliente.Service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteRequest request;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Juan Pérez", "12345678-9",
                "+56912345678", "juan@test.com",
                "Av. Providencia 1234, Santiago", LocalDate.of(2024, 1, 10));

        request = new ClienteRequest("Juan Pérez", "12345678-9",
                "+56912345678", "juan@test.com",
                "Av. Providencia 1234, Santiago", LocalDate.of(2024, 1, 10));
    }

    @Test
    void listarTodos_debeRetornarListaClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        List<Cliente> result = clienteService.listarTodos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Juan Pérez");
        verify(clienteRepository).findAll();
    }

    @Test
    void listarTodos_debeRetornarListaVacia_cuandoNoHayClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        List<Cliente> result = clienteService.listarTodos();

        assertThat(result).isEmpty();
    }

    @Test
    void buscarPorId_debeRetornarCliente_cuandoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarPorId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void buscarPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(99L))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorRut_debeRetornarCliente_cuandoExiste() {
        when(clienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarPorRut("12345678-9");

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void buscarPorRut_debeLanzarExcepcion_cuandoNoExiste() {
        when(clienteRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorRut("99999999-9"))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("99999999-9");
    }

    @Test
    void guardar_debeGuardarCliente_cuandoDatosValidos() {
        when(clienteRepository.existsByRut(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.guardar(request);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void guardar_debeLanzarExcepcion_cuandoRutDuplicado() {
        when(clienteRepository.existsByRut("12345678-9")).thenReturn(true);

        assertThatThrownBy(() -> clienteService.guardar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RUT");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void guardar_debeLanzarExcepcion_cuandoEmailDuplicado() {
        when(clienteRepository.existsByRut(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail("juan@test.com")).thenReturn(true);

        assertThatThrownBy(() -> clienteService.guardar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void actualizar_debeActualizarCliente_cuandoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.actualizar(1L, request);

        assertThat(result).isNotNull();
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.actualizar(99L, request))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void eliminar_debeEliminarCliente_cuandoExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertThatCode(() -> clienteService.eliminar(1L)).doesNotThrowAnyException();
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcion_cuandoNoExiste() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.eliminar(99L))
                .isInstanceOf(ClienteNoEncontradoException.class)
                .hasMessageContaining("99");

        verify(clienteRepository, never()).deleteById(any());
    }
}
