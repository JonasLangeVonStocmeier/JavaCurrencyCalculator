import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LatestCurrencyConverter implements CurrencyConverter {
    // fca_live_ZW2SPe8hbhVyriNs2GhIMJcMvlcsjVBWel5m7oNe
    //https://api.freecurrencyapi.com/v1/latest?apikey=base_currency&currencies

    @Override
    public double convertCurrency(double amount, String sourceCurrency, String targetCurrency) {
        final String apiURL = "https://api.freecurrencyapi.com/v1/latest", apiKey = "fca_live_ZW2SPe8hbhVyriNs2GhIMJcMvlcsjVBWel5m7oNe";
        final String urlString = String.format("%s?apikey=%s&base_currency=%s&currencies=%s", apiURL, apiKey, sourceCurrency, targetCurrency);

        try {
            final URL url = new URL(urlString);
            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Fehler bei der API anfrage" + httpURLConnection.getResponseCode());
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
            return amount * exchangeRate;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return 0;
    }

    private double extractExchangeRate(String data, String targetCurrency){
        final  String currencyString = "\"" + targetCurrency + "\":";
        final int index = data.indexOf(currencyString) + targetCurrency.length() + 3;
        final String number = data.substring(index, data.length() - 2);

        return Double.parseDouble(number);
    }

}
