package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//*[@name=\"login\"]").setValue(registeredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $x("//*[@id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@name=\"login\"]").setValue(notRegisteredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//*[@name=\"login\"]").setValue(blockedUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//*[@name=\"login\"]").setValue(wrongLogin);
        $x("//*[@name=\"password\"]").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//*[@name=\"login\"]").setValue(registeredUser.getLogin());
        $x("//*[@name=\"password\"]").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
