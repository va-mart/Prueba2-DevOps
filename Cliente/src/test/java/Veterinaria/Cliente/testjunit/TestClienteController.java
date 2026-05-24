package Veterinaria.Cliente.testjunit;

import Veterinaria.Cliente.Controller.ClienteController;
import Veterinaria.Cliente.DTO.ClienteRequest;
import Veterinaria.Cliente.Exception.ClienteNoEncontradoException;
import Veterinaria.Cliente.Model.Cliente;
import Veterinaria.Cliente.Service.ClienteService;
import Veterinaria.Cliente.security.JwtAuthenticationFilter;
import Veterinaria.Cliente.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class TestClienteController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

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
    void testBuscarPorId_debeRetornarCliente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testBuscarPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(clienteService.buscarPorId(99L))
                .thenThrow(new ClienteNoEncontradoException("Cliente no encontrado con id: 99"));

        mockMvc.perform(get("/api/v1/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListarTodos_debeRetornar200_conClientes() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    void testGuardar_debeRetornar201_cuandoDatosValidos() throws Exception {
        when(clienteService.guardar(any(ClienteRequest.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testActualizar_debeRetornar200_cuandoExiste() throws Exception {
        when(clienteService.actualizar(eq(1L), any(ClienteRequest.class))).thenReturn(cliente);

        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void testEliminar_debeRetornar200_cuandoExiste() throws Exception {
        doNothing().when(clienteService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente eliminado correctamente"));
    }

    @Test
    void testEliminar_debeRetornar404_cuandoNoExiste() throws Exception {
        doThrow(new ClienteNoEncontradoException("Cliente no encontrado con id: 99"))
                .when(clienteService).eliminar(99L);

        mockMvc.perform(delete("/api/v1/clientes/99"))
                .andExpect(status().isNotFound());
    }
}
