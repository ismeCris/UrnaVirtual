package app.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import app.controller.EleitorController;
import app.entity.Eleitor;
import app.service.EleitorService;

public class EleitorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EleitorService eleitorService;

    @InjectMocks
    private EleitorController eleitorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eleitorController).build();
    }

    @Test
    void SaveEleitor() throws Exception {
        String eleitorJson = "{\"cpf\": \"123.456.789-00\", \"email\": \"joao@teste.com\", \"nomeCompleto\": \"João da Silva\"}";

        when(eleitorService.save(any(Eleitor.class))).thenReturn("Eleitor cadastrado");

        mockMvc.perform(post("/api/eleitor/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eleitorJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Eleitor cadastrado"));

        verify(eleitorService, times(1)).save(any(Eleitor.class));
    }
    
    @Test
    void UpdateEleitor() throws Exception {
        String eleitorJson = "{\"nomeCompleto\": \"João da Silva Atualizado\", \"cpf\": \"123.456.789-00\", \"email\": \"joao@teste.com\"}";

        when(eleitorService.update(any(Eleitor.class), any(Long.class))).thenReturn("Eleitor atualizado");

        mockMvc.perform(put("/api/eleitor/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eleitorJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Eleitor atualizado"));

        verify(eleitorService, times(1)).update(any(Eleitor.class), any(Long.class));
    }
    @Test
    void FindById() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setNomeCompleto("João da Silva");
        eleitor.setCpf("123.456.789-00");
        eleitor.setEmail("joao@teste.com");
        eleitor.setStatus(Eleitor.Status.APTO);

        when(eleitorService.findById(1L)).thenReturn(eleitor);

        mockMvc.perform(get("/api/eleitor/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.email").value("joao@teste.com"));

        verify(eleitorService, times(1)).findById(1L);
    }
 
    @Test
    void testFindByIdValid() throws Exception {
        Eleitor eleitor = new Eleitor();
        eleitor.setNomeCompleto("João da Silva");
        eleitor.setCpf("123.456.789-00");
        eleitor.setEmail("joao@teste.com");
        eleitor.setStatus(Eleitor.Status.APTO);

        when(eleitorService.findById(1L)).thenReturn(eleitor);

        mockMvc.perform(get("/api/eleitor/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.email").value("joao@teste.com"));

        verify(eleitorService, times(1)).findById(1L);
    }


    @Test
    void FindAll() throws Exception {
        Eleitor eleitor1 = new Eleitor();
        eleitor1.setNomeCompleto("João da Silva");
        eleitor1.setCpf("123.456.789-00");
        eleitor1.setEmail("joao@teste.com");
        eleitor1.setStatus(Eleitor.Status.APTO);

        Eleitor eleitor2 = new Eleitor();
        eleitor2.setNomeCompleto("Maria Oliveira");
        eleitor2.setCpf("987.654.321-00");
        eleitor2.setEmail("maria@teste.com");
        eleitor2.setStatus(Eleitor.Status.INATIVO);

        List<Eleitor> eleitores = Arrays.asList(eleitor1, eleitor2);

        when(eleitorService.findAll()).thenReturn(eleitores);

        mockMvc.perform(get("/api/eleitor/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCompleto").value("João da Silva"))
                .andExpect(jsonPath("$[1].nomeCompleto").value("Maria Oliveira"));

        verify(eleitorService, times(1)).findAll();
    }

    @Test
    void testFindAllEmptyList() throws Exception {
        when(eleitorService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/eleitor/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(eleitorService, times(1)).findAll();
    }
 
    @Test
    void DeleteEleitor() throws Exception {
        when(eleitorService.delete(1L)).thenReturn("Eleitor deletado");

        mockMvc.perform(put("/api/eleitor/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Eleitor deletado"));

        verify(eleitorService, times(1)).delete(1L);
    }
    @Test
    void DeleteEleitorNotFound() throws Exception {
        when(eleitorService.delete(1L)).thenThrow(new RuntimeException("Eleitor não encontrado"));

        mockMvc.perform(put("/api/eleitor/delete/1"))
                .andExpect(status().isNotFound())  
                .andExpect(content().string("Eleitor não encontrado")); 

        verify(eleitorService, times(1)).delete(1L);
    }


}
