package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.DepositsPage;
import utils.TestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestData.*;


public class DepositsTests extends TestBase {

    private final DepositsPage depositsPage = new DepositsPage();

    @DisplayName("Проверка успешного запроса с минимальной суммой и максимальной ставкой")
    @Test
    public void checkDepositWithMinValueAndMaxInterestRateTest() {
        String duration = "6 месяцев";

        depositsPage
                .openPage()
                .setSliderPosition(0.0)
                .setMaxInterestRate();

        assertEquals(duration,
                depositsPage.getDurationValue(),
                "Deposit amount must be equal to: " + duration);

        String totalAnnualRate = formatAnnualRate(20.51);
        String totalAmount = formatCurrencyWithDecimal(11017.05);

        depositsPage.checkInterestRateTotalHasValue(totalAnnualRate);
        depositsPage.checkAmountTotalHasValue(totalAmount);

        depositsPage
                .checkDisabledWithWithdrawalAndTopUpRadio()
                .checkDisabledWithWithdrawalAndNotTopUpRadio()
                .checkDisabledWithoutWithdrawalAndTopUpRadio()
                .checkDisabledCompoundInterest()
                .checkDisabledDuration()
                .checkSelectedCompoundInterest();
    }

    @DisplayName("Проверка успешного запроса с максимальной суммой и максимальной ставкой")
    @Test
    public void checkDepositWithMaxValueAndMaxInterestRateTest1() {
        String duration = "6 месяцев";

        depositsPage
                .openPage()
                .setSliderPosition(2.0)
                .setMaxInterestRate();

        assertEquals(duration,
                depositsPage.getDurationValue(),
                "Deposit amount must be equal to: " + duration);

        String totalAnnualRate = formatAnnualRate(20.51);
        String totalAmount = formatCurrency(550852597);

        depositsPage.checkInterestRateTotalHasValue(totalAnnualRate);
        depositsPage.checkAmountTotalHasValue(totalAmount);

        depositsPage
                .checkDisabledWithWithdrawalAndTopUpRadio()
                .checkDisabledWithWithdrawalAndNotTopUpRadio()
                .checkDisabledWithoutWithdrawalAndTopUpRadio()
                .checkDisabledCompoundInterest()
                .checkDisabledDuration()
                .checkSelectedCompoundInterest()
                .checkSelectedWithoutWithdrawalAndTopUpRadio();
    }

    @ParameterizedTest(name = "Проверка получения ошибки при создании депозита: \"С пополнением и снятием\" и сроком = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkUnavailableDepositWithTopUpAndWithdrawalTest(String duration) {
        depositsPage
                .openPage()
                .setDuration(duration)
                .setSliderPosition(0.0)
                .setDepositType("С пополнением и снятием");

        depositsPage.checkSelectedWithWithdrawalAndTopUpRadio();

        depositsPage.checkResult("Нет вклада с выбранными параметрами");
    }

    @ParameterizedTest(name = "Проверка получения ошибки при создании депозита: \"С пополнением и без снятия\" и сроком = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkUnavailableDepositWithTopUpAndNotWithdrawalTest(String duration) {
        depositsPage
                .openPage()
                .setDuration(duration)
                .setSliderPosition(0.0)
                .setDepositType("С пополнением и без снятия");

        depositsPage.checkSelectedWithWithdrawalAndNotTopUpRadio();

        depositsPage.checkResult("Нет вклада с выбранными параметрами");
    }

    @ParameterizedTest(name = "Проверка получения ставки в зависимости от срока = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkAnnualRateTest(String duration) {
        String rate = formatAnnualRate(getAnnualRate(duration));

        depositsPage
                .openPage()
                .setDuration(duration)
                .setAmount(getRandomAmount());

        depositsPage.checkContractInterestRateTotal(rate);
    }

    @DisplayName("Проверка изменения базовой ставки при оставленных процентах на вкладе")
    @Test
    void checkChangingAnnualRateWithCompoundInterestTest() {
        String duration = getRandomDuration();
        String amount = getRandomAmount();
        String annualRate = formatAnnualRate(getAnnualRate(duration));

        depositsPage
                .openPage()
                .setAmount(amount)
                .setDuration(duration)
                .setCompoundInterest();

        assertEquals(amount,
                depositsPage.getAmountValue(),
                "Deposit amount must be equal to: " + amount);
        assertEquals(duration,
                depositsPage.getDurationValue(),
                "Deposit amount must be equal to: " + duration);

        depositsPage.checkInterestRateTotalHasNotValue(annualRate);
        depositsPage.checkAmountTotalHasNotValue(amount);
    }
}
