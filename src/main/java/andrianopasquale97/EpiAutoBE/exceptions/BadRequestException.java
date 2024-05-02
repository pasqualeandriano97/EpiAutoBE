package andrianopasquale97.EpiAutoBE.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException {
    private List<ObjectError> errorsList;
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(List<ObjectError> errorsList){
        super("Ci sono stati errori di validazione nel payload!");
        this.errorsList = errorsList;
    }
}
