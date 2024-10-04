import java.util.Date;


public interface ICurrencyConversion {

    String[] getCurrencies();

    String[] getConverters();

    double performConversion(double amount, String sourceCurrency, String targetCurrency, String converter);

    void setDate(Date date);

    Date getDate();
}
