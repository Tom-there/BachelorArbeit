package de.hhu.cs.stups.algvis;

public class Errors {
    public static void secondTokenIsNoChar(String full, String token){
        System.err.println("""
                ERROR
                Second token "
                """ + token  + """
                " in line:
                "
                """ + full + """
                " 
                was not an " = ".
                Assuming this was the case
                """);
    }
}
