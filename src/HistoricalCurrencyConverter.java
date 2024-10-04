import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class HistoricalCurrencyConverter implements CurrencyConverter {
    // https://api.freecurrencyapi.com/v1/historical
    private final ICurrencyConversion iCurrencyConversion;

    public HistoricalCurrencyConverter(ICurrencyConversion iCurrencyConversion){
        this.iCurrencyConversion = iCurrencyConversion;

    }
    @Override
    public double convertCurrency(double amount, String sourceCurrency, String targetCurrency) {
        final String apiURL = "https://api.freecurrencyapi.com/v1/historical",
                apiKey = "fca_live_ZW2SPe8hbhVyriNs2GhIMJcMvlcsjVBWel5m7oNe";

        LocalDate localDate = iCurrencyConversion.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        final String dateString = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            final String urlString = String.format("%s?apikey=%s&base_currency=%s&currencies=%s&date_from=%s&date_to=%s", apiURL, apiKey, sourceCurrency, targetCurrency, URLEncoder.encode(dateString, "UTF-8"), URLEncoder.encode(dateString, "UTF-8"));

            final URL url = new URL(urlString);
            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Fehler bei der Api Historical Anfrage: " + httpURLConnection.getResponseCode());
                return -1;
            }
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String response = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            bufferedReader.close();

            final double exchangeRate = extractExchangeRate(response, targetCurrency);
            return exchangeRate * amount;

        } catch(Exception e){
            e.printStackTrace();
        }


        return 0;
    }

    // "data":{"2024-09-10":{"EUR":0.9072301419}}}
    private double extractExchangeRate(String data, String targetCurrency){
        final String currencyString = "\"" + targetCurrency + "\":";
        final int index = data.indexOf(currencyString) + currencyString.length();
        final String number = data.substring(index, data.length() - 3);

        return Double.parseDouble(number);
    }
}
