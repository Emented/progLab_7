package emented.lab7.server.util;

import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.exceptions.CollectionIsEmptyException;

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
public class CollectionManager {
    /**
     * Поле, хранящее дату инициализации
     */
    private LocalDate dateOfInitialization;

    /**
     * Поле, хранящее коллекцию элементов
     */
    private HashSet<MusicBand> musicBands;

    public CollectionManager() {
        dateOfInitialization = LocalDate.now();
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
     * Метод, добавлющий в коллекцию новый элемент
     *
     * @param band Новый элемент для добавления
     */
    public void addMusicBand(MusicBand band) {
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

    public void setMusicBands(HashSet<MusicBand> musicBands) {
        this.musicBands = musicBands;
    }

    /**
     * Метод, удаляющий группу по ID
     *
     * @param id ID группы для удаления
     */
    public void removeBandById(Long id) {
        musicBands.removeIf(mb -> Objects.equals(mb.getId(), id));
    }

    /**
     * Метод обновляющий элемент коллекции по ID
     *
     * @param id        ID существующего элемента
     * @param musicBand Новый элемент коллекции
     */
    public void updateById(Long id, MusicBand musicBand) {
        musicBands.removeIf(mb -> Objects.equals(mb.getId(), id));
        musicBand.setId(id);
        musicBands.add(musicBand);
    }

    public Set<MusicBand> getUsersElements(List<Long> ids) {
        Set<MusicBand> result = new HashSet<>();
        for (MusicBand band : musicBands) {
            if (ids.contains(band.getId())) {
                result.add(band);
            }
        }
        return result.isEmpty() ? null : result;
    }

    public Set<MusicBand> getAlienElements(List<Long> ids) {
        Set<MusicBand> result = new HashSet<>();
        for (MusicBand band : musicBands) {
           if (!ids.contains(band.getId())) {
               result.add(band);
           }
        }
        return result.isEmpty() ? null : result;
    }

    public boolean checkMax(MusicBand musicBand) {
        boolean check = true;
        for (MusicBand band : musicBands) {
            if (band.compareTo(musicBand) >= 0) {
                check = false;
                break;
            }
        }
        return check;
    }

    public List<Long> returnIDsOfGreater(MusicBand musicBand) {
        List<Long> ids = new ArrayList<>();
        for (MusicBand mb : musicBands) {
            if (mb.compareTo(musicBand) > 0) {
                ids.add(mb.getId());
            }
        }
        return ids;
    }

    public List<Long> returnIDbyNumberOFParticipants(Long numberOfParticipants) {
        return musicBands.stream()
                .filter(mb -> Objects.equals(mb.getNumberOfParticipants(), numberOfParticipants))
                .map(MusicBand::getId)
                .collect(Collectors.toList());
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
     * Метод, возвращющий информацию о коллекции
     *
     * @return Строку, содержащую информацию о коллекции
     */
    public String returnInfo() {
        final int size = 6;
        return "Collection type: " + musicBands.getClass().toString().substring(size) + ", type of elements: "
                + MusicBand.class.toString().substring(size) + ", date of initialization: " + dateOfInitialization
                + ", number of elements: " + musicBands.size();
    }
}

