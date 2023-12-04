package interpreter.controller;

import interpreter.model.exceptions.*;
import interpreter.model.executionstack.ExecutionStack;
import interpreter.model.heaptable.HeapHashTable;
import interpreter.model.programstate.ProgramState;
import interpreter.model.statements.Statement;
import interpreter.model.values.ReferenceValue;
import interpreter.model.values.Value;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerImplementation implements Controller {
    private final Repository repository;
    private final String input;
    private ExecutorService executor;

    public ControllerImplementation(String input, ProgramState programState, Repository repository) {
        this.input = input;
        this.repository = repository;
        this.repository.add(programState);
    }


    @Override
    public void takeAllSteps() throws RepositoryException {
        ProgramState programState = repository.getProgramList().get(0);
        System.out.println("Execution starting for current loaded program ...\n");
        System.out.println(input);
        repository.logProgramStateExecution(programState);
        try {
            while (!programState.getExecutionStack().empty()) {
                programState.takeOneStep();
                collectGarbage();
                repository.logProgramStateExecution(programState);
            }
        } catch (SymbolTableException | StatementException | ValueException | ExpressionException |
                 RepositoryException | HeapException | ProgramStateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Final Program Output: \n");
        System.out.println(programState.getOutputList().toString());
        programState.getFileTable().cleanup();
        programState.getHeapTable().cleanup();
        repository.logProgramStateExecution(programState);
    }

    @Override
    public Repository getRepository() {
        return repository;
    }

    @Override
    public List<ProgramState> removeCompletedPrograms(List<ProgramState> input) {
        return input.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    private Set<Integer> getReachableAddresses(int id) {
        return Stream.concat(
                /// Reachable addresses from Symbol table
                repository.getProgramList().get(id).getSymbolTable()
                        .getValues()
                        .stream()
                        .filter(x -> x instanceof ReferenceValue)
                        .map(x -> ((ReferenceValue) x).getAddress())
                ,
                /// Reachable addresses from within the heap
                repository.getProgramList().get(id).getHeapTable()
                        .entriesStream()
                        .filter(x -> x.getValue() instanceof ReferenceValue)
                        .map(Map.Entry::getValue)
                        .map(x -> ((ReferenceValue) x).getAddress())
        ).collect(Collectors.toSet());
    }

    private void collectGarbage() {
        HashMap<Integer, Value> newHeap = repository.getProgramList().get(0).getHeapTable()
                .entriesStream()
                .filter(x -> getReachableAddresses(0).contains(x.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value, repeatValue) -> value,
                        HashMap::new));
        repository.getProgramList().get(0).setHeapTable(new HeapHashTable(newHeap));
    }
}
