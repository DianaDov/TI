package sample;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import static sample.Main.textReader;

public class Playfair {

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

    String key_matrix[][]=new String[5][5];
    String alphabet[]="A,B,C,D,E,F,G,H,I,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",+");

    String key_input="";
    String str_input="";
    String str="";
    String key="";

    String encode_str="";
    String dcode_str="";

    List<String> pairs = new LinkedList<String>();
    List<String> res = new LinkedList<String>();
    String errors="";

    void makeStr() {
        str = "";
        str_input = str_input.toUpperCase();
        str_input=str_input.replaceAll("J","I");
        str_input=str_input.replaceAll("\\s+","");
        str_input=str_input.replaceAll(",+","");
        for (int i = 0; i < str_input.length(); i++) {
            if (Arrays.toString(alphabet).contains(String.valueOf(str_input.charAt(i)))) {
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
        }
    }

    void makeKey(){
        key = "";
        key_input = key_input.toUpperCase();
        key_input=key_input.replaceAll("J","I");
        key_input=key_input.replaceAll("\\s+","");
        key_input=key_input.replaceAll(",+","");
        for (int i = 0; i < key_input.length(); i++) {
            if (Arrays.toString(alphabet).contains(String.valueOf(key_input.charAt(i)))) {
                key += key_input.charAt(i);
            }
        }
        if (key.length() == 0||key.matches("\\s+")) {
            errors = errors.concat("Нет символов для генерации ключа.\n");
            txt_errors.setText(errors);
            errors = "";
            initialize();
        }
        else {
            if(key.length()>alphabet.length){
                key=key.substring(0,alphabet.length);
            }
        }
    }

    void make_str(){
        if (key.length()==0||str.length()==0){
            return;
        }
        str=str.replaceAll("J","I");
        str=str.replaceAll("\\s+,+","");
        System.out.println(str);
        int index_alphabet=0;
        String key_tmp="";
        int index_key=0;
        for (int i=0;i<key.length();i++){
            if (!key_tmp.contains(String.valueOf(key.charAt(i)))){
                key_tmp+=key.charAt(i);
            }
        }
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if (index_key<key_tmp.length()){
                    key_matrix[i][j]= String.valueOf(key_tmp.charAt(index_key));
                    index_key++;
                }
                else {
                    if (!key_tmp.contains(String.valueOf(alphabet[index_alphabet]))) {
                        key_matrix[i][j] = alphabet[index_alphabet];
                    }
                    else {
                        j--;
                    }
                    index_alphabet++;
                }
            }
        }
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                System.out.printf("%-5s",key_matrix[i][j]);
            }
            System.out.println();
        }
        int index=0;
        for (int i=0;index<str.length();i++){
            if (index==str.length()-1){
                pairs.add(String.valueOf(str.charAt(index)).concat("X"));
                if (pairs.get(i).equals("XX")){
                    errors = errors.concat("Нельзя зашифровать \"ХХ\".\n");
                    pairs=new LinkedList<>();
                    txt_errors.setText(errors);
                    errors = "";
                    return;
                }
                else {
                    System.out.print(pairs.get(i)+"  ");
                    break;
                }
            }
            else {
                if (str.charAt(index)==str.charAt(index+1)){
                    pairs.add(String.valueOf(str.charAt(index)).concat("X"));
                    if (pairs.get(i).equals("XX")){
                        errors = errors.concat("Нельзя зашифровать \"ХХ\".\n");
                        pairs=new LinkedList<>();
                        txt_errors.setText(errors);
                        errors = "";
                        return;
                    } else {
                        --index;
                    }
                } else {
                    pairs.add(String.valueOf(str.charAt(index)).concat(String.valueOf(str.charAt(index+1))));
                    if (pairs.get(i).equals("XX")){
                        errors = errors.concat("Нельзя зашифровать \"ХХ\".\n");
                        pairs=new LinkedList<>();
                        txt_errors.setText(errors);
                        errors = "";
                        return;
                    }
                }
            }
            index=index+2;
            System.out.print(pairs.get(i)+"  ");
        }
        System.out.println();
    }

    int[] find_pos(char symbol){
        int []pos=new int[2];
        for (int i=0;i<5;i++){
            for (int j=0;j<5;j++){
                if (key_matrix[i][j].contains(String.valueOf(symbol))){
                    pos[0]=i;
                    pos[1]=j;
                    return pos;
                }
            }
        }
        return null;
    }

    void encode(){
        make_str();
        for (int i=0;i<pairs.size();i++){
            int pos_1[]=find_pos(pairs.get(i).charAt(0));
            int first_i=pos_1[0];
            int first_j=pos_1[1];
            int pos_2[]=find_pos(pairs.get(i).charAt(1));
            int second_i=pos_2[0];
            int second_j=pos_2[1];
            if (first_i!=second_i&&first_j!=second_j){
                res.add(key_matrix[first_i][second_j].concat(key_matrix[second_i][first_j]));
                System.out.print(res.get(i)+"  ");
            }
            else if (first_i==second_i&&first_j==second_j){
                if(first_j==key_matrix.length-1){
                    first_j=-1;
                }
                if(second_j==key_matrix.length-1){
                    second_j=-1;
                }
                res.add(key_matrix[first_i][first_j+1].concat(key_matrix[second_i][second_j+1]));
                System.out.print(res.get(i)+"  ");
            }
            else if (first_i==second_i){
                if(first_j==key_matrix.length-1){
                    first_j=-1;
                }
                if(second_j==key_matrix.length-1){
                    second_j=-1;
                }
                res.add(key_matrix[first_i][first_j+1].concat(key_matrix[second_i][second_j+1]));
                System.out.print(res.get(i)+"  ");
            }
            else {
                if(first_i==key_matrix.length-1){
                    first_i=-1;
                }
                if(second_i==key_matrix.length-1){
                    second_i=-1;
                }
                res.add(key_matrix[first_i+1][first_j].concat(key_matrix[second_i+1][second_j]));
                System.out.print(res.get(i)+"  ");
            }
        }
        for (int i=0;i<res.size();i++){
            encode_str+=res.get(i);
        }
    }

    List<String>decode_str=new LinkedList<>();

    void decode(){
        make_str();
        for (int i=0;i<pairs.size();i++){
            int pos_1[]=find_pos(pairs.get(i).charAt(0));
            int first_i=pos_1[0];
            int first_j=pos_1[1];
            int pos_2[]=find_pos(pairs.get(i).charAt(1));
            int second_i=pos_2[0];
            int second_j=pos_2[1];
            if (first_i!=second_i&&first_j!=second_j){
                decode_str.add(key_matrix[first_i][second_j].concat(key_matrix[second_i][first_j]));
                System.out.print(decode_str.get(i)+"  ");
            }
            else if (first_i==second_i&&first_j==second_j){
                if(first_j==0){
                    first_j=key_matrix.length;
                }
                if(second_j==0){
                    second_j=key_matrix.length;
                }
                decode_str.add(key_matrix[first_i][first_j-1].concat(key_matrix[second_i][second_j-1]));
                System.out.print(decode_str.get(i)+"  ");
            }
            else if (first_i==second_i){
                if(first_j==0){
                    first_j=key_matrix.length;
                }
                if(second_j==0){
                    second_j=key_matrix.length;
                }
                decode_str.add(key_matrix[first_i][first_j-1].concat(key_matrix[second_i][second_j-1]));
                System.out.print(decode_str.get(i)+"  ");
            }
            else {
                if(first_i==0){
                    first_i=key_matrix.length;
                }
                if(second_i==0){
                    second_i=key_matrix.length;
                }
                decode_str.add(key_matrix[first_i-1][first_j].concat(key_matrix[second_i-1][second_j]));
                System.out.print(decode_str.get(i)+"  ");
            }
        }
        for (int i=0;i<decode_str.size();i++){
            dcode_str+=decode_str.get(i);
        }
    }

    @FXML
    void initialize() {
        btn_encode.setOnAction(event -> {
            res=new ArrayList<>();
            pairs=new ArrayList<>();
            encode_str="";
            str_input=txt_phrase.getText();
            key_input=txt_key.getText();
            makeStr();
            makeKey();
            encode();
            txt_res.setText(encode_str);
        });
        btn_decode.setOnAction(event -> {
            decode_str=new ArrayList<>();
            pairs=new ArrayList<>();
            dcode_str="";
            str_input=txt_phrase.getText();
            key_input=txt_key.getText();
            makeStr();
            makeKey();
            decode();
            txt_res.setText(dcode_str);
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
