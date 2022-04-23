package emented.lab7.common.entities;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Класс, хранящий координаты музыкальной студии
 */
public class Coordinates implements Serializable {

    /**
     * Константа, отвечающая за максимальную координату по X
     */
    public static final long MAX_X = 947;

    /**
     * Константа, отвечающая за максимальную координату по Y
     */
    public static final long MAX_Y = 104;

    /**
     * Поле, хранящее в себе координату по X
     */
    private double x; //Максимальное значение поля: 947

    /**
     * Поле, хранящее в себе координату по Y
     */
    private Float y; //Максимальное значение поля: 104, Поле не может быть null

    public Coordinates(double x, Float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Метод, возвращающий координату по X
     *
     * @return координата X
     */
    public double getX() {
        return this.x;
    }

    /**
     * Метод, устанавливающий координату по X
     *
     * @param newX Новая координата по X
     */
    public void setX(double newX) {
        this.x = newX;
    }

    /**
     * Метод, возвращающий координату по Y
     *
     * @return координата Y
     */
    public Float getY() {
        return this.y;
    }

    /**
     * Метод, устанавливающий координату по Y
     *
     * @param newY Новая координата по Y
     */
    public void setY(Float newY) {
        this.y = newY;
    }

    /**
     * Переопределение метода, возвращающего строковое представление класса
     *
     * @return Строковое представление класса
     */
    @Override
    public String toString() {
        return "X = " + x + ", Y = " + y;
    }
}
