package interpreter.model.filetable;

import interpreter.model.values.StringValue;

import java.io.BufferedReader;

public interface FileTable {
    void add(StringValue name, BufferedReader fileDescriptor);

    BufferedReader lookup(String value);

    void remove(String fileIdentifier);
}
