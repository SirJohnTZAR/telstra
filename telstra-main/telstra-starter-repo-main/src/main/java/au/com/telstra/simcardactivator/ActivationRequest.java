package au.com.telstra.simcardactivator;

/*
Used to handler request going to the Micro service.
 */

public class ActivationRequest {
    private int iccid;

    public ActivationRequest(int iccid) {
        this.iccid = iccid;
    }

    // Getters and Setters
    public int getIccidiccid() {
        return iccid;
    }

    public void setIccidccid(int simId) {
        this.iccid = simId;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
               "iccid='" + iccid + '\'' +
               '}';
    }
}