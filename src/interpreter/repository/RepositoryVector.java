package interpreter.repository;

import interpreter.model.programstate.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class RepositoryVector implements Repository{
    final List<ProgramState> storage;
    int current;
    final String logFilePath;
    public RepositoryVector(String logFilePath){
        storage = new Vector<>();
        current = 0;
        this.logFilePath = logFilePath;
    }
    public RepositoryVector(ProgramState ps, String logFilePath) {
        storage = new Vector<>();
        current = 0;
        this.logFilePath = logFilePath;
        this.storage.add(ps);
    }

    @Override
    public void setCurrentProgram(int position) throws RepositoryException {
        if(position >= storage.size())
            throw new RepositoryException("Position does not represent a valid index within the interpreter.repository");
        this.current = position;
    }

    @Override
    public ProgramState getCurrentProgram() {
        return this.storage.get(this.current);
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
    public void logProgramStateExecution() throws RepositoryException {
        try(PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))){
            logger.println(getCurrentProgram());
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
