package com.tui.proof.presenter.handler;

import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler    {

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ExceptionResponse> handleServiceExceptions(ServiceException ex) {
        return new ResponseEntity<>(new ExceptionResponse("internal error"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadPilotesOrderException.class)
    public final ResponseEntity<ExceptionResponse> handleBadPilotesOrderException(BadPilotesOrderException ex) {
        return new ResponseEntity<>(new ExceptionResponse("bad pilotes number"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EditingClosedOrderException.class)
    public final ResponseEntity<ExceptionResponse> handleEditingClosedOrderException(EditingClosedOrderException ex) {
        return new ResponseEntity<>(new ExceptionResponse("editing a closed order"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleItemNotFoundException(ItemNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponse("item not found"), HttpStatus.NOT_FOUND);
    }

}
