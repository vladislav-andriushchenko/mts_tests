package pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class SearchResultsPage {

    public void verifyResultsPresent() {
        $("[data-testid='SearchResults__ResultsFound']")
                .shouldBe(visible);
    }

    public void applyStarFilter(int stars) {
        $("[data-testid='Filter__StarRating']").click();
        $$("label").findBy(text(stars + " звезды")).click();
        sleep(1000); // wait for filter to apply
    }

    public void openFirstHotel() {
        $$("[data-testid='SearchResults__ListItem']").first().click();
    }
}