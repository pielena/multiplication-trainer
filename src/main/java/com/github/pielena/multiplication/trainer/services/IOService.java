package com.github.pielena.multiplication.trainer.services;

public interface IOService {
    void out(String message);
    String readLn(String prompt);
    int readInt(String prompt);
}
