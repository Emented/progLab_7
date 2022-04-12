package emented.lab7.common.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Класс, хранящий информацию о студии
 */
public class Studio implements Serializable, Comparable<Studio> {

    private static final int MAX_ADDRESS_LENGTH = 100;

    /**
     * Поле, хранящее адрес студии (не может быть null)
     */
    @NotBlank(message = "The address must contain at least 1 character")
    @Size(min = 1, max = MAX_ADDRESS_LENGTH, message = "Address is too long")
    private String address; //Поле не может быть null

    public Studio(String adress) {
        this.address = adress;
    }

    /**
     * Метод, возвращающий адрес
     *
     * @return Адрес
     */
    public String getAddress() {
        return address;
    }

    /**
     * Метод, устанавливающий адрес
     *
     * @param address Новый адрес
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Метод, сравнивающий две студии
     *
     * @param anotherStudio Студия для сравнения
     * @return Целое число
     */
    @Override
    public int compareTo(Studio anotherStudio) {
        return this.address.compareTo(anotherStudio.getAddress());
    }

    /**
     * Переопределение метода, возвращающего строковое представление класса
     *
     * @return Строковое представление класса
     */
    @Override
    public String toString() {
        return "адрес студии: " + address;
    }
}
