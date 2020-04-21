public class ex01_M2 {

    final static String URL = "http://aoi.ise.bgu.ac.il/";
    public static void main(String[] args) {

        String userName = "307832972";
        int difficulty = 2;
        solvePassword(userName,difficulty);
        //testSolvePasswordClass(userName);
    }




    private static void solvePassword(String userName, int difficulty) {
        CheckPasswordLength checkPasswordLength = new CheckPasswordLength(URL,userName,difficulty);
        checkPasswordLength.measureConnectionWithDifferentLength();
        int length = checkPasswordLength.getLength();
        System.out.println(length);
        //SolvePassword solvePassword = new SolvePassword(length,URL,userName,2);
        //String solved = solvePassword.solvePassword();
        //System.out.println(solved);



    }

    private static void testSolvePasswordClass(String userName) {
        SolvePassword solvePassword = new SolvePassword(6,URL,userName,2);
        solvePassword.solvePassword();
    }

}
