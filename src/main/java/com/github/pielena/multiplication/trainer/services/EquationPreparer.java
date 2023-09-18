package com.github.pielena.multiplication.trainer.services;

import com.github.pielena.multiplication.trainer.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
