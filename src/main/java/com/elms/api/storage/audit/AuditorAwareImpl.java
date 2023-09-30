package com.elms.api.storage.audit;

import com.elms.api.jwt.QRJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;


@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor(){
        String currentUser = getCurrentUser();
        return Optional.of(currentUser==null?"unknown":currentUser);

    }

    public  String getCurrentUser() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session =  attr.getRequest().getSession();
            QRJwt qrJwt = (QRJwt) session.getAttribute("user_sesson");
            if(qrJwt!=null){
                return qrJwt.getUsername();
            }
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
        }

        return null;
    }
}
