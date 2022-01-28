package com.tui.proof.core.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "bad pilotes number")
public class BadPilotesOrderException extends RuntimeException{
}
