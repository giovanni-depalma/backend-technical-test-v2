package com.tui.proof.presenter.handler;

import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<Object> handleServiceExceptions(ServiceException ex, WebRequest request) {
        return new ResponseEntity(new ExceptionResponse("internal error"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadPilotesOrderException.class)
    public final ResponseEntity<Object> handleBadPilotesOrderException(BadPilotesOrderException ex, WebRequest request) {
        return new ResponseEntity(new ExceptionResponse("bad pilotes number"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EditingClosedOrderException.class)
    public final ResponseEntity<Object> handleEditingClosedOrderException(EditingClosedOrderException ex, WebRequest request) {
        return new ResponseEntity(new ExceptionResponse("editing a closed order"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        return new ResponseEntity(new ExceptionResponse("item not found"), HttpStatus.NOT_FOUND);
    }

}
