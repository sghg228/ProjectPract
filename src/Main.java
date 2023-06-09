import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    static double  valDefault = 60;
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\User\\DesKtop\\test.txt");
        FileInputStream inputStream = new FileInputStream(file);
        String strVal = "";
        int iter;
        while ((iter = inputStream.read()) != -1){
            strVal += (char)iter;
        }
        Double val = Double.parseDouble(strVal);
        Scanner scan = new Scanner(System.in);
        String curCom = scan.nextLine();
        System.out.println(doCommand(curCom, val));
    }

    public static String toDollars(String string, Double val){
        if (Pattern.matches("[0-9]+.[0-9]{2}[p]",string) == true){
            Double number = Double.parseDouble(string.substring(0, string.indexOf('p')));
            if(number != 0){
                number /= val;
                String result = new DecimalFormat("#0.00").format(number);
                return "$" + result;
            }
            else {
                return "errorQ";
            }
        }
        else {
            return "errorQ";
        }
    }

    public static String toRubles(String string, Double val){
        if (Pattern.matches("[$][0-9]+.[0-9]{2}", string) == true) {
            Double number = Double.parseDouble(string.substring(1));
            number *= val;
            String result = new DecimalFormat("#0.00").format(number);
            return number + "p";
        }
        else {
            return "errorQ";
        }
    }

    public static String doCommand(String string, Double val){
        if (!(Pattern.matches("(toDollars|toRubles)[(][(toDollars|toRubles)+.0-9$p ]+[)]", string)) == true ){
            //divArg[i] = toRubles(divArg[i].substring(divArg[i].indexOf('(')+1, divArg[i].lastIndexOf(')')));
            return "errorQ";
        }
        String command = string.substring(0,string.indexOf('('));
        String arg = string.substring(string.indexOf('(')+1, string.lastIndexOf(')'));
        String[] divArg = arg.split(" ");
        String result = "";
        switch (command){
            case "toDollars":
                if (divArg.length == 1){
                    return toDollars(divArg[0],val);
                }
                if (divArg.length %2 == 0){
                    return "errorQ";
                }
                else {

                    for (int i = 0; i < divArg.length; i++){
                        if (Pattern.matches("(toDollars|toRubles)[(][(toDollars|toRubles)+.0-9$p ]+[)]", divArg[i]) == true ){
                            divArg[i] = doCommand(divArg[i], val);
                        }
                    }
                    result += divArg[0];
                    for (int i = 2; i < divArg.length; i += 2){
                        if (divArg[i-1].equals("+") == true){
                            result = sum(result, divArg[i]);
                        }
                        else {
                            result = razn(result, divArg[i]);
                        }
                    }
                    return toDollars(result,val);
                }
                //break;
            case  "toRubles":
                if (divArg.length == 1){
                    return toRubles(divArg[0],val);
                }
                if (divArg.length %2 == 0){
                    return "errorQ";
                }
                else {

                    for (int i = 0; i < divArg.length; i++){
                        if (Pattern.matches("(toDollars|toRubles)[(][(toDollars|toRubles)+.0-9$p ]+[)]", divArg[i]) == true ){
                            divArg[i] = doCommand(divArg[i],val);
                        }
                    }
                    result += divArg[0];
                    for (int i = 2; i < divArg.length; i += 2){
                        if (divArg[i-1].equals("+") == true){
                            result = sum(result, divArg[i]);
                        }
                        else {
                            result = razn(result, divArg[i]);
                        }
                    }
                    return toRubles(result,val);
                }

            default:
                return "errorQ";


        }
    }

    public static String sum(String str1, String str2){
        if (str1.charAt(0) == '$' && str2.charAt(0) == '$' ){
            Double d1 = Double.parseDouble(str1.substring(1));
            Double d2 = Double.parseDouble(str2.substring(1));
            Double result = d1+d2;
            String strResult = new DecimalFormat("#0.00").format(result);
            return "$"+strResult.replace(',','.');
        }
        if (str1.charAt(str1.length()-1) == 'p' && str2.charAt(str2.length()-1) == 'p'){
            Double d1 = Double.parseDouble((str1.substring(0,str1.indexOf("p"))));
            Double d2 = Double.parseDouble((str2.substring(0,str2.indexOf("p"))));
            Double result = d1 + d2;
            String strResult = new DecimalFormat("#0.00").format(result);
            return strResult.replace(',','.')+"p";
        }
        return "errorQ";
    }

    public static String razn(String str1, String str2){
        if (str1.charAt(0) == '$' && str2.charAt(0) == '$' ){
            Double d1 = Double.parseDouble(str1.substring(1));
            Double d2 = Double.parseDouble(str2.substring(1));
            Double result = d1-d2;
            String strResult = new DecimalFormat("#0.00").format(result);
            return "$"+strResult.replace(',','.');
        }
        if (str1.charAt(str1.length()-1) == 'p' && str2.charAt(str2.length()-1) == 'p'){
            Double d1 = Double.parseDouble((str1.substring(0,str1.indexOf("p"))));
            Double d2 = Double.parseDouble((str2.substring(0,str2.indexOf("p"))));
            Double result = d1 - d2;
            String strResult = new DecimalFormat("#0.00").format(result);
            return strResult.replace(',','.')+"p";
        }
        return "errorQ";
    }

}

