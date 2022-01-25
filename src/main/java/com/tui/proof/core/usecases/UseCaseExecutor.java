package com.tui.proof.core.usecases;

import java.util.function.Function;

public interface UseCaseExecutor {
    
    <R, I extends UseCase.InputValues, O extends UseCase.OutputValues> R execute(
            UseCase<I, O> useCase,
            I input,
            Function<O, R> outputMapper);
}
