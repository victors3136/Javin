import interpreter.view.CoolGraphicMenu;
import interpreter.view.Menu;
import interpreter.view.RequiredGraphicMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Cool gui or required gui? (c/r)");
        Scanner input = new Scanner(System.in);
        Menu userGUI = switch (input.nextLine()) {
            case "c" -> new CoolGraphicMenu();
            case "r" -> new RequiredGraphicMenu();
            default -> {
                System.err.println("Wrong input. Rerun.");
                System.exit(1);
                yield null;
            }
        };
        userGUI.show();
    }
}
