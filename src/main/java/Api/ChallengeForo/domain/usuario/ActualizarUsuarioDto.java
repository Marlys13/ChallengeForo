package Api.ChallengeForo.domain.usuario;

public record ActualizarUsuarioDto(
        String password,
        Role role,
        String nombre,
        String apellido,
        String email,
        Boolean enabled
) {
}
