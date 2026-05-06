package com.group.thr.hedi.DAO.Implement;

import com.group.thr.hedi.DAO.Interface.IAuthenticationDAO;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationDAO implements IAuthenticationDAO {
    @Override
    public String authenticate(String email, String password) {
        return "nuag";
    }
}
