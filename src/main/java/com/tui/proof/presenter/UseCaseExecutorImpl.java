package com.tui.proof.presenter;


import org.springframework.stereotype.Service;

import java.util.function.Function;

import com.tui.proof.core.usecases.UseCase;
import com.tui.proof.core.usecases.UseCaseExecutor;

@Service
public class UseCaseExecutorImpl implements UseCaseExecutor {
    @Override
    public <R, I extends UseCase.InputValues, O extends UseCase.OutputValues> R execute(
            UseCase<I, O> useCase, I input, Function<O, R> outputMapper) {
        return outputMapper.apply(useCase.execute(input));
    }
}
