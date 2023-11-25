package interpreter.controller;

import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

public interface Controller {
    void setCurrentProgramInRepo(int index) throws RepositoryException;
    ProgramState takeOneStep(ProgramState programState) throws ControllerException, SymbolTableException, StatementException, ValueException, ExpressionException, TypeException, HeapException, ProgramStateException;
    void takeAllSteps() throws SymbolTableException, ControllerException, StatementException, ValueException, ExpressionException, RepositoryException, TypeException, HeapException, ProgramStateException;
    Repository getRepository();
}
