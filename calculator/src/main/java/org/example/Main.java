package org.example;

import org.example.BusinessLogics.CurrencyRate;
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

        String stringCurrency = new CurrencyRate().GetCurrencyRate();

        double doubleCurrency = 0.0;
        boolean isInitialized = false;

        if (stringCurrency == null) {
            System.out.println("Не удалось автоматически определить курс валюты. Введите курс валюты вручную.");
            while (!isInitialized){
                try {
                    doubleCurrency = Double.parseDouble(scanner.nextLine());
                    isInitialized = true;
                }
                catch (Exception exception){
                    System.out.println("Вы ввели курс в неправильном формате.");
                }
            }
        }
        else {
            doubleCurrency = Double.parseDouble(stringCurrency);
        }

        System.out.println("Операция: " + operation);
        String result = new RegExAnalyzer().Calculate(operation_simplified, doubleCurrency);
        System.out.println("Результат операции: " + result);
    }
}

