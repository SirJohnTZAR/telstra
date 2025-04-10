package au.com.telstra.simcardactivator;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SimCards {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    String iccid, customerEmail;
    Boolean active;

    public SimCards() {}

    public SimCards(String iccid, String customerEmail, Boolean active) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.active = active;
    }

    public String getIccid() {
        return iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Boolean getActive() {
        return active;
    }



    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                "{\n" +
                        "   \"iccid\"='%s', \n" +
                        "   \"customerEmail\"='%s',\n" +
                        "   \"active\"='%s'\n}",
                id, iccid, customerEmail, active);
    }

}
