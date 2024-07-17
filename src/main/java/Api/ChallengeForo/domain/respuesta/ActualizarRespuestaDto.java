package Api.ChallengeForo.domain.respuesta;

public record ActualizarRespuestaDto(
        String mensaje, Boolean solucion, Boolean borrado
) {
}
