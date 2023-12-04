package interpreter.controller;

import interpreter.model.programstate.ProgramState;
import interpreter.model.exceptions.*;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

import java.util.List;

public interface Controller {
//    void setCurrentProgramInRepo(int index) throws RepositoryException;
//    ProgramState takeOneStep(ProgramState programState) throws ControllerException, SymbolTableException, StatementException, ValueException, ExpressionException, HeapException;
    void takeAllSteps() throws RepositoryException;
    Repository getRepository();
    List<ProgramState> removeCompletedPrograms(List<ProgramState> input);
}
