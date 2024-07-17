package Api.ChallengeForo.domain.topico.validacion.crear;

import Api.ChallengeForo.domain.topico.CrearTopicoDto;
import Api.ChallengeForo.domain.topico.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado {
    @Autowired
    private TopicoRepository topicoRepository;


    @Override
    public void validate(CrearTopicoDto data) {
        var topicoDuplicado = topicoRepository.existsByTituloAndMensaje(data.titulo(), data.mensaje());
        if(topicoDuplicado){
            throw new ValidationException("Este topico ya existe. Revisa /topicos/" + topicoRepository.findByTitulo(data.titulo()).getId());

        }
    }
}
