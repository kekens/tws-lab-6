package ru.kekens;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.kekens.dto.Account;
import ru.kekens.dto.AccountsRequest;
import ru.kekens.dto.KeyValueParamsDto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestfulServiceClient {

    private static final String URI = "http://localhost:8080/tws-4/api/accounts";

    public static void main(String[] args) throws ParseException {

        // Создаем клиента
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI);

        // Запросы по поиску счетов
        getAccounts(target);

        // Запросы по вставке счетов


    }

    private static void printAccountInfo(Account acc) {
        System.out.printf("Account %d: label - %s;\t code - %s;\t category - %s;\t amount - %s;\t openDate - %s\n",
                acc.getId(), acc.getLabel(), acc.getCode(), acc.getCategory(),
                acc.getAmount(), acc.getOpenDate());
    }

    private static void printAccountInfo(List<Account> accList) {
        for (Account acc : accList) {
            printAccountInfo(acc);
        }
    }

    private static void getAccounts(WebTarget target) throws ParseException {
        System.out.println("------ START GET ACCOUNTS ------ ");
        // Запрос 0
        System.out.println("Request 0 - All");
        List<Account> accountList = getAccounts(target, null);
        printAccountInfo(accountList);

        // Request 1
        AccountsRequest request1 = new AccountsRequest();
        KeyValueParamsDto paramsDto1 = new KeyValueParamsDto();
        paramsDto1.setKey("category");
        paramsDto1.setValue("personal");
        paramsDto1.setCompareOperation("=");
        paramsDto1.setLogicOperation("AND");
        request1.getList().add(paramsDto1);
        accountList = getAccounts(target, request1);

        System.out.println("\nRequest 1 - by category \"personal\". Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        // Запрос 2
        AccountsRequest request2 = new AccountsRequest();
        KeyValueParamsDto paramsDto2 = new KeyValueParamsDto();
        paramsDto2.setKey("open_date");
        paramsDto2.setValue("2022-01-01");
        paramsDto2.setCompareOperation(">");
        paramsDto2.setLogicOperation("AND");
        request2.getList().add(paramsDto2);
        accountList = getAccounts(target, request2);

        System.out.println("\nRequest 2 - by date > 2022-01-01. Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        // Запрос 3
        AccountsRequest request3 = new AccountsRequest();
        request3.getList().add(paramsDto1);
        request3.getList().add(paramsDto2);
        accountList = getAccounts(target, request3);

        System.out.println("\nRequest 3 - by category \"personal\" and date > 2022-01-01. Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        // Запрос 4
        AccountsRequest request4 = new AccountsRequest();
        KeyValueParamsDto paramsDto3 = new KeyValueParamsDto();
        paramsDto3.setKey("label");
        paramsDto3.setValue("Test");
        paramsDto3.setCompareOperation("LIKE");
        paramsDto3.setLogicOperation("OR");
        request4.getList().add(paramsDto3);

        KeyValueParamsDto paramsDto4 = new KeyValueParamsDto();
        paramsDto4.setKey("amount");
        paramsDto4.setValue(BigDecimal.ZERO);
        paramsDto4.setCompareOperation("<");
        paramsDto4.setLogicOperation("OR");
        request4.getList().add(paramsDto4);
        accountList = getAccounts(target, request4);

        System.out.println("\nRequest 4 - by label LIKE \"Test\" or amount < 0. Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        // Запрос 5
        AccountsRequest request5 = new AccountsRequest();
        KeyValueParamsDto paramsDto5 = new KeyValueParamsDto();
        paramsDto5.setKey("code");
        paramsDto5.setValue("47");
        paramsDto5.setCompareOperation("LIKE");
        paramsDto5.setLogicOperation("AND");
        request5.getList().add(paramsDto5);

        KeyValueParamsDto paramsDto6 = new KeyValueParamsDto();
        paramsDto6.setKey("category");
        paramsDto6.setValue("personal");
        paramsDto6.setCompareOperation("=");
        paramsDto6.setLogicOperation("AND");
        request5.getList().add(paramsDto6);

        KeyValueParamsDto paramsDto7 = new KeyValueParamsDto();
        paramsDto7.setKey("amount");
        paramsDto7.setValue(BigDecimal.ZERO);
        paramsDto7.setCompareOperation(">");
        paramsDto7.setLogicOperation("AND");
        request5.getList().add(paramsDto7);

        KeyValueParamsDto paramsDto8 = new KeyValueParamsDto();
        paramsDto8.setKey("open_date");
        paramsDto8.setValue("2020-04-05");
        paramsDto8.setCompareOperation("=");
        paramsDto8.setLogicOperation("AND");
        request5.getList().add(paramsDto8);
        accountList = getAccounts(target, request5);

        System.out.println("\nRequest 5 - by code LIKE \"47\" and category = \"personal\" and amount > 0 and " +
                "date = \"2020-04-05\". Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        System.out.println("------ END GET ACCOUNTS ------ ");

//        // Request 6
//        AccountsRequest request6 = new AccountsRequest();
//        KeyValueParamsDto paramsDto9 = new KeyValueParamsDto();
//        paramsDto9.setKey("code");
//        paramsDto9.setValue(47L);
//        paramsDto9.setCompareOperation("LIKE");
//        paramsDto9.setLogicOperation("AND");
//        request6.getList().add(paramsDto9);
//        System.out.println("\nRequest 6 - by Long code");
//        accountList = getAccounts(accountService, request6);
//
//        // Request 7
//        AccountsRequest request7 = new AccountsRequest();
//        KeyValueParamsDto paramsDto10 = new KeyValueParamsDto();
//        paramsDto10.setKey("code");
//        paramsDto10.setValue(BigInteger.TEN);
//        paramsDto10.setCompareOperation("LIKE");
//        paramsDto10.setLogicOperation("AND");
//        request7.getList().add(paramsDto10);
//        System.out.println("\nRequest 7 - by BigInteger code");
//        accountList = getAccounts(accountService, request7);
//
//        // Request 8
//        AccountsRequest request8 = new AccountsRequest();
//        request8.getList().add(new KeyValueParamsDto());
//        System.out.println("\nRequest 8 - by empty param");
//        accountList = getAccounts(accountService, request8);
    }

    private static List<Account> getAccounts(WebTarget target, AccountsRequest accountsRequest) {
        Response response = null;
        if (accountsRequest == null) {
            response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
        } else {
            response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(accountsRequest, MediaType.APPLICATION_JSON), Response.class);
        }
        return response.readEntity(new GenericType<>() {});
    }

}