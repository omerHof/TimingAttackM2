import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class URLRequest {

    private String request;

    public URLRequest(String URL) {
        String curlCommand = "curl -s -w \\n%{time_total} -o - ";
        this.request = curlCommand + URL;
    }

    public double measureTime() {
        String requestTime = "";
        String requestResult = "";
        try {
            Process process = Runtime.getRuntime().exec(request);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            requestResult = stdInput.readLine();

            for (String line = stdInput.readLine(); line != null; line = stdInput.readLine()) {
                requestTime = line;
            }

            stdInput.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        double ans  = Double.parseDouble(requestTime)*1000;
        return ans;
    }



    /**
     * this function will measure connection time to a given length
     * help func to measureConnectionWithDifferentLength()
     */

    public double measureConnectionToGivenURLMedian() {
        ArrayList<Double> timeList= new ArrayList<>();
        for(int i=0;i<10;i++){
            double time = measureTime();
            System.out.println(time);
            timeList.add(time);
        }
        Collections.sort(timeList);
        double totalMedianTime = (timeList.get(timeList.size()/2)+ timeList.get(timeList.size()/2-1))/2;

        return totalMedianTime;
    }

    public double measureConnectionToGivenURLMinimum(){
        ArrayList<Double> timeList= new ArrayList<>();
        for(int i=0;i<20;i++){
            double time = measureTime();
            System.out.println(time);
            timeList.add(time);
        }
        double totalMinimumTime = Collections.min(timeList);

        double totalMedianTime = (timeList.get(timeList.size()/2)+ timeList.get(timeList.size()/2-1))/2;
        double totalMaxTime = Collections.max(timeList);
        System.out.println("max time is "+ totalMaxTime);
        System.out.println("min time is "+ totalMinimumTime);
        System.out.println("median time is "+ totalMedianTime);
        return totalMinimumTime;
    }

}
