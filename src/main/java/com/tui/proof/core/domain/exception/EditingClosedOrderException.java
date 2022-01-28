package com.tui.proof.core.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "editing a closed order")
public class EditingClosedOrderException extends RuntimeException{
}
