package com.elms.api.storage.id;

import com.elms.api.storage.base.Auditable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try{
            Auditable reuseId = (Auditable) o;
            if(reuseId.getId()!=null){
                return reuseId.getId();
            }
        }catch (Exception e){
            //e.printStackTrace();
        }
        return SnowFlakeIdService.getInstance().nextId();
    }
}
