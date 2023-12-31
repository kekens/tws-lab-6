package ru.kekens.exception;

public class AccountServiceException extends Exception {

    private static final String DEFAULT_MESSAGE = "Ошибка в сервисе счетов: ";
    public static AccountServiceException DEFAULT_INSTANCE =
            new AccountServiceException(DEFAULT_MESSAGE);

    public AccountServiceException(String message) {
        super(message);
    }

    public AccountServiceException(String baseMessage, String message) {
        super(baseMessage + ": " + message);
    }


}
