import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConversionHandler implements ICurrencyConversion {

    private final String[] currencies = new String[]{"USD", "EUR", "GBP"};

    private final Map<String, CurrencyConverter> converters = new HashMap<>();

    private Date selectedDate;

    public CurrencyConversionHandler(){
        converters.put("Echtzeit", new LatestCurrencyConverter());
        converters.put("Historisch", new HistoricalCurrencyConverter(this));
        converters.put("Fix", new FixedCurrencyConverter());
    }

    @Override
    public String[] getCurrencies() {
        return currencies;
    }

    @Override
    public String[] getConverters() {
        return converters.keySet().toArray(new String[converters.size()]);
    }

    @Override
    public void setDate(Date date) {
        selectedDate = date;
    }
    @Override
    public Date getDate(){
        return this.selectedDate;
    }

    @Override
    public double performConversion(double amount, String sourceCurrency, String targetCurrency, String converter) {
        System.out.println("click!");
        final CurrencyConverter currencyConverter = converters.get(converter);
        return currencyConverter.convertCurrency(amount, sourceCurrency, targetCurrency);
    }
}
