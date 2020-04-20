import java.util.ArrayList;
import java.util.Arrays;

public class SolvePassword {

    private int length;
    private String baseUrl;
    private String username;
    final private   ArrayList<Character> smallLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('e','t','a','o','i','n','s','r','h','l','d','c','u','m','f'
                        ,'p','g','w','y','b','v','k','x','j','q','z'));
    final private   ArrayList<Character> capitalLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('E','T','A','O','I','N','S','R','H','L','D','C','U','M','F'
                    ,'P','G','W','Y','B','V','K','X','J','Q','Z'));

    public SolvePassword(int length, String baseUrl, String username) {
        this.length = length;
        this.baseUrl = baseUrl;
        this.username = username;
    }





    public String solvePassword(){
        StringBuilder password = createFirstPassword();
        for (int i=0;i<length;i++){


        }
        return "";
    }

    private StringBuilder createFirstPassword() {
        StringBuilder password=new StringBuilder("");
        for(int i=0;i<length;i++){
            password.setCharAt(i,'e');
        }
        return password;
    }

    private String measureTimeForGivanChars(StringBuilder password,int charPosition){
        for(int j=0;j<smallLetterByFrequency.size();j++){
            password.setCharAt(charPosition,smallLetterByFrequency.get(j));
            String url = createUrl(this.baseUrl, this.username, password.toString(), 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURL();
        }
        return "";
    }

    private String measureTimeForUpperCase(StringBuilder password,int charPosition){
        for(int j=0;j<capitalLetterByFrequency.size();j++){
            password.setCharAt(charPosition,capitalLetterByFrequency.get(j));
            String url = createUrl(this.baseUrl, this.username, password.toString(), 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURL();
        }
        return "";
    }

    private String measureTimeForDigits(StringBuilder password,int charPosition){
        for(int j=0;j<10;j++){
            ///check!
            password.setCharAt(charPosition,(char) j);
            String url = createUrl(this.baseUrl, this.username, password.toString(), 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURL();
        }
        return "";
    }

    private char findChar(String lowerCase, String upperCase, String digits){
        return 'c';
    }

    private String createUrl(String baseUrl, String username, String password, Integer difficulty){
        String url_format = baseUrl + "\\?user=%s\\&password=%s\\&difficulty=%d";
        String url = String.format(url_format,username, password, difficulty);
        return url;
    }
}
