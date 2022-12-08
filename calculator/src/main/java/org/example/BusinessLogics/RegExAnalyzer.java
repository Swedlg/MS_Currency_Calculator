package org.example.BusinessLogics;

import org.example.MyExceptions.FailedToSimplifyException;
import org.example.MyExceptions.NullStringException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExAnalyzer {

    double coefficient;

    public String Calculate(String str, double coefficient) throws FailedToSimplifyException, NullStringException {

        if (str == null || str.length() == 0){
            throw new NullStringException();
        }

        this.coefficient = coefficient;

        int before_cycle_length = str.length();
        int after_cycle_length = -1;

        String result = str;

        // Запрос крутиться в цикле и постепенно упрощается
        while (before_cycle_length > after_cycle_length){
            before_cycle_length = result.length();
            result = CalculateCycle(result);
            after_cycle_length = result.length();
        }

        //Если результирующая строка соответсвует шаблону записи доллара
        Matcher mather = Pattern.compile("^d\\d+(,\\d{1,2})?$").matcher(result);
        if (mather.find()) {
            return result;
        }

        //Если результирующая строка соответсвует шаблону записи рубля
        mather = Pattern.compile("^\\d+(,\\d{1,2})?р$").matcher(result);
        if (mather.find()) {
            return result;
        }

        //Если не было совпадение выдаем ошибку некорректности ввода данных
        throw new FailedToSimplifyException();
    }


    private String CalculateCycle(String str){

        //Если встречаем простой метод конвертации долларов в рубли
        Matcher mather = Pattern.compile("toRubles\\(\\$\\d+(,\\d{1,2})?\\)").matcher(str);
        List<String> entries = new ArrayList<>();
        while (mather.find()) {
            entries.add(mather.group());
        }
        for (var entry : entries) {
            //Убираем название функции и кавычки
            String dollars_value = entry.replace("toRubles(", "").replace(")", "");

            //Получаем значение в рублях
            String rubles_value = DollarsToRubles(dollars_value);

            String regex_entry = entry.replace("$", "\\$")
                    .replace("(", "\\(").replace(")", "\\)");

            //String regex_entry = Pattern.quote(entry);

            //Заменяем вхождение на посчитанное значение
            str = str.replaceFirst(regex_entry, rubles_value);
        }

        //Если встречаем простой метод конвертации рублей в доллары
        mather = Pattern.compile("toDollars\\(\\d+(,\\d{1,2})?р\\)").matcher(str);
        entries = new ArrayList<>();
        while (mather.find()) {
            entries.add(mather.group());
        }
        for (var entry : entries) {
            //Убираем название функции и кавычки
            String rubles_value = entry.replace("toDollars(", "").replace(")", "");

            //Получаем значение в рублях
            String dollars_value = RublesToDollars(rubles_value);

            String regex_entry = entry.replace("$", "\\$")
                    .replace("(", "\\(").replace(")", "\\)");

            //String regex_entry = Pattern.quote(entry);

            //Заменяем вхождение на посчитанное значение
            str = str.replaceFirst(regex_entry, dollars_value.replace("$", "\\$"));
        }

        //Если встречаем простой метод сложения долларов
        mather = Pattern.compile("\\$\\d+(,\\d{1,2})?\\+\\$\\d+(,\\d{1,2})?").matcher(str);
        entries = new ArrayList<>();
        while (mather.find()) {
            entries.add(mather.group());
        }
        for (var entry : entries) {
            String[] values = entry.split("\\+");

            String result = SumOfDollars(values[0], values[1]);

            //Так как в replaceFirst передается RegEx добавляем экранирование знаку '+'
            entry = entry.replace("+", "\\+").replace("$", "\\$");

            str = str.replaceFirst(entry, result.replace("$", "\\$"));
        }

        //Если встречаем простой метод сложения рублей
        mather = Pattern.compile("\\d+(,\\d{1,2})?р\\+\\d+(,\\d{1,2})?р").matcher(str);
        entries = new ArrayList<>();
        while (mather.find()) {
            entries.add(mather.group());
        }
        for (var entry : entries) {

            String[] values = entry.split("\\+");

            String result = SumOfRubles(values[0], values[1]);

            //Так как в replaceFirst передается RegEx добавляем экранирование знаку '+'
            entry = entry.replace("+", "\\+");

            str = str.replaceFirst(entry, result);
        }

        return str;
    }

    private String DollarsToRubles(String string_dollars){

        //Убираем знак доллара
        string_dollars = string_dollars.replace("$", "");

        //Заменяем запятую на точку, если она есть (для конвертации в тип Double)
        string_dollars = string_dollars.replace(",", ".");

        //Получаем количество долларов
        double double_dollars = Double.parseDouble(string_dollars);

        //Получаем количество рублей
        double double_rubles = double_dollars * coefficient;

        //Получаем строку количества рублей
        String string_rubles = String.format("%.2f",double_rubles);

        //Заменяем точку на запятую как и было
        string_rubles = string_rubles.replace(".", ",");

        //Добавляем знак рубля в конце
        string_rubles = string_rubles.concat("р");

        return string_rubles;
    }

    private String RublesToDollars(String string_rubles){

        //Убираем знак рубля
        string_rubles = string_rubles.replace("р", "");

        //Заменяем запятую на точку, если она есть (для конвертации в тип Double)
        string_rubles = string_rubles.replace(",", ".");

        //Получаем количество рублей
        double double_rubles = Double.parseDouble(string_rubles);

        //Получаем количество долларов
        double double_dollars = double_rubles / coefficient;

        //Получаем строку количества долларов
        String string_dollars = String.format("%.2f",double_dollars);

        //Заменяем точку на запятую как и было
        string_dollars = string_dollars.replace(".", ",");

        //Добавляем в начале знак доллара
        string_dollars = "$".concat(string_dollars);

        return string_dollars;
    }


    private String SumOfDollars(String value1, String value2){
        //Убираем знак доллара
        value1 = value1.replace("$", "");
        value2 = value2.replace("$", "");

        //Заменяем запятую на точку
        value1 = value1.replace(",", ".");
        value2 = value2.replace(",", ".");

        double double_result = Double.parseDouble(value1) + Double.parseDouble(value2);

        String string_result = String.format("%.2f", double_result);

        string_result = string_result.replace(".", ",");

        string_result = "$".concat(string_result);

        return string_result;
    }

    private String SumOfRubles(String value1, String value2){
        //Убираем знак доллара
        value1 = value1.replace("р", "");
        value2 = value2.replace("р", "");

        //Заменяем запятую на точку
        value1 = value1.replace(",", ".");
        value2 = value2.replace(",", ".");

        double double_result = Double.parseDouble(value1) + Double.parseDouble(value2);

        String string_result = String.format("%.2f", double_result);

        string_result = string_result.replace(".", ",");

        string_result = string_result.concat("р");

        return string_result;
    }
}
