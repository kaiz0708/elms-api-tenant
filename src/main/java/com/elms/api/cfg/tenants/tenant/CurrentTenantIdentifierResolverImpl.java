package com.elms.api.cfg.tenants.tenant;

import com.elms.api.cfg.tenants.TenantConstant;
import com.elms.api.cfg.tenants.TenantDBContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private String defaultTenant = TenantConstant.DEFAULT_TENANT_ID;

    @Override
    public String resolveCurrentTenantIdentifier() {
        String t =  TenantDBContext.getCurrentTenant();
        if(t!=null){
            return t;
        } else {
            return defaultTenant;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
