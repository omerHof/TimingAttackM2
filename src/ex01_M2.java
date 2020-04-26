import java.io.IOException;

public class ex01_M2 {

    final static String URL = "http://aoi.ise.bgu.ac.il/";
    public static void main(String[] args) throws IOException {

        //String userName = "207912734";
        String userName = "307832972";


        int difficulty = 1;

        solvePassword(userName,difficulty);

    }

    private static void solvePassword(String userName, int difficulty) throws IOException {
        CheckPasswordLength checkPasswordLength = new CheckPasswordLength(URL,userName,difficulty);
        int length = checkPasswordLength.measureConnectionWithDifferentLength();
       //System.out.println("Password length is: "+length);
       Solver solvePassword = new Solver(length,URL,userName,difficulty);
       String solved = solvePassword.solvePassword();
       System.out.println(solved);
       String finaleUrl = URL+"?user="+userName+"&password="+solved+"&difficulty="+difficulty;
       URLRequest urlRequest = new URLRequest(finaleUrl);
       if (urlRequest.checkPassword()){
           System.out.println(solved);
       }
    }

    public static void checkPassword() throws IOException {
        String finalurl = URL+"?user=308532811&password=joyemayjbdnvaflb&difficulty=1";
        URLRequest urlRequest = new URLRequest(finalurl);
        if (urlRequest.checkPassword()){
            System.out.println(finalurl);
        }
    }

    private static void testSolvePasswordClass(String userName) {
        SolvePassword solvePassword = new SolvePassword(6,URL,userName,2);
        solvePassword.solvePassword();
    }

}
