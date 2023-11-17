package interpreter.repository;

import interpreter.model.programstate.ProgramState;

public interface Repository {
    void setCurrentProgram(int position) throws RepositoryException;
    ProgramState getCurrentProgram();
    void add(ProgramState programState);
    int size();
    void logProgramStateExecution()throws RepositoryException;
}
