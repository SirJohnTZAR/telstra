package au.com.telstra.simcardactivator;

/*
Used to handler request going to the Micro service.
 */

public class ActivationRequest {
    private String iccid;

    public ActivationRequest(String iccid) {
        this.iccid = iccid;
    }

    // Getters and Setters
    public String getIccidiccid() {
        return iccid;
    }

    public void setIccidccid(String simId) {
        this.iccid = simId;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
               "iccid='" + iccid + '\'' +
               '}';
    }
}