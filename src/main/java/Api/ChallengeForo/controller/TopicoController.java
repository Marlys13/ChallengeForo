package Api.ChallengeForo.controller;

import Api.ChallengeForo.domain.curso.Curso;
import Api.ChallengeForo.domain.curso.CursoRepository;
import Api.ChallengeForo.domain.respuesta.Respuesta;
import Api.ChallengeForo.domain.respuesta.RespuestaDto;
import Api.ChallengeForo.domain.respuesta.RespuestaRepository;
import Api.ChallengeForo.domain.topico.*;
import Api.ChallengeForo.domain.topico.validacion.actualizar.ValidarTopicoActualizado;
import Api.ChallengeForo.domain.topico.validacion.crear.TopicoDuplicado;
import Api.ChallengeForo.domain.topico.validacion.crear.ValidarTopicoCreado;
import Api.ChallengeForo.domain.usuario.Usuario;
import Api.ChallengeForo.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topic", description = "Está vinculado a un curso y usuario específicos.")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarTopicoCreado> crearValidadores;

    @Autowired
    List<ValidarTopicoActualizado> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo topico en la BD")
    public ResponseEntity<TopicoDto> crearTopico(@RequestBody @Valid CrearTopicoDto crearTopicoDTO, UriComponentsBuilder uriBuilder){
        crearValidadores.forEach(v -> v.validate(crearTopicoDTO));

        Usuario usuario = usuarioRepository.findById(crearTopicoDTO.usuarioId()).get();
        Curso curso = cursoRepository.findById(crearTopicoDTO.cursoId()).get();
        Topico topico = new Topico(crearTopicoDTO, usuario, curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Lee todos los temas independientemente de su estado")
    public ResponseEntity<Page<TopicoDto>> leerTodosTopicos(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC)Pageable pageable){
        var pagina = topicoRepository.findAll(pageable).map(TopicoDto::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de temas abiertos y cerrados")
    public ResponseEntity<Page<TopicoDto>> leerTopicosNoEliminados(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable){
        var pagina = topicoRepository.findAllByEstadoIsNot(Estado.DELETED, pageable).map(TopicoDto::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee un único tema por su ID")
    public ResponseEntity<TopicoDto> leerUnTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new TopicoDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @GetMapping("/{id}/solucion")
    @Operation(summary = "Lee la respuesta del topico marcada como su solución")
    public ResponseEntity<RespuestaDto> leerSolucionTopico(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceByTopicoId(id);

        var datosRespuesta = new RespuestaDto(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el título, el mensaje, el estado o el ID del curso de un tema")
    public ResponseEntity<TopicoDto> actualizarTopico(@RequestBody @Valid ActualizarTopicoDto actualizarTopicoDTO, @PathVariable Long id){
        actualizarValidadores.forEach(v -> v.validate(actualizarTopicoDTO));

        Topico topico = topicoRepository.getReferenceById(id);

        if(actualizarTopicoDTO.cursoId() != null){
            Curso curso = cursoRepository.getReferenceById(actualizarTopicoDTO.cursoId());
            topico.actualizarTopicoConCurso(actualizarTopicoDTO, curso);
        }else{
            topico.actualizarTopico(actualizarTopicoDTO);
        }

        var datosTopico = new TopicoDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un topic")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.eliminarTopico();
        return ResponseEntity.noContent().build();
    }

}