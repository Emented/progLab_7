package emented.lab7.server.parser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.security.AnyTypePermission;
import emented.lab7.common.util.StreamUtil;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.util.CollectionManager;
import emented.lab7.server.validator.FileValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Класс, реализующий парсинг XML файла, а так же запись в файл
 */
public class XMLParser {

    /**
     * Создаем экземпляр класса, отвечающего за парсинг файла
     */
    private final XStream xStream = new XStream();
    /**
     * Создаем экземпляр класса, отвечающего за конвертацию потока в строку
     */
    private final StreamUtil converter = new StreamUtil();

    /**
     * Метод, инициализирующий парсер
     */
    private void initializeParser() {
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.processAnnotations(CollectionManager.class);
    }

    /**
     * Метод, считывающий коллекцию из XML файла
     *
     * @param fileName Имя исходного файла
     * @return Экземпляр класса, хранящего в себе коллекцию
     * @throws IOException Возможна ошибка доступа либо отсутствия файла по данному адресу
     */
    public CollectionManager readFromXML(String fileName) throws IOException, ConversionException {
        FileInputStream stream = new FileInputStream(fileName);
        initializeParser();
        String xmlText = converter.streamToString(stream);
        stream.close();
        CollectionManager collection;
        collection = (CollectionManager) xStream.fromXML(xmlText);
        collection.reassignIds();
        String res = FileValidator.validateClass(collection);
        if (res != null) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(res));
            System.exit(1);
        } else {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getGreenText("Successfully transfered data from file, the application is launched"));
        }
        collection.setFileName(fileName);
        collection.setDateOfInitialization(LocalDate.now());
        return collection;
    }

    /**
     * Метод, записывающий коллекцию в XML файл с указанным именем
     *
     * @param fileName   Имя исходного файла
     * @param musicBands Класс, содежащий коллекцию, которую необходимо записать
     * @throws IOException Возможна ошибка доступа либо отсутствия файла по данному адресу
     */
    public void writeToXML(String fileName, CollectionManager musicBands) throws IOException {
        initializeParser();
        xStream.processAnnotations(CollectionManager.class);
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xStream.toXML(musicBands.getMusicBands());
        PrintWriter p = new PrintWriter(fileName);
        p.write(text);
        p.close();
    }

    /**
     * Метод, записывающий коллекцию в XML файл с именем указанным в классе коллекции
     *
     * @param musicBands Класс, содежащий коллекцию, которую необходимо записать
     * @throws IOException Возможна ошибка доступа либо отсутствия файла по данному адресу
     */
    public void writeToXMLofExistingInstance(CollectionManager musicBands) throws IOException {
        initializeParser();
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xStream.toXML(musicBands.getMusicBands());
        PrintWriter p = new PrintWriter(musicBands.getFileName());
        p.write(text);
        p.close();
    }
}
