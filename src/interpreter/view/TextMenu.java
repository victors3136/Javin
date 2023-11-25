package interpreter.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TextMenu {
    private final Map<String, Command> commands;

    public TextMenu() {
        commands = new HashMap<>();
    }

    public void addCommand(String command, Command effect) {
        commands.put(command, effect);
    }

    public void printMenu() {
        System.out.println(commands.
                values().
                stream().
                map(command -> "---------\n"+command.getKey() + " - " + command.getDescription()+"\n").
                collect(Collectors.joining()));
    }
    public void show(){
        Scanner stdin = new Scanner(System.in);
        while(true){
            printMenu();
            System.out.print("Input option: ");
            String key = stdin.nextLine();
            if(key.equals("break the loop")){
                break;
            }
            Command currentCommand = commands.get(key);
            if(currentCommand == null) {
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
