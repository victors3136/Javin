package interpreter.controller;

import interpreter.model.heaptable.HeapHashTable;
import interpreter.model.programstate.ProgramState;
import interpreter.model.values.ReferenceValue;
import interpreter.repository.Repository;
import interpreter.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerImplementation implements Controller {
    private final Repository repository;
    private ExecutorService executor;

    public ControllerImplementation(ProgramState programState, Repository repository) {
        this.repository = repository;
        this.repository.add(programState);
    }

    @Override
    public void takeOneStepForAll(List<ProgramState> inputList) {
        inputList.forEach(program -> {
            try {
                repository.logProgramStateExecution(program);
            } catch (RepositoryException e) {
                System.err.println(e.getMessage());
            }
        });
        List<Callable<ProgramState>> callList = inputList
                .stream()
                .map(program -> (Callable<ProgramState>) (program::takeOneStep)) /// How to handle excepts here?
                .toList();
        List<ProgramState> newList;
        try {
            newList = executor
                    .invokeAll(callList)
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException err) {
                            System.err.println(err.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (InterruptedException err) {
            System.err.println(err.getMessage());
            return;
        }
        inputList.addAll(newList);
        inputList.forEach(program -> {
            try {
                repository.logProgramStateExecution(program);
            } catch (RepositoryException e) {
                System.err.println(e.getMessage());
            }
        });
        repository.setProgramList(inputList);
    }

    @Override
    public String takeAllSteps() {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programs = removeCompletedPrograms(repository.getProgramList());
        while (!programs.isEmpty()) {
            collectGarbage();
            takeOneStepForAll(programs);
            programs = removeCompletedPrograms(programs);
        }
        executor.shutdownNow();
        String output = repository.getProgramList().getFirst().getOutputList().toString();
        repository.setProgramList(programs);
        return output;
    }

    @Override
    public List<ProgramState> removeCompletedPrograms(List<ProgramState> input) {
        return input
                .stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCompletedPrograms() {
        repository.setProgramList(removeCompletedPrograms(repository.getProgramList()));
        repository.getProgramList();
    }

    @Override
    public ProgramState getProgram() {
        return repository.getProgramList().getFirst();
    }

    private Set<Integer> getReachableAddresses() {
        return Stream.concat(
                /// Reachable addresses from Symbol table
                repository.getProgramList().stream()
                        .flatMap(program -> program.getSymbolTable().getValues().stream())
                        .filter(value -> value instanceof ReferenceValue)
                        .map(x -> ((ReferenceValue) x).getAddress())
                ,
                /// Reachable addresses from within the heap
                repository.getProgramList()
                        .stream()
                        .flatMap(program -> program.getHeapTable().entriesStream())
                        .collect(Collectors.toSet())
                        .stream()
                        .filter(mapEntry -> mapEntry.getValue() instanceof ReferenceValue)
                        .map(x -> ((ReferenceValue) x.getValue()).getAddress())
        ).collect(Collectors.toSet());
    }

    @Override
    public void collectGarbage() {
        Set<Integer> addresses = this.getReachableAddresses();
        HeapHashTable newHeap = new HeapHashTable(repository
                .getProgramList()
                .stream()
                .flatMap(program -> program.getHeapTable()
                        .entriesStream()
                        .filter(entry -> addresses.contains(entry.getKey())))
                .collect(Collectors
                        .toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (value, repeatValue) -> value,
                                HashMap::new
                        )
                ));
        repository.getProgramList().forEach(program -> program.setHeapTable(newHeap));
    }

    @Override
    public List<ProgramState> getPrograms() {
        return repository.getProgramList();
    }
}
