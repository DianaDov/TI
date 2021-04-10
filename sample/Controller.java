package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_decode;

    @FXML
    private Button btn_choose;

    @FXML
    private Text txt_warning;

    @FXML
    private TextArea txt_data;

    @FXML
    private Button btn_key;

    @FXML
    private TextArea txt_key;

    @FXML
    private TextArea txt_register;

    @FXML
    private TextArea txt_decode;

    @FXML
    private TextArea txt_encode;

    @FXML
    private Button btn_encode;

    StringBuffer key = new StringBuffer();
    String inputKey = "";
    StringBuffer data = new StringBuffer();
    StringBuffer encodeData = new StringBuffer();
    StringBuffer decodeData = new StringBuffer();
    String ext = "";
    File file;
    byte[] text_arr;
    byte[] key_arr;
    byte[] encode_arr;

    void getData(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        text_arr=bytes;
        int new_len= Math.min(bytes.length, 1000);
        data=toBinary(Arrays.copyOf(bytes,new_len));
    }

    //считывается ифа в массив байтов и переводится в биты ппереводится только первая 1000 эл-тов
    StringBuffer toBinary(byte[] bytes) {
        StringBuffer str = new StringBuffer();
        for (byte b : bytes) {
             str.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(b < 0 ? (b + 256) : b))));
        }
        System.out.println("Данные преобразованы.");
        return str;
    }

    //генерируетс ключ, в него передаётся длина массива байтов*8=биты
    //для его генерации исп регистр линейного сдвига(работа регистра задаётся с помощью примитивного многочлена)
    void makeKey(String inputKey) {
        int[] array = new int[inputKey.length()];
        System.out.println(array.length);
        int firstSymbol;
        int secondSymbol;
        int thirdSymbol;
        int fourthSymbol;
        for (int i = 0; i < inputKey.length(); i++) {
            array[26 - i] = Integer.parseInt(Character.toString(inputKey.charAt(i)));
        }
        for (int i = 0; i < this.text_arr.length*8; i++) {
            key.append(array[array.length - 1]);
            //на каждом шаге будет происх обр связь
            //в обратной связи будут участвовать ячейки 27, 8 , 7, 1
            firstSymbol = array[26];
            secondSymbol = array[7];
            thirdSymbol = array[6];
            fourthSymbol = array[0];
            for (int j = array.length - 1; j > 0; j--) {
                array[j] = array[j - 1];
            }
            array[0] = firstSymbol ^ secondSymbol ^ thirdSymbol ^ fourthSymbol;
        }
        System.out.println("Ключ сгенерирован.");
    }

    //примитивный многочлен - тот мн кот задаёт работу регистра с максимально возм периодом
    //Последовательность максимально возможного для данного генератора периода называется M-последовательностью.
    //период, равный 2^m – 1 (m - длина регистра)
    //1.  регистр линейного сдвига с обратной связью - нужен для того, чтобы сгенерировать биты ключа

    void readInoutKey() throws IOException {
        txt_warning.setText("");
        String tmpKey=txt_register.getText();
        String readKey="";
        //вводит какой-то ключ сколь угодно длинный, прогоняем S-бокс(256 итер) на каждом шаге в завис
        //от символа ключа выбир 2 эл-та S-бокса и меняются местами
        //потом процесс шифрования - опять перемеш и достаётсяи делается ксор с байтом ключа
        for (int i=0;i<tmpKey.length();i++){
            if (tmpKey.charAt(i)=='1'||tmpKey.charAt(i)=='0'){
                readKey+=tmpKey.charAt(i);
            }
        }
        if (readKey.length()<27){
            txt_warning.setText("Должно быть минимум 27 единиц и нулей!");
        } else {
            inputKey=readKey.substring(0,27);
            txt_warning.setText("Состояние регистра принято!");
        }
    }
    //2.  а геффе мультиплексор и 3 регистра (упр 1\0 + формула выхода xG = (x1 and x2) or (not x1 and x3)
    //3.  RC4 работает на основе подстановочной таблицы - программная реализация (побайтовое шифрование)
    //генерир псевдослуч посл для этого исп S-бокс (РЕАЛ - МАССИВ) сначала заполн в прямом пор 256 эл, польз

    void encode2() throws IOException {
        int limit=500;
        key_arr=toByte(key.toString());
        encode_arr=new byte[key_arr.length];
        if (key_arr.length==text_arr.length){
            for(int i=0;i<key_arr.length;i++){
                //само шифрование - ксорятся байты, а не биты => прога работает быстро
                encode_arr[i]=(byte) (key_arr[i]^text_arr[i]);
            }
            writeDecryptDataToFile(encode_arr,"src/sample/result/encode.txt");
            encodeData= encode_arr.length > limit ? toBinary(Arrays.copyOf(encode_arr,limit)) : toBinary(encode_arr);
            System.out.println("Данные зашифрованы.");
        } else {
            System.out.println("Данные не зашифрованы.");
        }
    }

    void decode2() throws IOException {
        int limit = 500;
        key_arr=toByte(key.toString());
        byte[] answer=new byte[key_arr.length];
        if (key_arr.length==encode_arr.length){
            for(int i=0;i<key_arr.length;i++){
                answer[i]=(byte) (key_arr[i]^encode_arr[i]);
            }
            writeDecryptDataToFile(answer,"src/sample/result/res."+ext);
            decodeData= answer.length > limit ? toBinary(Arrays.copyOf(answer,limit)) : toBinary(answer);
            System.out.println("Данные зашифрованы.");
        } else {
            System.out.println("Данные не зашифрованы.");
        }
    }

    byte[] toByte(String s){
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++) {
            if ((c = s.charAt(i)) == '1') {
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            }
            else if (c != '0') {
                throw new IllegalArgumentException();
            }
        }
        return toReturn;
    }

    void writeDecryptDataToFile(byte[] bytes, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes, 0, bytes.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    String getExtension(String fileName) {
        String ext = "";
        String[] arr = fileName.split("\\.");
        ext = arr[arr.length - 1];
        return ext;
    }

    void chooseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл");
        Stage stage = new Stage();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            getData(file.getPath());
        }
    }

    void clearAll(){
        txt_key.clear();
        txt_decode.clear();
        txt_encode.clear();
        txt_data.clear();
        decodeData=new StringBuffer();
        encodeData=new StringBuffer();
        key=new StringBuffer();
        ext = "";
    }

    @FXML
    void initialize() throws IOException {

        int limit=1000;

        btn_key.setOnAction(event -> {
            try {
                readInoutKey();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btn_choose.setOnAction(event -> {
            if (inputKey.length()==27) {
                txt_warning.setText("");
                clearAll();
                try {
                    chooseFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ext = getExtension(file.getPath());
                try {
                    getData(file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String sub_data = data.length() > limit ? data.substring(0, limit) : data.toString();
                txt_data.setText(sub_data);
                makeKey(inputKey);
                String sub_key = key.length() > limit ? key.substring(0, limit) : key.toString();
                txt_key.setText(sub_key);
            }
            else {
                txt_warning.setText("Подтвердите состояние регистра!");
            }
        });

        btn_encode.setOnAction(event -> {
            try {
                encode2();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sub_encode = encodeData.length() > limit ? encodeData.substring(0, limit) : encodeData.toString();
            txt_encode.setText(sub_encode);
        });

        btn_decode.setOnAction(event -> {
            try {
                decode2();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String sub_decode = decodeData.length() > limit ? decodeData.substring(0, limit) : decodeData.toString();
            txt_decode.setText(sub_decode);
        });

    }
}
