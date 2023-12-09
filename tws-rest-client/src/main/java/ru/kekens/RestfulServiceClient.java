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
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RestfulServiceClient {

    private static final String URI = "http://localhost:8080/tws-rest/api/accounts";

    public static void main(String[] args) throws ParseException {

        // Создаем клиента
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI);

        // Запросы по поиску счетов
        getAccounts(target);

        // Запросы по вставке счетов
        System.out.println("\n------ START INSERT ACCOUNTS ------ ");

        // Запрос 1
        AccountsRequest requestIns1 = new AccountsRequest();
        KeyValueParamsDto paramsInsDto1 = new KeyValueParamsDto();
        paramsInsDto1.setKey("label");
        paramsInsDto1.setValue("New account 1");
        KeyValueParamsDto paramsInsDto2 = new KeyValueParamsDto();
        paramsInsDto2.setKey("code");
        paramsInsDto2.setValue("30303");
        KeyValueParamsDto paramsInsDto3 = new KeyValueParamsDto();
        paramsInsDto3.setKey("category");
        paramsInsDto3.setValue("derivative");
        KeyValueParamsDto paramsInsDto4 = new KeyValueParamsDto();
        paramsInsDto4.setKey("amount");
        paramsInsDto4.setValue(BigDecimal.TEN);
        KeyValueParamsDto paramsInsDto5 = new KeyValueParamsDto();
        paramsInsDto5.setKey("open_date");
        paramsInsDto5.setValue("2021-04-04");
        requestIns1.getList().addAll(List.of(paramsInsDto1, paramsInsDto2, paramsInsDto3, paramsInsDto4, paramsInsDto5));
        Long id = insertAccount(target, requestIns1);

        System.out.println("\nЗапрос 1 - INSERT (New account 1, 30303, derivative, 10.00, 2021-04-04)\n" +
                "New id: " + id);

        // Запрос 2
        AccountsRequest requestIns2 = new AccountsRequest();
        KeyValueParamsDto paramsInsDto6 = new KeyValueParamsDto();
        paramsInsDto6.setKey("label");
        paramsInsDto6.setValue("New account 2");
        KeyValueParamsDto paramsInsDto7 = new KeyValueParamsDto();
        paramsInsDto7.setKey("code");
        paramsInsDto7.setValue("63696");
        KeyValueParamsDto paramsInsDto8 = new KeyValueParamsDto();
        paramsInsDto8.setKey("category");
        paramsInsDto8.setValue("fictious");
        KeyValueParamsDto paramsInsDto9 = new KeyValueParamsDto();
        paramsInsDto9.setKey("amount");
        paramsInsDto9.setValue(new BigDecimal("111.999"));
        KeyValueParamsDto paramsInsDto10 = new KeyValueParamsDto();
        paramsInsDto10.setKey("open_date");
        paramsInsDto10.setValue("2023-09-01");
        requestIns2.getList().addAll(List.of(paramsInsDto6, paramsInsDto7, paramsInsDto8, paramsInsDto9, paramsInsDto10));
        Long id2 = insertAccount(target, requestIns2);

        System.out.println("\nЗапрос 2 - INSERT (New account 2, 63696, fictious, 111.999, 2023-09-01)\n" +
                "New id: " + id2);

        // Запрос 3
        System.out.println("\nRequest 3 - INSERT not all params\n");
        AccountsRequest requestIns3 = new AccountsRequest();
        KeyValueParamsDto paramsInsDto11 = new KeyValueParamsDto();
        paramsInsDto11.setKey("label");
        paramsInsDto11.setValue("New account 3");
        requestIns3.getList().add(paramsInsDto11);
        Long id3 = insertAccount(target, requestIns3);


        // Запрос 4
        System.out.println("\nЗапрос 4 - INSERT wrong param amount5\n");
        AccountsRequest requestIns4 = new AccountsRequest();
        KeyValueParamsDto paramsInsDto12 = new KeyValueParamsDto();
        paramsInsDto12.setKey("label");
        paramsInsDto12.setValue("New account 2");
        KeyValueParamsDto paramsInsDto13 = new KeyValueParamsDto();
        paramsInsDto13.setKey("code");
        paramsInsDto13.setValue("63696");
        KeyValueParamsDto paramsInsDto14 = new KeyValueParamsDto();
        paramsInsDto14.setKey("category");
        paramsInsDto14.setValue("fictious");
        KeyValueParamsDto paramsInsDto15 = new KeyValueParamsDto();
        paramsInsDto15.setKey("amount");
        paramsInsDto15.setValue("2023-09-01");
        KeyValueParamsDto paramsInsDto16 = new KeyValueParamsDto();
        paramsInsDto16.setKey("open_date");
        paramsInsDto16.setValue("2023-09-01");
        requestIns4.getList().addAll(List.of(paramsInsDto12, paramsInsDto13, paramsInsDto14, paramsInsDto15, paramsInsDto16));
        Long id4 = insertAccount(target, requestIns4);

        // Все счета
        List<Account> accountList = getAccounts(target, null);
        System.out.println("\nAll accounts");
        for (Account account : accountList) {
            printAccountInfo(account);
        }
        System.out.println("------ END INSERT ACCOUNTS ------ ");

        // Запросы по обновлению
        System.out.println("\n------ START UPDATE ACCOUNTS ------ ");
        System.out.println("Запрос 1 - UPDATE account SET category=personal, amount=-1000, label = New account 1 after update\n" +
                "WHERE id = " + id);
        // Запрос 1 - Обновить новый счет 1
        AccountsRequest requestUpd1 = new AccountsRequest();
        KeyValueParamsDto paramsUpdDto1 = new KeyValueParamsDto();
        paramsUpdDto1.setKey("category");
        paramsUpdDto1.setValue("personal");
        KeyValueParamsDto paramsUpdDto2 = new KeyValueParamsDto();
        paramsUpdDto2.setKey("amount");
        paramsUpdDto2.setValue(new BigDecimal("-1000"));
        KeyValueParamsDto paramsUpdDto3 = new KeyValueParamsDto();
        paramsUpdDto3.setKey("label");
        paramsUpdDto3.setValue("New account 1 after update");
        requestUpd1.getList().addAll(List.of(paramsUpdDto1, paramsUpdDto2,paramsUpdDto3));
        updateAccount(target, id, requestUpd1);

        // Запрос 2 - Обновить новый счет 2
        System.out.println("Запрос 2 - UPDATE account SET category=personal, amount=-3000, open_date = 2014-09-01\n" +
                "WHERE id = " + id2 + "\n");
        AccountsRequest requestUpd2 = new AccountsRequest();
        KeyValueParamsDto paramsUpdDto4 = new KeyValueParamsDto();
        paramsUpdDto4.setKey("category");
        paramsUpdDto4.setValue("personal");
        KeyValueParamsDto paramsUpdDto5 = new KeyValueParamsDto();
        paramsUpdDto5.setKey("amount");
        paramsUpdDto5.setValue(new BigDecimal("-3000"));
        KeyValueParamsDto paramsUpdDto6 = new KeyValueParamsDto();
        paramsUpdDto6.setKey("open_date");
        paramsUpdDto6.setValue("2014-09-01");
        requestUpd2.getList().addAll(List.of(paramsUpdDto4, paramsUpdDto5, paramsUpdDto6));
        updateAccount(target, id2, requestUpd2);

        // Получить счета 1 и 2
        AccountsRequest requestGetNewAccounts = new AccountsRequest();
        KeyValueParamsDto paramsDtoNew1 = new KeyValueParamsDto();
        paramsDtoNew1.setKey("id");
        paramsDtoNew1.setValue(id);
        paramsDtoNew1.setCompareOperation("=");
        paramsDtoNew1.setLogicOperation("OR");

        KeyValueParamsDto paramsDtoNew2 = new KeyValueParamsDto();
        paramsDtoNew2.setKey("id");
        paramsDtoNew2.setValue(id2);
        paramsDtoNew2.setCompareOperation("=");
        paramsDtoNew2.setLogicOperation("OR");
        requestGetNewAccounts.getList().addAll(List.of(paramsDtoNew1, paramsDtoNew2));
        accountList = getAccounts(target, requestGetNewAccounts);
        for (Account account : accountList) {
            printAccountInfo(account);
        }

        // Запрос 3 - Update account null
        System.out.println("\nЗапрос 3 - Update account empty params");
        AccountsRequest requestUpd3 = new AccountsRequest();
        updateAccount(target, id2, new AccountsRequest());

        // Запрос 5 - Update not existed account
        System.out.println("Запрос 4 - Update account empty");
        updateAccount(target, 100100L, new AccountsRequest());
        System.out.println("------ END UPDATE ACCOUNTS ------ ");
        
        System.out.println("------ END UPDATE ACCOUNTS ------ ");

        // Запросы по удалению счетов
        System.out.println("\n------ START DELETE ACCOUNTS ------ ");
        System.out.println("Запрос 1 - DELETE FROM account " +
                "WHERE id = " + id);
        // Запрос 1 - Удалить новый счет 1
        deleteAccount(target, id);

        // Запрос 2 - Удалить новый счет 2
        System.out.println("Запрос 2 - DELETE FROM account " +
                "WHERE id = " + id2);
        deleteAccount(target, id2);

        // Запрос 3 - Delete account by null
        System.out.println("Request 3 - DELETE FROM account " +
                "WHERE id = null");
        deleteAccount(target, null);

        // Запрос 4 - Delete account with negative id
        System.out.println("Request 4 - DELETE FROM account " +
                "WHERE id = -1");
        deleteAccount(target, -1L);

        // Все счета
        accountList = getAccounts(target, null);
        System.out.println("\nAll accounts");
        for (Account account : accountList) {
            printAccountInfo(account);
        }

        // Запрос 3 - Удалить все счета
//        System.out.println("\nЗапрос 3 - DELETE FROM account");
//        deleteAccounts(target);

        accountList = getAccounts(target, null);
        System.out.println("\nAll accounts");
        for (Account account : accountList) {
            printAccountInfo(account);
        }
        System.out.println("------ END DELETE ACCOUNTS ------ ");
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
        System.out.println("Запрос 0 - All");
        List<Account> accountList = getAccounts(target, null);
        printAccountInfo(accountList);

        // Запрос 1
        AccountsRequest request1 = new AccountsRequest();
        KeyValueParamsDto paramsDto1 = new KeyValueParamsDto();
        paramsDto1.setKey("category");
        paramsDto1.setValue("personal");
        paramsDto1.setCompareOperation("=");
        paramsDto1.setLogicOperation("AND");
        request1.getList().add(paramsDto1);
        accountList = getAccounts(target, request1);

        System.out.println("\nЗапрос 1 - by category \"personal\". Found " + accountList.size() + " accounts");
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

        System.out.println("\nЗапрос 2 - by date > 2022-01-01. Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        // Запрос 3
        AccountsRequest request3 = new AccountsRequest();
        request3.getList().add(paramsDto1);
        request3.getList().add(paramsDto2);
        accountList = getAccounts(target, request3);

        System.out.println("\nЗапрос 3 - by category \"personal\" and date > 2022-01-01. Found " + accountList.size() + " accounts");
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

        System.out.println("\nЗапрос 4 - by label LIKE \"Test\" or amount < 0. Found " + accountList.size() + " accounts");
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

        System.out.println("\nЗапрос 5 - by code LIKE \"47\" and category = \"personal\" and amount > 0 and " +
                "date = \"2020-04-05\". Found " + accountList.size() + " accounts");
        printAccountInfo(accountList);

        System.out.println("------ END GET ACCOUNTS ------ ");

        // Запрос 6
        AccountsRequest request6 = new AccountsRequest();
        KeyValueParamsDto paramsDto9 = new KeyValueParamsDto();
        paramsDto9.setKey("code");
        paramsDto9.setValue(47L);
        paramsDto9.setCompareOperation("LIKE");
        paramsDto9.setLogicOperation("AND");
        request6.getList().add(paramsDto9);
        System.out.println("\nЗапрос 6 - by Long code");
        accountList = getAccounts(target, request6);

        // Запрос 7
        AccountsRequest request7 = new AccountsRequest();
        KeyValueParamsDto paramsDto10 = new KeyValueParamsDto();
        paramsDto10.setKey("code");
        paramsDto10.setValue(BigInteger.TEN);
        paramsDto10.setCompareOperation("LIKE");
        paramsDto10.setLogicOperation("AND");
        request7.getList().add(paramsDto10);
        System.out.println("\nЗапрос 7 - by BigInteger code");
        accountList = getAccounts(target, request7);

        // Запрос 8
        AccountsRequest request8 = new AccountsRequest();
        request8.getList().add(new KeyValueParamsDto());
        System.out.println("\nЗапрос 8 - by empty param");
        accountList = getAccounts(target, request8);
    }

    private static List<Account> getAccounts(WebTarget target, AccountsRequest accountsRequest) {
        Response response = null;
        if (accountsRequest == null) {
            response = target.request(MediaType.APPLICATION_JSON).get(Response.class);
        } else {
            response = target.path("filter").request(MediaType.APPLICATION_JSON).post(Entity.entity(accountsRequest, MediaType.APPLICATION_JSON), Response.class);
        }

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println(response.readEntity(String.class));
        } else {
            return response.readEntity(new GenericType<>() {});
        }

        return null;
    }

    private static Long insertAccount(WebTarget target, AccountsRequest accountsRequest) {
        Response response = null;
        response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(accountsRequest, MediaType.APPLICATION_JSON), Response.class);

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println(response.readEntity(String.class));
        } else {
            return response.readEntity(Long.class);
        }

        return null;
    }

    private static void updateAccount(WebTarget target, Long id, AccountsRequest accountsRequest) {
        Response response = null;
        response = target.path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).put(Entity.entity(accountsRequest, MediaType.APPLICATION_JSON), Response.class);

        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            System.out.println(response.readEntity(String.class));
        }
    }

    private static void deleteAccount(WebTarget target, Long id) {
        Response response = null;
        target.path(String.valueOf(id)).request(MediaType.APPLICATION_JSON).delete(Response.class);
    }

    private static void deleteAccount(WebTarget target) {
        Response response = null;
        target.path("all").request(MediaType.APPLICATION_JSON).delete(Response.class);
    }

}