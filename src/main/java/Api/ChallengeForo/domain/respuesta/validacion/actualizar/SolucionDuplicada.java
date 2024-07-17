package Api.ChallengeForo.domain.respuesta.validacion.actualizar;


import Api.ChallengeForo.domain.respuesta.ActualizarRespuestaDto;
import Api.ChallengeForo.domain.respuesta.Respuesta;
import Api.ChallengeForo.domain.respuesta.RespuestaRepository;
import Api.ChallengeForo.domain.topico.Estado;
import Api.ChallengeForo.domain.topico.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolucionDuplicada implements ValidarRespuestaActualizada{
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validate(ActualizarRespuestaDto data, Long respuestaId) {
        if (data.solucion()){
            Respuesta respuesta = respuestaRepository.getReferenceById(respuestaId);
            var topicoResuelto = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            if (topicoResuelto.getEstado() == Estado.CLOSED){
                throw new ValidationException("Este topico ya esta solucionado.");
            }
        }
    }
}
