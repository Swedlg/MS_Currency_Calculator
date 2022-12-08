package org.example.MyExceptions;

public class FailedToSimplifyException extends Exception {
    @Override
    public String toString() {
        return "Ошибка. Проверьте корректность входных данных.";
    }
}
