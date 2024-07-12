package bg.duosoft.uniapplicationdemo.exceptions;


public class ReadClaimException extends RuntimeException {

    public ReadClaimException() {
    }

    public ReadClaimException(String claim, String token) {
        super("Claim: " + claim + " Token: " + token);
    }

}
