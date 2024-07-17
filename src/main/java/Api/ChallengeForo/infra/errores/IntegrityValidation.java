package Api.ChallengeForo.infra.errores;

public class IntegrityValidation extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public IntegrityValidation(String s){
        super(s);
    }
}
