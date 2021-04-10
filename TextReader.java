package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sample.Controller.openScene;

class TextReader {
    private File sourceFile = null;
    private final File resultFile = new File("src/sample/result.txt");

    void chooseSourceFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        Stage stage = new Stage();
        sourceFile = fileChooser.showOpenDialog(stage);
    }

    String readSourceFile() throws IOException {
        String result = "";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            result=result.concat(temp);
        }
        return result;
    }

    void writeToFile(String resultText) throws IOException {
        clearResultFile();
        FileWriter fileWriter = new FileWriter(resultFile, true);
        fileWriter.write(resultText);
        fileWriter.flush();
    }

    void clearResultFile() throws IOException {
        FileWriter fileWriter = new FileWriter(resultFile);
        fileWriter.write("");
        fileWriter.flush();
    }
}
