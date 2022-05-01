package emented.lab7.server.util;

import com.thoughtworks.xstream.converters.ConversionException;
import emented.lab7.common.util.Request;
import emented.lab7.common.util.Response;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


@Aspect
public class AspectsForLogging {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectsForLogging.class);

    @Before("execution(void startServerWorker())")
    public void startServerAdvise(JoinPoint joinPoint) {
        LOGGER.info(joinPoint.getSignature().getName() + " method executed");
    }

    @AfterReturning(pointcut = "execution(public static String validateClass(..))", returning = "res")
    public void validationAdvise(Object res) {
        if (res == null) {
            LOGGER.info("Successfully validated data from file");
        } else {
            LOGGER.error((String) res);
        }
    }

    @AfterThrowing(pointcut = "execution(* readFromXML(..))", throwing = "ex")
    public void xmlExceptionAdvise(Throwable ex) {
        if (ex.getClass() == ConversionException.class) {
            LOGGER.error("Error during type conversion");
        }
    }

    @AfterReturning(pointcut = "execution(* listenForRequest())", returning = "res")
    public void listenForRequestAdvise(Object res) {
        if (res != null) {
            LOGGER.info("Accepted request: " + res);
        }
    }

    @After("execution(void sendResponse(..))")
    public void sendResponseAdvice(JoinPoint joinPoint) {
        LOGGER.info("Sent response: " + ((Response) joinPoint.getArgs()[0]).getInfoAboutResponse());
    }

    @AfterReturning(pointcut = "execution(String readCommand())", returning = "res")
    public void readCommandFromConsoleAdvise(Object res) {
        LOGGER.info("Read command from console: " + res.toString());
    }

    @After("execution(* emented.lab7.server.util.CommandManager.executeServerCommand(..))")
    public void executeServerCommandAdvise(JoinPoint joinPoint) {
        LOGGER.info("Executing server command: " + joinPoint.getArgs()[0]);
    }

    @After("execution(* emented.lab7.server.util.CommandManager.executeClientCommand(..))")
    public void executeClientCommandAdvise(JoinPoint joinPoint) {
        LOGGER.info("Executing client command: " + ((Request) joinPoint.getArgs()[0]).getCommandName());
    }

    @AfterThrowing(pointcut = "execution(* *(..))", throwing = "ex")
    public void anyExceptionAdvise(Throwable ex) {
        LOGGER.error(Arrays.toString(ex.getStackTrace()));
    }

}
