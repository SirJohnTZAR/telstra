package au.com.telstra.simcardactivator;

import org.springframework.data.repository.CrudRepository;

public interface SimCardInfoRepo extends CrudRepository<SimCards, Long>{

    SimCards findByid(Long id);
    SimCards findBycustomerEmail(String customerEmail);
    SimCards findByiccid(String iccid);

}
