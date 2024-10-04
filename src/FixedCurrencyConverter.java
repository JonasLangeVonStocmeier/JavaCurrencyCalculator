import java.util.HashMap;
import java.util.Map;

public class FixedCurrencyConverter implements CurrencyConverter{

    private final Map<String, Double> fixedExchangeRates = new HashMap<>();

    public FixedCurrencyConverter(){
        fixedExchangeRates.put("USD", 1.0);
        fixedExchangeRates.put("EUR", 0.85);
        fixedExchangeRates.put("GBP", 0.73);
    }

    @Override
    public double convertCurrency(double amount, String sourceCurrency, String targetCurrency) {
        final double sourceExchangeRates = fixedExchangeRates.get(sourceCurrency),
            targetExchangeRate = fixedExchangeRates.get(targetCurrency);

        return amount * targetExchangeRate / sourceExchangeRates;
    }
}
