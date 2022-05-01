package emented.lab7.client.workwithcommandline;

import emented.lab7.client.util.CommandValidators;
import emented.lab7.common.entities.Coordinates;
import emented.lab7.common.entities.MusicBand;
import emented.lab7.common.entities.Studio;
import emented.lab7.common.entities.enums.MusicGenre;

import java.util.Locale;
import java.util.Scanner;

/**
 * Класс, предназанченный для генерации новой музыкальной группы
 */
public class MusicBandGenerator {

    private final int maxNameLength = 100;
    private final int maxDescriptionLength = 300;
    private final int maxAddressLength = 100;


    /**
     * Новая музыкальная группа
     */
    private final MusicBand generatedMusicBand;


    /**
     * Сканер для считывания в интерактином режиме
     */
    private final Scanner sc = new Scanner(System.in);

    /**
     * Конструктор класса
     */
    public MusicBandGenerator() {
        generatedMusicBand = new MusicBand();
    }

    /**
     * Конструктор класса с заданным ID
     *
     * @param id ID новой музыкальной группы
     */
    public MusicBandGenerator(Long id) {
        generatedMusicBand = new MusicBand(id);
    }

    /**
     * Метод, устанавливающий имя для новой музыкальной группы
     */
    private void getName() {
        String name = CommandValidators.validateStringInput("Enter the name of the music group (max length + "
                        + maxNameLength
                        + " symbols)",
                false,
                sc,
                maxNameLength);
        generatedMusicBand.setName(name);
    }

    /**
     * Метод, считывающий число учатников с коммандной строки и устанавливающий его для новой муыкальной группы
     */
    private void getNumberOfParticipants() {
        long numberOfParticipants = CommandValidators.validateInput(arg -> ((long) arg) > 0,
                "Enter the number of participants",
                "Error processing the number, repeat the input",
                "The number of participants must be greater than 0, repeat the input",
                Long::parseLong,
                false,
                sc);
        generatedMusicBand.setNumberOfParticipants(numberOfParticipants);
    }

    private void getCoordinates() {
        double x = CommandValidators.validateInput(arg -> ((double) arg) <= Coordinates.MAX_X,
                "Enter the X coordinate of the group (its value should be no more than " + Coordinates.MAX_X + ")",
                "Error processing the number, repeat the input",
                "The X coordinate should be no more than " + Coordinates.MAX_X + ", repeat the input",
                Double::parseDouble,
                false,
                sc);
        Float y = CommandValidators.validateInput(arg -> ((Float) arg) <= Coordinates.MAX_Y,
                "Enter the Y coordinate of the group (its value should be no more than " + Coordinates.MAX_Y + ")",
                "Error processing the number, repeat the input",
                "The Y coordinate should be no more than " + Coordinates.MAX_Y + ", repeat the input",
                Float::parseFloat,
                false,
                sc);
        generatedMusicBand.setCoordinates(new Coordinates(x, y));
    }

    /**
     * Метод, считывающий описание с коммандной строки и устанавливающий его для новой муыкальной группы
     */
    private void getDescription() {
        String description = CommandValidators.validateStringInput("Enter a description of the group (press ENTER to skip, max length "
                        + maxDescriptionLength
                        + " symbols)",
                true,
                sc,
                maxDescriptionLength);
        generatedMusicBand.setDescription(description);
    }

    /**
     * Метод, считывающий жанр с коммандной строки и устанавливающий его для новой муыкальной группы
     */
    private void getMusicGenre() {
        MusicGenre genre = CommandValidators.validateInput(arg -> true,
                "Enter the genre of music from the suggested ones below (press ENTER to skip)\n" + MusicGenre.show(),
                "There is no such musical genre, repeat the input",
                "Input error",
                string -> MusicGenre.valueOf(string.toUpperCase(Locale.ROOT)),
                true,
                sc);
        generatedMusicBand.setGenre(genre);
    }

    /**
     * Метод, считывающий студию с коммандной строки и устанавливающий ее для новой муыкальной группы
     */
    private void getStudio() {
        String address = CommandValidators.validateStringInput("Enter studio address of the group (press ENTER to skip, max length "
                        + maxAddressLength
                        + " symbols)",
                true,
                sc,
                maxDescriptionLength);
        if (address != null) {
            generatedMusicBand.setStudio(new Studio(address));
        } else {
            generatedMusicBand.setStudio(null);
        }
    }

    /**
     * Метод, устанавливающий переменные, считанные в интерактивном режиме
     */
    public void setVariables() {
        getName();
        getCoordinates();
        getNumberOfParticipants();
        getDescription();
        getMusicGenre();
        getStudio();
    }

    /**
     * Метод, возвращающий новую музыкальную группу
     *
     * @return Новая музыкальная группа
     */
    public MusicBand getGeneratedMusicBand() {
        return generatedMusicBand;
    }
}
