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
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс, хранящий в себе коллекцию музыкальных групп, а так же реализующий методы работы с ней
 */
public class CollectionManager {

    private final ReentrantLock reentrantLock = new ReentrantLock();
    /**
     * Поле, хранящее дату инициализации
     */
    private final LocalDate dateOfInitialization;

    /**
     * Поле, хранящее коллекцию элементов
     */
    private HashSet<MusicBand> musicBands;

    public CollectionManager() {
        dateOfInitialization = LocalDate.now();
    }

    /**
     * Метод, добавлющий в коллекцию новый элемент
     *
     * @param band Новый элемент для добавления
     */
    public void addMusicBand(MusicBand band) {
        try {
            reentrantLock.lock();
            musicBands.add(band);
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод, возвращающий коллекцию
     *
     * @return Коллекция групп
     */
    public HashSet<MusicBand> getMusicBands() {
        try {
            reentrantLock.lock();
            return musicBands;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void setMusicBands(HashSet<MusicBand> musicBands) {
        try {
            reentrantLock.lock();
            this.musicBands = musicBands;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод, удаляющий группу по ID
     *
     * @param id ID группы для удаления
     */
    public void removeBandById(Long id) {
        try {
            reentrantLock.lock();
            musicBands.removeIf(mb -> Objects.equals(mb.getId(), id));
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод обновляющий элемент коллекции по ID
     *
     * @param id        ID существующего элемента
     * @param musicBand Новый элемент коллекции
     */
    public void updateById(Long id, MusicBand musicBand) {
        try {
            reentrantLock.lock();
            musicBands.removeIf(mb -> Objects.equals(mb.getId(), id));
            musicBand.setId(id);
            musicBands.add(musicBand);
        } finally {
            reentrantLock.unlock();
        }
    }

    public Set<MusicBand> getUsersElements(List<Long> ids) {
        try {
            reentrantLock.lock();
            Set<MusicBand> result = new HashSet<>();
            for (MusicBand band : musicBands) {
                if (ids.contains(band.getId())) {
                    result.add(band);
                }
            }
            return result.isEmpty() ? null : result;
        } finally {
            reentrantLock.unlock();
        }
    }

    public Set<MusicBand> getAlienElements(List<Long> ids) {
        try {
            reentrantLock.lock();
            Set<MusicBand> result = new HashSet<>();
            for (MusicBand band : musicBands) {
                if (!ids.contains(band.getId())) {
                    result.add(band);
                }
            }
            return result.isEmpty() ? null : result;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean checkMax(MusicBand musicBand) {
        try {
            reentrantLock.lock();
            boolean check = true;
            for (MusicBand band : musicBands) {
                if (band.compareTo(musicBand) >= 0) {
                    check = false;
                    break;
                }
            }
            return check;
        } finally {
            reentrantLock.unlock();
        }
    }

    public List<Long> returnIDsOfGreater(MusicBand musicBand) {
        try {
            reentrantLock.lock();
            List<Long> ids = new ArrayList<>();
            for (MusicBand mb : musicBands) {
                if (mb.compareTo(musicBand) > 0) {
                    ids.add(mb.getId());
                }
            }
            return ids;
        } finally {
            reentrantLock.unlock();
        }
    }

    public List<Long> returnIDbyNumberOFParticipants(Long numberOfParticipants) {
        try {
            reentrantLock.lock();
            return musicBands.stream()
                    .filter(mb -> Objects.equals(mb.getNumberOfParticipants(), numberOfParticipants))
                    .map(MusicBand::getId)
                    .collect(Collectors.toList());
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод для возврата элемента коллекции с минимальным значением студии
     *
     * @return Элемент коллекции с минимальным значением студии
     */
    public MusicBand returnMinByStudio() throws CollectionIsEmptyException {
        try {
            reentrantLock.lock();
            if (!musicBands.isEmpty()) {
                List<MusicBand> sortedBands = new ArrayList<>(musicBands).stream().sorted().collect(Collectors.toList());
                return sortedBands.get(0);
            } else {
                throw new CollectionIsEmptyException("Collection is empty");
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод для подсчета элементов коллекции, количество участников которых меньше заданного
     *
     * @param numberOfParticipants Число участников для сравнения
     * @return Число элементов с меньшим числом участников
     */
    public int countLessThanNumberOfParticipants(Long numberOfParticipants) {
        try {
            reentrantLock.lock();
            int counter = 0;
            List<MusicBand> sortedBands = new ArrayList<>(musicBands);
            sortedBands.sort(Comparator.comparingLong(MusicBand::getNumberOfParticipants));
            for (MusicBand band : sortedBands) {
                if (band.getNumberOfParticipants() < numberOfParticipants) {
                    counter++;
                }
            }
            return counter;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Метод, возвращющий информацию о коллекции
     *
     * @return Строку, содержащую информацию о коллекции
     */
    public String returnInfo() {
        try {
            reentrantLock.lock();
            final int size = 6;
            return "Collection type: " + musicBands.getClass().toString().substring(size) + ", type of elements: "
                    + MusicBand.class.toString().substring(size) + ", date of initialization: " + dateOfInitialization
                    + ", number of elements: " + musicBands.size();
        } finally {
            reentrantLock.unlock();
        }
    }
}

