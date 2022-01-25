package com.tui.proof.core.usecases;

public interface UseCase<I extends UseCase.InputValues, O extends UseCase.OutputValues> {
    public O execute(I input);

    public interface InputValues {
    }

    public interface OutputValues {
    }
}
