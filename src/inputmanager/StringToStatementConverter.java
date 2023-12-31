package inputmanager;

import interpreter.controller.Controller;

public interface StringToStatementConverter {
    Controller program(String source, int counter);
}
