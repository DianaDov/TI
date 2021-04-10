package sample;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import static sample.Main.textReader;

public class RailFence {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_decode;

    @FXML
    private Button btn_read;

    @FXML
    private Button btn_home;

    @FXML
    private TextArea txt_res;

    @FXML
    private TextArea txt_key;

    @FXML
    private TextArea txt_phrase;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_encode;

    @FXML
    private Text txt_errors;

    String str_input = "";
    String str = "";
    int key = 0;
    String encode_str = "";
    String decode_str = "";
    String errors = "";
    String digits[] = "1,2,3,4,5,6,7,8,9,0".split(",");

    void makeStr() {
        str = "";
        str_input = str_input.toUpperCase();
        for (int i = 0; i < str_input.length(); i++) {
            if (str_input.charAt(i) >= 65 && str_input.charAt(i) <= 90) {
                str += str_input.charAt(i);
            }
        }
        if (str.length() == 0) {
            errors = errors.concat("Нет корректных символов в введённой строке.\n");
            txt_errors.setText(errors);
            errors = "";
            initialize();
        } else {
            txt_errors.setText("");
            if (key > str.length()) {
                key = str.length();
            }
        }
    }

    private void encode() {
        if (key == 0 || str.length() == 0) {
            return;
        }
        char[][] phrase = new char[key][str.length()];
        int period = 2 * (key - 1);
        if (period > 0) {
            int i = 0;
            for (int j = 0; j < str.length(); j++) {
                int ost = j % period;
                i = key - 1 - Math.abs(key - 1 - ost);
                phrase[i][j] = str.charAt(j);
            }
        } else {
            for (int j = 0; j < str.length(); j++) {
                phrase[0][j] = str.charAt(j);
            }
        }
        for (int l = 0; l < key; l++) {
            for (int p = 0; p < str.length(); p++) {
                if (phrase[l][p] >= 65 && phrase[l][p] <= 90) {
                    encode_str += phrase[l][p];
                }
                System.out.print(phrase[l][p] + "  ");
            }
            System.out.println();
        }
        System.out.println(encode_str);
    }

    void decode() {
        if (key == 0 || str.length() == 0) {
            return;
        }
        char[][] phrase = new char[key][str.length()];
        int period = 2 * (key - 1);
        if (period > 0) {
            int i = 0;
            for (int j = 0; j < str.length(); j++) {
                int ost = j % period;
                i = key - 1 - Math.abs(key - 1 - ost);
                phrase[i][j] = '-';
            }
        } else {
            for (int j = 0; j < str.length(); j++) {
                phrase[0][j] = str.charAt(j);
            }
        }
        int index = 0;
        for (int l = 0; l < key; l++) {
            for (int p = 0; p < str.length(); p++) {
                if (phrase[l][p] == '-') {
                    phrase[l][p] = str.charAt(index);
                    index++;
                }
                System.out.print(phrase[l][p] + "  ");
            }
            System.out.println();
        }
        int i = 0;
        if (period > 0) {
            for (int j = 0; j < str.length(); j++) {
                int ost = j % period;
                i = key - 1 - Math.abs(key - 1 - ost);
                decode_str += phrase[i][j];
            }
        } else {
            for (int j = 0; j < str.length(); j++) {
                decode_str += phrase[0][j];
            }
        }
        System.out.println(decode_str);
    }

    void getKey() {
        String key_input = txt_key.getText();
        String key_temp = "";
        for (int i = 0; i < key_input.length(); i++) {
            if (Arrays.toString(digits).contains(String.valueOf(key_input.charAt(i)))) {
                key_temp += key_input.charAt(i);
            }
        }
        if (key_temp.length() == 0) {
            errors = errors.concat("Ключ должен быть числом.\n");
            txt_errors.setText(errors);
            txt_res.setText("");
            errors = "";
            initialize();
        } else {
            key_temp = key_temp.replaceAll("\\s+", "");
            key = Integer.parseInt(key_temp);
            if (key == 0) {
                errors = errors.concat("Ключ должен быть больше нуля.\n");
                txt_errors.setText(errors);
                txt_res.setText("");
                errors = "";
                initialize();
            }

        }
    }

    @FXML
    void initialize() {
        Controller.changeScene(btn_home, "sample");
        btn_encode.setOnAction(event -> {
            encode_str = "";
            str_input = txt_phrase.getText();
            makeStr();
            getKey();
            encode();
            txt_res.setText(encode_str);
        });
        btn_decode.setOnAction(event -> {
            decode_str = "";
            str_input = txt_phrase.getText();
            makeStr();
            getKey();
            decode();
            txt_res.setText(decode_str);
        });
        btn_read.setOnAction(event -> {
            try {
                textReader.chooseSourceFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                txt_phrase.setText(textReader.readSourceFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_save.setOnAction(event -> {
            try {
                textReader.writeToFile(txt_res.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
