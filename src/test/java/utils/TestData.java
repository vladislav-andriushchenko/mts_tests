package utils;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.requireNonNull;


public class TestData {

    private static final Faker faker = new Faker();

    private static final Map<String, Double> DURATION_PERCENTAGE_MAP = new HashMap<>() {{
        put("3 месяца", 19.69);
        put("4 месяца", 19.54);
        put("5 месяцев", 19.38);
        put("6 месяцев", 19.69);
        put("9 месяцев", 19.24);
        put("18 месяцев", 14.43);
        put("1 год", 18.37);
        put("2 года", 13.19);
        put("3 года", 12.45);
    }};

    public static String getRandomDuration() {
        return faker.options().option(DURATION_PERCENTAGE_MAP.keySet().toArray(new String[0]));
    }

    public static String getRandomAmount() {
        return String.valueOf(faker.number().numberBetween(10_000, 500_000_001));
    }

    public static double getAnnualRate(String duration) {
        return DURATION_PERCENTAGE_MAP.getOrDefault(duration, 0.0);
    }

    public static String formatCurrency(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ru"));
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        return formatter.format(amount) + " ₽";
    }

    public static String formatCurrencyWithDecimal(double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ru"));
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("#,##0.00", symbols);
        return formatter.format(amount) + " ₽";
    }

    public static String formatAnnualRate(double rate) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ru"));
        symbols.setDecimalSeparator(',');
        DecimalFormat formatter = new DecimalFormat("0.00", symbols);
        return formatter.format(rate) + "%";
    }
}
