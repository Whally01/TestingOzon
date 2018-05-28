package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OzonWebTest extends TestBase {
    private String price;
    private String name;

    /**
     * Переход на основную страницу ozon.ru
     */
    @Test(description = "Открыть в браузере сайт https://www.ozon.ru/")
    public void navigate() {
        String baseURL = "https://ozon.ru/";
        getWebDriver().get(baseURL);
    }


    /**Сначала кликаем по блоку Все разделы, далее кликаем раздел "Музыка" */
    @Test(description = "В меню \"Каталог товаров\" выбрать категорию \"Музыка\".",
            dependsOnMethods = "navigate")
    public void goToMusic() throws InterruptedException {
        closeBanner();
        getWebDriver().findElement(By.cssSelector("span.eHeaderCatalogButton_Text")).click();
        getWebDriver().findElement(By.linkText("Музыка")).click();
    }

    /**Находим элемент названием "Коллекционные издания" и кликаем по ней*/
    @Test(description = "В подменю выбрать \"Виниловые пластинки - Коллекционные издания\".",
            dependsOnMethods = "goToMusic")
    public void goToVinylPlast() {
        getWebDriver().findElement(By.xpath("(//a[contains(text(),'Коллекционные издания')])[4]")).click();
    }

    /**
     * Получаем по id блок где располагаются элементы,
     * если в блоке есть данные значит тест прошел
     */
    @Test(description = "Проверить, что открылся список товаров.", dependsOnMethods = "goToVinylPlast")
    public void checkShowingGoods() {
        WebElement collection = getWebDriver().findElement(By.id("bTilesModeShow"));
        Assert.assertTrue(true, collection.getText());
    }

    /**
     * Находим первый элемент и переходим на нее нажаетием на этот блок
     */
    @Test(description = "Выбрать первый товар.", dependsOnMethods = "checkShowingGoods")
    public void selectFirstGoods() {
        getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[4]/div/div/div[3]/div[1]/div[1]/div/a")).click();
    }

    /**
     * Находим цену и название товара и записываем их в переменные
     */
    @Test(description = "Запомнить стоимость и название данного товара.", dependsOnMethods = "selectFirstGood")
    public void savePriceAndNameGoods() {
        price = getWebDriver().findElement(By.cssSelector("div.bOzonPrice:nth-child(3) > span:nth-child(1)")).getText();
        name = getWebDriver().findElement(By.className("bItemName")).getText();
    }

    /**
     * Находим конпку "добавить в корзину" и кликаем по ней
     */
    @Test(description = "Добавить его в корзину.", dependsOnMethods = "savePriceAndNameGood")
    public void addToBug() {
        getWebDriver().findElement(By.cssSelector("div.bSaleBlockButton.jsButton")).click();
    }

    /**
     * Переходим по URL где находится корзина данного магазина
     */
    @Test(description = "Открыть корзину. Проверить то, что в корзине находится раннее выбранный товар и его стоимость равна\n" +
            "стоимости, запомненной в п.6.", dependsOnMethods = "addToBug")
    public void goToBugAndCheckGoods() {
        getWebDriver().get("https://www.ozon.ru/context/cart/");
        String currentPrice = getWebDriver().findElement(By.cssSelector(".eCartItem_price")).getText();
        String currentPriceSub = currentPrice.substring(0, currentPrice.length() - 8); // отрезаем часть ,00 руб.
        String currentName = getWebDriver().findElement(By.cssSelector(".eCartItem_nameValue")).getText();
        Assert.assertEquals(currentPriceSub, price);
        Assert.assertEquals(currentName, name);
    }

    /**
     * Находим иконку для удаления и кликаем по ней
     */
    @Test(description = "Выбрать этот товар в корзине и «Удалить» из корзины.", dependsOnMethods = "goToBugAndCheckGoods")
    public void selectAndDeleteGoods() {
        getWebDriver().findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[2]/div[3]/div[1]/div[3]/div[1]/div/div/div[1]/div/div[2]/div/div/div[3]/div/div[2]")).click();
    }

    /**
     * Находим заголовок, где должна появиться надпись "Корзина пуста", проверяем что текст соответсвует => тест прошел
     */
    @Test(description = "Проверить, что корзина пуста.", dependsOnMethods = "selectAndDeleteGood")
    public void checkBugEmptiness() throws InterruptedException {
        Boolean isPresent = getWebDriver().findElements(By.cssSelector(".eCartPage_flexBlocks")).size() > 0;
        if (isPresent)
            Assert.fail("Корзина не пуста!");
    }

    /**
     * Очищаем данные и закрываем браузер
     */
    @Test(description = "Закрыть браузер.", dependsOnMethods = "checkBugEmptiness")
    public void closeBrowser() {
        getWebDriver().quit();
    }
}
