package interpreter.view;

import inputmanager.InputManager;
import inputmanager.StringToStatementConverter;
import inputmanager.parser.ParseException;
import inputmanager.tokenizer.TokenizerException;
import interpreter.controller.Controller;
import interpreter.model.exceptions.TypecheckException;
import interpreter.view.commands.ExitCommand;
import interpreter.view.commands.RunProgramCommand;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static interpreter.view.SourceGenerator.CODE;

@SuppressWarnings("unused")
public class TextMenu implements Menu {
    private static final StringToStatementConverter inputManager = new InputManager();
    private final Map<String, Command> commands;

    public TextMenu() {
        commands = new HashMap<>();
        this.setup();
    }

    public void addCommand(String command, Command effect) {
        commands.put(command, effect);
    }

    public void printMenu() {
        System.out.println(commands.
                values().
                stream().
                map(command -> "---------\n" + command.getKey() + " -\n " + command.getDescription() + "\n").
                collect(Collectors.joining()));
    }

    private void setup() {
        this.addCommand("exit", new ExitCommand("exit", "exit"));
        List<String[]> sources = SourceGenerator.makeList();
        AtomicInteger counter = new AtomicInteger(1);
        for (String[] source : sources) {
            Controller controller;
            try {
                controller = inputManager.program(source[CODE], counter.get());
            } catch (TypecheckException | TokenizerException | ParseException err) {
                System.err.println(err.getMessage());
                continue;
            }
            this.addCommand(String.valueOf(counter.get()), new RunProgramCommand(String.valueOf(counter.get()), source[CODE], controller));
            counter.getAndIncrement();
        }
        this.show();
    }

    public void show() {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Input option: ");
            String key = stdin.nextLine();
            if (key.equals("break the loop")) {
                break;
            }
            Command currentCommand = commands.get(key);
            if (currentCommand == null) {
                System.err.println("Try again");
                continue;
            }
            currentCommand.execute();
            commands.remove(key, currentCommand);
            System.out.print("Press 'enter' to continue ...");
            stdin.nextLine();
        }
    }
}
