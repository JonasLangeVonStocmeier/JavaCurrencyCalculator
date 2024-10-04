import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class CurrencyConverterWindow {

    private final ICurrencyConversion iCurrencyConversion;

    private JTextField currencyAmount;

    private JLabel resultLabel;

    private JComboBox<String> sourceCurrency;

    public CurrencyConverterWindow(ICurrencyConversion iCurrencyConversion) {
        this.iCurrencyConversion = iCurrencyConversion;

        final JFrame jFrame = new JFrame();

        jFrame.add(createOriginalValuePanel(), BorderLayout.NORTH);
        jFrame.add(createTargetValuePanel(), BorderLayout.CENTER);
        jFrame.add(createResultPanel(), BorderLayout.SOUTH);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(500, 500);
        jFrame.setVisible(true);

        jFrame.pack();

    }

    public static void main(String[] args) {
        new CurrencyConverterWindow(new CurrencyConversionHandler()) {
        };
    }

    private JPanel createOriginalValuePanel() {
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        currencyAmount = new JTextField(10);

        sourceCurrency = new JComboBox<>();

        sourceCurrency.setModel(new DefaultComboBoxModel<>(iCurrencyConversion.getCurrencies()));

        jPanel.add(currencyAmount);
        jPanel.add(sourceCurrency);

        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ausganswert"));

        return jPanel;
    }

    private JPanel createTargetValuePanel() {
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        final JComboBox<String> currency = new JComboBox<>();
        currency.setModel(new DefaultComboBoxModel<>(iCurrencyConversion.getCurrencies()));

        final JComboBox<String> converter = new JComboBox<>();
        converter.setModel(new DefaultComboBoxModel<>(iCurrencyConversion.getConverters()));

        final JSpinner jSpinner = new JSpinner(new SpinnerDateModel());
        jSpinner.setEditor(new JSpinner.DateEditor(jSpinner, "yyyy-MM-dd"));


        final JButton jButton = new JButton("Umrechnen");
        jButton.addActionListener(e -> {
            iCurrencyConversion.setDate((Date)jSpinner.getValue());

            final double result = iCurrencyConversion.performConversion(Integer.parseInt(currencyAmount.getText()), (String) sourceCurrency.getSelectedItem(),(String) currency.getSelectedItem(), (String) converter.getSelectedItem());
            resultLabel.setText("Ergebnis: " + result);
        });

        jPanel.add(currency);
        jPanel.add(converter);
        jPanel.add(jSpinner);
        jPanel.add(jButton);

        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Zielwährung"));

        return jPanel;
    }

    private JPanel createResultPanel() {
        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        resultLabel = new JLabel("Ergebnis: ");

        jPanel.add(resultLabel);

        jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ergebnis"));
        return jPanel;
    }
}
