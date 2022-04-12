package emented.lab7.common.exceptions;

/**
 * Unchecked исключение, отвечающее за возможность зацикливания при выполенении скрипта
 */
public class LoopPossibilityException extends RuntimeException {

    /**
     * Конструктор исключения
     *
     * @param message Сообщение, описывающее исключение
     */
    public LoopPossibilityException(String message) {
        super(message);
    }
}
