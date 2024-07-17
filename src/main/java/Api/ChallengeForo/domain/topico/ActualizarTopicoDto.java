package Api.ChallengeForo.domain.topico;

public record ActualizarTopicoDto(
        String titulo,
        String mensaje,
        Estado estado,
        Long cursoId
) {
}
