package org.example.MyExceptions;

public class NullStringException extends Exception {
    @Override
    public String toString() {
        return "Ошибка. На вход передана нулевая строка.";
    }
}
