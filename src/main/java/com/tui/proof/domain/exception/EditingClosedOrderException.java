package com.tui.proof.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "editing a closed order")
public class EditingClosedOrderException extends Exception{
}
