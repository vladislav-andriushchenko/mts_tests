package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.DepositsPage;
import utils.TestBase;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestData.*;
import static io.qameta.allure.Allure.step;


public class DepositsTests extends TestBase {

    private final DepositsPage depositsPage = new DepositsPage();

    @DisplayName("Проверка успешного запроса с минимальной суммой и максимальной ставкой")
    @Test
    public void checkDepositWithMinValueAndMaxInterestRateTest() {
        String duration = "6 месяцев";

        step("Открыть страницу и поставить минимальную цену", () -> {
            depositsPage
                    .openPage()
                    .setSliderPosition(0.0)
                    .setMaxInterestRate();
        });

        step("Проверить, что срок выбран корректно", () -> {
            assertEquals(duration,
                    depositsPage.getDurationValue(),
                    "Duration must be equal to: " + duration);
        });

        step("Проверить, что доп. опции недоступны при выбранной макс. ставке", () -> {
            depositsPage
                    .checkDisabledWithWithdrawalAndTopUpRadio()
                    .checkDisabledWithWithdrawalAndNotTopUpRadio()
                    .checkDisabledWithoutWithdrawalAndTopUpRadio()
                    .checkDisabledCompoundInterest()
                    .checkDisabledDuration()
                    .checkSelectedCompoundInterest();
        });

        String totalAnnualRate = formatAnnualRate(20.51);
        String totalAmount = formatCurrencyWithDecimal(11017.05);

        step("Проверить, что финальная ставка и сумма корректы", () -> {
            depositsPage.checkInterestRateTotalHasValue(totalAnnualRate);
            depositsPage.checkAmountTotalHasValue(totalAmount);
        });
    }

    @DisplayName("Проверка успешного запроса с максимальной суммой и максимальной ставкой")
    @Test
    public void checkDepositWithMaxValueAndMaxInterestRateTest1() {
        String duration = "6 месяцев";

        step("Открыть страницу и поставить максимальную цену", () -> {
            depositsPage
                    .openPage()
                    .setSliderPosition(2.0)
                    .setMaxInterestRate();
        });

        step("Проверить, что срок выбран корректно", () -> {
            assertEquals(duration,
                    depositsPage.getDurationValue(),
                    "Duration must be equal to: " + duration);
        });

        step("Проверить, что доп. опции недоступны при выбранной макс. ставке", () -> {
            depositsPage
                    .checkDisabledWithWithdrawalAndTopUpRadio()
                    .checkDisabledWithWithdrawalAndNotTopUpRadio()
                    .checkDisabledWithoutWithdrawalAndTopUpRadio()
                    .checkDisabledCompoundInterest()
                    .checkDisabledDuration()
                    .checkSelectedCompoundInterest()
                    .checkSelectedWithoutWithdrawalAndTopUpRadio();
        });

        String totalAnnualRate = formatAnnualRate(20.51);
        String totalAmount = formatCurrency(550852597);

        step("Проверить, что финальная ставка и сумма корректы", () -> {
            depositsPage.checkInterestRateTotalHasValue(totalAnnualRate);
            depositsPage.checkAmountTotalHasValue(totalAmount);
        });
    }

    @ParameterizedTest(name = "Проверка получения ошибки при создании депозита: \"С пополнением и снятием\" и сроком = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkUnavailableDepositWithTopUpAndWithdrawalTest(String duration) {
        step("Открыть страницу, поставить минимальную цену, настроить депозит", () -> {
            depositsPage
                    .openPage()
                    .setDuration(duration)
                    .setSliderPosition(0.0)
                    .setDepositType("С пополнением и снятием");
        });

        step("Проверить, что выбран \"С пополнением и снятием\"", () -> {
            depositsPage.checkSelectedWithWithdrawalAndTopUpRadio();
        });

        step("Проверить наличие ошибки", () -> {
            depositsPage.checkResult("Нет вклада с выбранными параметрами");
            depositsPage.checkAmountTotalHasValue("0");
        });

    }

    @ParameterizedTest(name = "Проверка получения ошибки при создании депозита: \"С пополнением и без снятия\" и сроком = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkUnavailableDepositWithTopUpAndNotWithdrawalTest(String duration) {
        step("Открыть страницу, поставить минимальную цену, настроить депозит", () -> {
            depositsPage
                    .openPage()
                    .setDuration(duration)
                    .setSliderPosition(0.0)
                    .setDepositType("С пополнением и снятием");
        });

        step("Проверить, что выбран \"С пополнением и без снятия\"", () -> {
            depositsPage.checkSelectedWithWithdrawalAndNotTopUpRadio();
        });

        step("Проверить наличие ошибки", () -> {
            depositsPage.checkResult("Нет вклада с выбранными параметрами");
            depositsPage.checkAmountTotalHasValue("0");
        });
    }

    @ParameterizedTest(name = "Проверка получения ставки в зависимости от срока = {0}")
    @ValueSource(strings = {"3 месяца", "4 месяца", "5 месяцев", "6 месяцев", "9 месяцев", "18 месяцев", "1 год", "2 года", "3 года"})
    void checkAnnualRateTest(String duration) {
        String rate = formatAnnualRate(getAnnualRate(duration));

        step("Открыть страницу, поставить срок и сумму", () -> {
            depositsPage
                    .openPage()
                    .setDuration(duration)
                    .setAmount(getRandomAmount());
        });

        step("Проверить, что ставка совпадает с выставленным сроком вклада", () -> {
            depositsPage.checkContractInterestRateTotal(rate);
        });
    }

    @DisplayName("Проверка изменения базовой ставки при оставленных процентах на вкладе")
    @Test
    void checkChangingAnnualRateWithCompoundInterestTest() {
        String duration = getRandomDuration();
        String amount = getRandomAmount();
        String annualRate = formatAnnualRate(getAnnualRate(duration));

        step("Открыть страницу, поставить срок и сумму, оставить проценты на вкладе", () -> {
            depositsPage
                    .openPage()
                    .setAmount(amount)
                    .setDuration(duration)
                    .setCompoundInterest();
        });

        step("Проверить, что срок выбран корректно", () -> {
            assertEquals(duration,
                    depositsPage.getDurationValue(),
                    "Duration must be equal to: " + duration);
        });

        step("Проверить, что сумма введена корректно", () -> {
            assertEquals(amount,
                    depositsPage.getAmountValue(),
                    "Deposit amount must be equal to: " + amount);
        });


        step("Проверить, что финальная ставка и сумма изменились", () -> {
            depositsPage.checkInterestRateTotalHasNotValue(annualRate);
            depositsPage.checkAmountTotalHasNotValue(amount);
        });
    }
}
