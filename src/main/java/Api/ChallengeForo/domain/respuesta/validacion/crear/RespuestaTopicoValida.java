package Api.ChallengeForo.domain.respuesta.validacion.crear;


import Api.ChallengeForo.domain.respuesta.CrearRespuestaDto;
import Api.ChallengeForo.domain.topico.Estado;
import Api.ChallengeForo.domain.topico.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaTopicoValida implements ValidarRespuestaCreada{
    @Autowired
    private TopicoRepository repository;

    @Override
    public void validate(CrearRespuestaDto data) {
        var topicoExiste = repository.existsById(data.topicoId());

        if (!topicoExiste){
            throw new ValidationException("Este topico no existe.");
        }

        var topicoAbierto = repository.findById(data.topicoId()).get().getEstado();

        if(topicoAbierto != Estado.OPEN){
            throw new ValidationException("Este topico no esta abierto.");
        }

    }
}
