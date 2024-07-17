package Api.ChallengeForo.domain.respuesta.validacion.actualizar;

import Api.ChallengeForo.domain.respuesta.ActualizarRespuestaDto;

public interface ValidarRespuestaActualizada {
    public void validate(ActualizarRespuestaDto data, Long respuestaId);
}