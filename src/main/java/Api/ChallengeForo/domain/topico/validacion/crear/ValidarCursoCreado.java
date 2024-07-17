package Api.ChallengeForo.domain.topico.validacion.crear;

import Api.ChallengeForo.domain.curso.CursoRepository;
import Api.ChallengeForo.domain.topico.CrearTopicoDto;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoCreado implements ValidarTopicoCreado{
    @Autowired
    private CursoRepository repository;

    @Override
    public void validate(CrearTopicoDto data) {
        var ExisteCurso = repository.existsById(data.cursoId());
        if(!ExisteCurso){
            throw new ValidationException("Este curso no existe.");
        }

        var cursoHabilitado = repository.findById(data.cursoId()).get().getActivo();
        if(!cursoHabilitado){
            throw new ValidationException("Este curso no esta disponible en este momento.");
        }
    }
}
