package emented.lab7.server.validator;

import emented.lab7.common.entities.Coordinates;
import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.entities.Studio;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.util.CollectionManager;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Класс, содержащий в себе методы валидации
 */
public final class FileValidator {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    static {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.logback").setLevel(Level.OFF);
    }

    private FileValidator() {
    }

    public static String validateClass(CollectionManager collectionInWork) {
        for (MusicBand m : collectionInWork.getMusicBands()) {
            Set<ConstraintViolation<Coordinates>> validatedCoordinates = VALIDATOR.validate(m.getCoordinates());
            Set<ConstraintViolation<Studio>> validatedStudio = new HashSet<>();
            if (m.getStudio() != null) {
                validatedStudio = VALIDATOR.validate(m.getStudio());
            }
            Set<ConstraintViolation<MusicBand>> validatedBand = VALIDATOR.validate(m);
            if (!validatedBand.isEmpty() || !validatedCoordinates.isEmpty() || !validatedStudio.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Errors were found in the source file"));
                validatedBand.stream().map(ConstraintViolation::getMessage)
                        .forEach(res -> builder.append(res).append("\n"));
                validatedCoordinates.stream().map(ConstraintViolation::getMessage)
                        .forEach(res -> builder.append(res).append("\n"));
                validatedStudio.stream().map(ConstraintViolation::getMessage)
                        .forEach(res -> builder.append(res).append("\n"));
                if (!builder.isEmpty()) {
                    builder.delete(builder.length() - 1, builder.length());
                }
                return builder.toString();
            }
        }
        return null;
    }
}
