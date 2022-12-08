package org.example;

import org.example.BusinessLogics.RegExAnalyzer;
import org.example.MyExceptions.FailedToSimplifyException;
import org.example.MyExceptions.NullStringException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FailedToSimplifyException, NullStringException {
        Scanner scanner = new Scanner(System.in);
        String operation = scanner.nextLine();

        // Например
        // toRubles($85,4 +   toDollars(737р + toRubles($85,4)) ) + toRubles($85,4 + toDollars(737р))

        String operation_simplified = operation.replace(" ", "");

        System.out.println("Операция: " + operation);
        String result = new RegExAnalyzer().Calculate(operation_simplified, 60.0);
        System.out.println("Результат операции: " + result);
    }
}

