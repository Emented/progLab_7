package emented.lab7.common.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import emented.lab7.common.entities.enums.MusicGenre;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@XStreamAlias("musicband")
public class MusicBand implements Serializable, Comparable<MusicBand> {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 300;

    @NotNull
    @PastOrPresent(message = "The collection cannot have a creation date in the future time")
    private final LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @NotNull
    @Positive(message = "The id must be greater then 0")
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Size(min = 1, max = MAX_NAME_LENGTH, message = "Name is too long")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NotNull
    private Coordinates coordinates; //Поле не может быть null
    @Positive(message = "The number of participants must be greater than 0")
    private long numberOfParticipants; //Значение поля должно быть больше 0
    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Description is too long")
    private String description; //Поле может быть null
    private MusicGenre genre; //Поле может быть null
    private Studio studio; //Поле может быть null

    /**
     * Конструктор, автоматически выставляющий ID и дату инициализации
     */
    public MusicBand() {
        creationDate = LocalDate.now();
    }

    public MusicBand(Long id) {
        this.id = id;
        creationDate = LocalDate.now();
    }

    /**
     * Метод, возвращающий ID
     *
     * @return ID объекта
     */
    public Long getId() {
        return id;
    }

    /**
     * Метод, устанавливающий ID по данному
     *
     * @param id Новый ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Метод, устанавливающий имя объекта
     *
     * @param name Новое имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод, возвращающий соординаты объекта
     *
     * @return Координаты объекта
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Метод, устанавливающий координаты объекта
     *
     * @param coordinates Новые координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Метод, возвращающий число участников
     *
     * @return Число участников
     */
    public long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * Метод, устанавливающий число участников
     *
     * @param numberOfParticipants Новое число участников
     */
    public void setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * Метод, устанавливающий описание
     *
     * @param description Новое описание
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Метод, устанавливающий жанр
     *
     * @param genre Новый жанр
     */
    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    /**
     * Метод, возвращающий студию
     *
     * @return Студия
     */
    public Studio getStudio() {
        return studio;
    }

    /**
     * Метод, устанавливающий студию
     *
     * @param studio Новая студия
     */
    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    /**
     * Метод сравнения
     *
     * @param anotherBand Группа для сравнения
     * @return Целочисленное значение
     */
    @Override
    public int compareTo(MusicBand anotherBand) {
        if (this.getStudio() == null) {
            return -1;
        } else if (anotherBand.getStudio() == null) {
            return 1;
        }
        return this.getStudio().compareTo(anotherBand.getStudio());
    }

    /**
     * Переопределение метода, возвращающего строковое представление класса
     *
     * @return Строковое представление класса
     */
    @Override
    public String toString() {
        return "ID: " + id
                + ", name: " + name
                + ", coordinates: " + coordinates
                + ", creation date: " + creationDate
                + ", number of participants: " + numberOfParticipants
                + ", description: " + ((description == null) ? "missing" : description)
                + ", genre: " + ((genre == null) ? "not defined" : genre)
                + ", " + ((studio == null) ? "the studio is missing" : studio);
    }
}
