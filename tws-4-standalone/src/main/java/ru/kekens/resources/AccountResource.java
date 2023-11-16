package ru.kekens.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import ru.kekens.dto.AccountsRequest;
import ru.kekens.model.Account;
import ru.kekens.dao.AccountDAO;
import ru.kekens.dto.KeyValueParamsDto;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.List;

import static ru.kekens.utils.Constants.*;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountDAO dao;

    @Path("/{id}")
    @GET
    public Account getAccountById(Long id) {
        return getAccountDAO().getAccountById(id);
    }

    @GET
    public List<Account> getAllAccounts() {
        return getAccountDAO().getAccounts();
    }

    @POST
    public List<Account> getAccounts(AccountsRequest accountsRequest) {
        // Проверяем параметры
        List<KeyValueParamsDto> params = accountsRequest.getList();
        return getAccountDAO().getAccountsByParams(params);
    }

    private AccountDAO getAccountDAO() {
        if (dao == null) {
            dao = new AccountDAO();
        }
        return dao;
    }

    /**
     * Проверка объекта параметра для запроса
     * @param keyValueParamsDto объект параметра для запроса
     * @return признак да/нет (валидный/невалидный)
     */
    private ValidationResult checkValueParams(KeyValueParamsDto keyValueParamsDto) {
        return checkValueParams(keyValueParamsDto, false);
    }

    /**
     * Проверка объекта параметра для запроса
     * @param keyValueParamsDto объект параметра для запроса
     * @param checkOperation признак проверки операции
     * @return признак да/нет (валидный/невалидный)
     */
    private ValidationResult checkValueParams(KeyValueParamsDto keyValueParamsDto, boolean checkOperation) {
        String key = keyValueParamsDto.getKey();
        // Проверям поле
        if (key == null || key.isEmpty()) {
            return new ValidationResult(false, "Ключ параметра пустой");
        }
        if (!FIELD_ORDER.contains(key) && !key.equals("id")) {
            return new ValidationResult(false, "У сущности Account не существует поля \" + key");
        }
        // Проверяем тип данных
        Object value = keyValueParamsDto.getValue();
        if (!(value instanceof String) && !(value instanceof Long)
                && !(value instanceof Integer) && !(value instanceof BigDecimal)
                && !(value instanceof XMLGregorianCalendar))
        {
            return new ValidationResult(false,
                    "Не поддерживается тип данных параметра " + value.getClass().getName());
        }
        // Проверяем валидность параметра
        ValidationResult result = checkValue(key, value);
        if (!result.getSuccess()) {
            return result;
        }

        // Проверяем операции
        if (checkOperation) {
            // Проверка операции сравнения
            String compareOperation = keyValueParamsDto.getCompareOperation();
            if (StringUtils.isEmpty(compareOperation)) {
                compareOperation = DEFAULT_COMPARE_OPERATION;
            }

            if (!COMPARE_OPERATIONS_SET.contains(compareOperation.toUpperCase())) {
                return new ValidationResult(false, "Не поддерживается операция сравнения \""
                        + compareOperation + "\". Параметр - " + key);
            }

            if (compareOperation.toUpperCase().equals("LIKE") && !(keyValueParamsDto.getValue() instanceof String)) {
                return new ValidationResult(false, "Не поддерживается операция сравнения \"LIKE\" " +
                        "для нестроковых значений. Параметр - " + key);
            }

            // Проверка логической операции
            String logicOperation = keyValueParamsDto.getLogicOperation();
            if (StringUtils.isEmpty(logicOperation)) {
                logicOperation = DEFAULT_LOGIC_OPERATION;
            }

            if (!LOGIC_OPERATIONS_SET.contains(logicOperation.toUpperCase())) {
                return new ValidationResult(false, "Не поддерживается логическая операция \""
                        + logicOperation + "\". Параметр - " + key);
            }
        }

        return new ValidationResult();
    }

    private ValidationResult checkValue(String key, Object value) {
        boolean correct = true;
        switch (key) {
            case "label":
                correct = value instanceof String;
                break;
            case "code":
                correct = value instanceof String;
                break;
            case "category":
                correct = value instanceof String;
                break;
            case "amount":
                correct = value instanceof BigDecimal;
                break;
            case "open_date":
                correct = value instanceof XMLGregorianCalendar;
                break;
        }

        if (!correct) {
            return new ValidationResult(false, "Тип данных " + value.getClass().getName() +
                    " параметра " + key + " не соответствует требуемому");
        }
        return new ValidationResult();
    }

    /**
     * Внутренний класс для возврата результата проверок
     */
    private static class ValidationResult {
        private Boolean success;
        private String errorMessage;

        public ValidationResult() {
            this.success = true;
        }

        public ValidationResult(Boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }

}
