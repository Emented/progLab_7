package emented.lab7.server.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.exceptions.CollectionIsEmptyException;
import emented.lab7.common.exceptions.GroupNotFoundException;
import emented.lab7.common.exceptions.GroupNotMaxException;
import emented.lab7.common.exceptions.IDNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс, хранящий в себе коллекцию музыкальных групп, а так же реализующий методы работы с ней
 */
@XStreamAlias("set")
public class CollectionManager {

    private static long idCounter = 1;

    /**
     * Поле, хранящее имя файла
     */
    private static String fileName;

    /**
     * Поле, хранящее дату инициализации
     */
    private LocalDate dateOfInitialization;

    /**
     * Поле, хранящее коллекцию элементов
     */
    @XStreamImplicit
    private HashSet<MusicBand> musicBands;

    public CollectionManager() {
        dateOfInitialization = LocalDate.now();
    }

    /**
     * Конструктор класса
     *
     * @param fileName Имя файла
     */
    public CollectionManager(String fileName) {
        dateOfInitialization = LocalDate.now();
        this.fileName = fileName;
    }

    public void reassignIds() {
        for (MusicBand m : musicBands) {
            m.setId(idCounter++);
        }
    }

    /**
     * Метод, возвращающий дату инициалзации
     *
     * @return Дата инициализации
     */
    public LocalDate getDateOfInitialization() {
        return dateOfInitialization;
    }

    /**
     * Метод, устанавливающий дату инициализации
     *
     * @param dateOfInitialization Дата инициализации
     */
    public void setDateOfInitialization(LocalDate dateOfInitialization) {
        this.dateOfInitialization = dateOfInitialization;
    }

    /**
     * Метод, возвращающий имя файла коллекции
     *
     * @return Имя файла
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Метод, устанавливающий имя файла
     *
     * @param fileName Новое имя файла
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод, добавлющий в коллекцию новый элемент
     *
     * @param band Новый элемент для добавления
     */
    public void addMusicBand(MusicBand band) {
        band.setId(idCounter++);
        musicBands.add(band);
    }

    /**
     * Метод, возвращающий коллекцию
     *
     * @return Коллекция групп
     */
    public HashSet<MusicBand> getMusicBands() {
        return musicBands;
    }

    /**
     * Метод, удаляющий группу по ID
     *
     * @param id ID группы для удаления
     */
    public void removeBandById(Long id) throws IDNotFoundException {
        if (!musicBands.removeIf(mb -> Objects.equals(mb.getId(), id))) {
            throw new IDNotFoundException("There is no group with this ID");
        }
    }

    /**
     * Метод обновляющий элемент коллекции по ID
     *
     * @param id        ID существующего элемента
     * @param musicBand Новый элемент коллекции
     */
    public void updateById(Long id, MusicBand musicBand) throws IDNotFoundException {
        if (musicBands.removeIf(mb -> Objects.equals(mb.getId(), id))) {
            musicBand.setId(id);
            musicBands.add(musicBand);
        } else {
            throw new IDNotFoundException("There is no group with this ID");
        }
    }

    public void checkId(Long id) throws IDNotFoundException {
        boolean flag = false;
        for (MusicBand band : musicBands) {
            if (Objects.equals(band.getId(), id)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new IDNotFoundException("There is no group with this ID");
        }
    }

    /**
     * Метод, добавляющий новый элемент в коллекцию, если он больше всех существующих
     *
     * @param musicBand Новый элемент
     */
    public void addIfMax(MusicBand musicBand) throws GroupNotMaxException {
        for (MusicBand band : musicBands) {
            if (band.compareTo(musicBand) >= 0) {
                throw new GroupNotMaxException("The entered music group is not maximal");
            }
        }
        addMusicBand(musicBand);
    }

    /**
     * Метод, удаляющий все элементы коллекции, большие данной
     *
     * @param musicBand Элемент для сравнения
     */
    public Set<MusicBand> removeIfGreater(MusicBand musicBand) throws CollectionIsEmptyException {
        Set<MusicBand> copy = new HashSet<>(musicBands);
        if (!musicBands.isEmpty()) {
            musicBands.removeIf(mb -> mb.compareTo(musicBand) > 0);
            copy.removeAll(musicBands);
        } else {
            throw new CollectionIsEmptyException("Collection is empty");
        }
        return copy;
    }

    /**
     * Метод, удаляющий из коллекции элемент с эквивалентным заданному числом участников
     *
     * @param numberOfParticipants Число участников для сравнения
     */
    public MusicBand removeAnyByNumberOfParticipants(Long numberOfParticipants) throws GroupNotFoundException, CollectionIsEmptyException {
        if (!musicBands.isEmpty()) {
            List<MusicBand> matchBands = musicBands.stream().filter(mb -> Objects.equals(mb.getNumberOfParticipants(), numberOfParticipants)).collect(Collectors.toList());
            if (matchBands.isEmpty()) {
                throw new GroupNotFoundException("There is no group with this number of participants");
            } else {
                musicBands.remove(matchBands.get(0));
                return matchBands.get(0);
            }
        } else {
            throw new CollectionIsEmptyException("Collection is empty");
        }
    }

    /**
     * Метод для возврата элемента коллекции с минимальным значением студии
     *
     * @return Элемент коллекции с минимальным значением студии
     */
    public MusicBand returnMinByStudio() throws CollectionIsEmptyException {
        if (!musicBands.isEmpty()) {
            List<MusicBand> sortedBands = new ArrayList<>(musicBands).stream().sorted().collect(Collectors.toList());
            return sortedBands.get(0);
        } else {
            throw new CollectionIsEmptyException("Collection is empty");
        }
    }

    /**
     * Метод для подсчета элементов коллекции, количество участников которых меньше заданного
     *
     * @param numberOfParticipants Число участников для сравнения
     * @return Число элементов с меньшим числом участников
     */
    public int countLessThanNumberOfParticipants(Long numberOfParticipants) {
        int counter = 0;
        List<MusicBand> sortedBands = new ArrayList<>(musicBands);
        sortedBands.sort(Comparator.comparingLong(MusicBand::getNumberOfParticipants));
        for (MusicBand band : sortedBands) {
            if (band.getNumberOfParticipants() < numberOfParticipants) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Метод очистки коллекции
     */
    public void clearCollection() {
        musicBands.clear();
    }

    /**
     * Метод, возвращющий информацию о коллекции
     *
     * @return Строку, содержащую информацию о коллекции
     */
    public String returnInfo() {
        final int size = 6;
        return "Collection type: " + musicBands.getClass().toString().substring(size) + ", type of elements: "
                + MusicBand.class.toString().substring(size) + ", date of initialization: " + dateOfInitialization
                + ", number of elements: " + musicBands.size() + ", file of collection: " + fileName;
    }

    /**
     * Вывести все элементы коллекции в их строковом представлении
     */
    public String show() {
        if (musicBands.isEmpty()) {
            return "Collection is empty";
        } else {
            StringBuilder sb = new StringBuilder();
            for (MusicBand band : musicBands) {
                sb.append(band).append("\n");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
            return sb.toString();
        }
    }
}

