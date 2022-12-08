package org.example.BusinessLogics;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

public class CurrencyRate {

    public String GetCurrencyRate(){
        try {
            MonetaryAmount oneDollar = Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(1).create();
            CurrencyConversion conversionEUR = MonetaryConversions.getConversion("RUB");
            MonetaryAmount convertedAmountUSDtoEUR = oneDollar.with(conversionEUR);
            return convertedAmountUSDtoEUR.getNumber().toString();
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
