package Api.ChallengeForo.domain.curso;

public record CursoDto(Long id, String name, Categoria categoria, Boolean activo) {

    public CursoDto(Curso curso){
        this(curso.getId(), curso.getName(), curso.getCategoria(), curso.getActivo());
    }
}
