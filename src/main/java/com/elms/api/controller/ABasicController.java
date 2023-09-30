package com.elms.api.controller;

import com.elms.api.constant.TestConstant;
import com.elms.api.service.impl.UserServiceImpl;
import com.elms.api.jwt.QRJwt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


public class ABasicController {

    @Autowired
    private UserServiceImpl userService;

    public long getCurrentUser(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        return qrJwt.getAccountId();
    }

    public QRJwt getSessionFromToken(){
        return userService.getAddInfoFromToken();
    }

    public boolean isSuperAdmin(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_ADMIN) && qrJwt.getIsSuperAdmin();
        }
        return false;
    }

    public boolean isShop(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_SHOP);
        }
        return false;
    }

    public boolean isTabletApp(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_TABLET);
        }
        return false;
    }

    public boolean isBoardApp(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_BOARD);
        }
        return false;
    }

    public boolean isPaymentApp(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_PAYMENT);
        }
        return false;
    }

    public boolean isMobileEmployeeApp(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_MOBILE);
        }
        return false;
    }

    public boolean isWebsiteCustomer(){
        QRJwt qrJwt = userService.getAddInfoFromToken();
        if(qrJwt!=null){
            return Objects.equals(qrJwt.getUserKind(), TestConstant.USER_KIND_WEBSITE);
        }
        return false;
    }
}
