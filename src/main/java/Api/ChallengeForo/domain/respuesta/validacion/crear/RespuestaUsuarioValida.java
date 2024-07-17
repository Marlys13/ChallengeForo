package Api.ChallengeForo.domain.respuesta.validacion.crear;


import Api.ChallengeForo.domain.respuesta.CrearRespuestaDto;
import Api.ChallengeForo.domain.usuario.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaUsuarioValida implements ValidarRespuestaCreada{
    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validate(CrearRespuestaDto data) {
        var usuarioExiste = repository.existsById(data.usuarioId());

        if(!usuarioExiste){
            throw new ValidationException("Este usuario no existe");
        }

        var usuarioHabilitado = repository.findById(data.usuarioId()).get().isEnabled();

        if(!usuarioHabilitado){
            throw new ValidationException("Este usuario no esta habilitado");
        }
    }
}
