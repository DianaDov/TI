package sample;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import static sample.Main.textReader;

public class Vigenere {

    @FXML
    private Button btn_decode;

    @FXML
    private Button btn_read;

    @FXML
    private Text txt_errors;

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

    String str_input="";
    String key_input="";
    String key_progressive="";
    String str="";
    String key="";
    String encode_res="";
    String decode_res="";

    char letters[] = {'А','Б','В','Г','Д','Е','Ё','Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ц','Ч','Ш','Щ','Ъ','Ы','Ь','Э','Ю','Я'};
    String errors = "";

    void makeStr() {
        str = "";
        str_input = str_input.toUpperCase();
        str_input=str_input.replaceAll("\\s+","");
        for (int i = 0; i < str_input.length(); i++) {
            if (Arrays.toString(letters).contains(String.valueOf(str_input.charAt(i)))) {
                str += str_input.charAt(i);
            }
        }
        if (str.length() == 0) {
            errors = errors.concat("Нет символов для расшифровки в введённой строке.\n");
            txt_errors.setText(errors);
            errors = "";
            initialize();
        } else {
            txt_errors.setText("");
            if (key.length() > str.length()) {
                key = key.substring(0,str.length());
            }
        }
    }

    void makeKey(){
        key = "";
        key_input = key_input.toUpperCase();
        key_input=key_input.replaceAll("\\s+","");
        for (int i = 0; i < key_input.length(); i++) {
            if (Arrays.toString(letters).contains(String.valueOf(key_input.charAt(i)))) {
                key += key_input.charAt(i);
            }
        }
        if (key.length() == 0||key.matches("\\s+")) {
            errors = errors.concat("Нет символов для генерации ключа.\n");
            txt_errors.setText(errors);
            errors = "";
            initialize();
        }
    }

    // составляем прогрессивный ключ
    void make_prkey(){
        if (key.length()==0||str.length()==0){
            return;
        }
        key_progressive="";
        int a=0;
        int shift=0;
        for (int i=0;i<str.length();i++,a++){
            // символу исходного текста ставим в соответствие символ шифротекста
            // (который определяем добавив к номеру символа ключа кол-во позиций на которое он сдвинут
            // относительно алфавита исходного текста
            if (a==(key.length())){
                a=0;
                shift++;
                if (shift==33){
                    shift=0;
                }
            }
            // определяем номер символа ключа
            int index=find_index(key.charAt(a));
            int j = 0;
            if (letters.length-index>shift){
                        j=index+shift;
            }
            else {// при повторении ключа, сдвигаем символы ключа на 1 позицию...
                int tmp=0;
                j=index;
                while (j!=letters.length-1){
                    /*Идея использования прогрессивного ключа заключается
                в циклическом сдвиге символов ключа на одну позицию
                в упорядоченном алфавите символов при повторном применении ключа.
                Тогда для ключа SIAMESE при повторном его использовании по прогрессивной
                схеме имеем TJBNFTF,*/
                    j++;
                    tmp++;
                    System.out.println(tmp);
                }
                j=shift-tmp;
            }
            key_progressive+=letters[j];
        }
        System.out.println(key_progressive);
    }

    // буквы исходного алфавита нумеруем числами от 0 до н - находим номер
    int find_index(char c){
        int index=0;
        for (int i=0;i<letters.length;i++){
            if (letters[i]==c){
                return i;
            }
        }
        return '*';
    }

    //кодируем текст
    void encode(){
        make_prkey();
        for (int i=0;i<str.length();i++){
            int i_str=0;
            int i_key=0;
            // символытекста и ключа "заменяем" их порядковым номером
            for (int k=0;k<letters.length;k++){
                if (str.charAt(i)==letters[k]){
                    i_str=k;
                }
                if (key_progressive.charAt(i)==letters[k]){
                    i_key=k;
                }
            }
            int index=(i_str +i_key)%33;
            System.out.println("Ci = (Mi + Ki) mod N = ("+letters[i_str]+" + "+letters[i_key]+") mod 33 = "+letters[index]);
            encode_res+=letters[index];
        }
        System.out.println(encode_res);
    }

    void decode(){
        make_prkey();
        for (int i=0;i<str.length();i++){
            int i_ciph=0;
            int i_key=0;
            for (int k=0;k<letters.length;k++){
                if (str.charAt(i)==letters[k]){
                    i_ciph=k;
                }
                if (key_progressive.charAt(i)==letters[k]){
                    i_key=k;
                }
            }
            // разность по модулю
            int index=(i_ciph+33-i_key)%33;
            System.out.println("Ci = (Mi + Ki) mod N = ("+letters[i_ciph]+" + "+letters[i_key]+") mod 33 = "+letters[index]);
            decode_res+=letters[index];
        }
        System.out.println(decode_res);
    }

    @FXML
    void initialize() {
        btn_encode.setOnAction(event -> {
            encode_res="";
            str_input=txt_phrase.getText();
            key_input=txt_key.getText();
            makeStr();
            makeKey();
            encode();
            txt_res.setText(encode_res);
        });
        btn_decode.setOnAction(event -> {
            decode_res="";
            str_input=txt_phrase.getText();
            key_input=txt_key.getText();
            makeStr();
            makeKey();
            decode();
            txt_res.setText(decode_res);
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
        Controller.changeScene(btn_home,"sample");
    }
}
