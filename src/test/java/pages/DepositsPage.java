package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DepositsPage {

    private final SelenideElement
            amountInput = $("input[data-testid='input-slider']"),
            durationSelect = $("input[role='combobox']"),
            maxInterestRateCheckbox = $("input[role='switch']").parent(),
            compoundInterestCheckbox = $("div[type='checkbox']"),
            compoundCheckMark = $("[data-testid='icon_baseX24/ic-check']"),
            slider = $(".rc-slider"),
            sliderHandle = $(".rc-slider-handle"),
            withoutWithdrawalAndTopUpRadio = $$("input[type='radio']").get(0),
            withWithdrawalAndNotTopUpRadio = $$("input[type='radio']").get(1),
            withWithdrawalAndTopUpRadio = $$("input[type='radio']").get(2),
            resultSection = $$("section[data-testid='flexbox']").get(1),
            amountTotal = $$("h2[data-testid='heading']").get(0),
            interestRateTotal = $$("h2[data-testid='heading']").get(1),
            contractInterestRateTotal = $$("section[data-testid='flexbox']")
                    .get(1).$$("div[data-testid='flexbox']")
                    .get(5).$$("div[data-testid='text']").get(1);

    private final ElementsCollection durationList = $$("div[role='option']");

    public DepositsPage openPage() {
        open("/vkladi/vklad-mts-dengi");
        sleep(2000);
        return this;
    }

    public DepositsPage setSliderPosition(Double percentage) {
        sliderHandle.shouldBe(visible, enabled);

        int width = slider.getSize().getWidth();
        int moveTo = (int) (width * percentage) - (width / 2);

        executeJavaScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", sliderHandle);
        sleep(300);
        actions()
                .clickAndHold(sliderHandle)
                .moveByOffset(moveTo, 0)
                .release()
                .perform();
        return this;
    }

    public DepositsPage setDuration(String duration) {
        durationSelect.click();

        durationList
                .filterBy(text(duration))
                .first()
                .shouldBe(visible)
                .click();

        return this;
    }

    public DepositsPage setMaxInterestRate() {
        maxInterestRateCheckbox.click();
        return this;
    }

    public DepositsPage setCompoundInterest() {
        compoundInterestCheckbox.click();
        return this;
    }

    public DepositsPage setDepositType(String type) {
        $(byText(type)).parent().click();
        return this;
    }

    public DepositsPage setAmount(String myAmount) {
        String inputSequence = Keys.chord(Keys.CONTROL, "a") + myAmount;
        amountInput.scrollIntoView(true).sendKeys(inputSequence);
        return this;
    }

    public String getAmountValue() {
        return sliderHandle.getAttribute("aria-valuenow");
    }

    public String getDurationValue() {
        return durationSelect.getValue();
    }

    public void checkResult(String text) {
        resultSection.shouldHave(text(text));
    }

    public void checkInterestRateTotalHasValue(String text) {
        interestRateTotal.shouldHave(text(text));
    }

    public void checkInterestRateTotalHasNotValue(String text) {
        interestRateTotal.shouldNotHave(text(text));
    }

    public void checkAmountTotalHasValue(String text) {
        amountTotal.shouldHave(text(text));
    }

    public void checkAmountTotalHasNotValue(String text) {
        amountTotal.shouldNot(text(text));
    }

    public void checkContractInterestRateTotal(String text) {
        contractInterestRateTotal.shouldHave(text(text));
    }

    public DepositsPage checkSelectedWithoutWithdrawalAndTopUpRadio() {
        withoutWithdrawalAndTopUpRadio.shouldHave(attribute("value", "on"));
        return this;
    }

    public DepositsPage checkDisabledWithoutWithdrawalAndTopUpRadio() {
        withoutWithdrawalAndTopUpRadio.shouldBe(disabled);
        return this;
    }

    public DepositsPage checkSelectedWithWithdrawalAndNotTopUpRadio() {
        withWithdrawalAndNotTopUpRadio.shouldHave(attribute("value", "on"));
        return this;
    }

    public DepositsPage checkDisabledWithWithdrawalAndNotTopUpRadio() {
        withWithdrawalAndNotTopUpRadio.shouldBe(disabled);
        return this;
    }

    public DepositsPage checkSelectedWithWithdrawalAndTopUpRadio() {
        withWithdrawalAndTopUpRadio.shouldHave(attribute("value", "on"));
        return this;
    }
    public DepositsPage checkDisabledWithWithdrawalAndTopUpRadio() {
        withWithdrawalAndTopUpRadio.shouldBe(disabled);
        return this;
    }

    public DepositsPage checkSelectedCompoundInterest() {
        compoundCheckMark.shouldBe(visible);
        return this;
    }

    public DepositsPage checkDisabledCompoundInterest() {
        compoundInterestCheckbox.shouldHave(attribute("disabled"));
        return this;
    }

    public DepositsPage checkDisabledDuration() {
        durationSelect.shouldBe(disabled);
        return this;
    }
}
