package ru.kekens.exception;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccountServiceExceptionMapper implements ExceptionMapper<AccountServiceException> {

    @Override
    public Response toResponse(AccountServiceException e) {
        return
                Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

}
