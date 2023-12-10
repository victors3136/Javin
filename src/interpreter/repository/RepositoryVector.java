package interpreter.repository;

import interpreter.model.programstate.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class RepositoryVector implements Repository{
    List<ProgramState> storage;
    final String logFilePath;
    public RepositoryVector(String logFilePath){
        storage = new Vector<>();
        this.logFilePath = logFilePath;
    }
    @Override
    public void add(ProgramState programState) {
        storage.add(programState);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void logProgramStateExecution(ProgramState toLog) throws RepositoryException {
        try(PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))){
            logger.println(toLog.toString());
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<ProgramState> getProgramList() {
        return storage;
    }

    @Override
    public void setProgramList(List<ProgramState> input) {
            storage = input;
    }
}
