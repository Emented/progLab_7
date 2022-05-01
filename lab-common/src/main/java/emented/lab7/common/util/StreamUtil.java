package emented.lab7.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс, отвечающий за перевод потока ввода с строку/структуру строк
 */
public class StreamUtil {

    /**
     * Метод, конвертирующий поток из файла в строку
     *
     * @param file Поток ввода из файла
     * @return Строку, которая составлена из информации в файле
     * @throws IOException Возможна ошибка доступа либо отсутствия файла по данному адресу
     */
    public String streamToString(FileInputStream file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
        StringBuilder fString = new StringBuilder();
        bufferedReader.readLine();
        String line = bufferedReader.readLine();
        while (line != null) {
            fString.append(line.trim());
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return fString.toString();
    }
}
