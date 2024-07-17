package Api.ChallengeForo.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearCursoDto(
        @NotBlank String name,
        @NotNull Categoria categoria
) {
}
