package emented.lab7.common.entities;

import java.io.Serializable;

/**
 * Класс, хранящий информацию о студии
 */
public class Studio implements Serializable, Comparable<Studio> {

    /**
     * Поле, хранящее адрес студии (не может быть null)
     */
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
