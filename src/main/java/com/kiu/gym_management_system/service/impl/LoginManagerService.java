package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.entity.LoginEntity;
import com.kiu.gym_management_system.model.login.LoginAbilityModel;
import com.kiu.gym_management_system.model.login.LoginModel;
import com.kiu.gym_management_system.repository.LoginRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoginManagerService implements LoginManager {


    @Autowired
    LoginRepository loginRepository;

    @Override
    public Response getUserLoginDetails(String email, String password) {
        List<LoginEntity> loginEntityList = loginRepository.findByEmailAndPassword(email, password);

        List<LoginModel> loginModelArrayList = new ArrayList<>();
        Response response = new Response();
        if (loginEntityList.isEmpty()) {
            response.setCode(400);
            response.setMsg("Wrong Email or Password");
        } else {

            for (LoginEntity login : loginEntityList) {
                LoginModel loginModel = new LoginModel();
                loginModel.setId(login.getId());
                loginModel.setFullName(login.getFullName());
                loginModel.setUsername(login.getUsername());
                loginModel.setEmail(login.getEmail());
                loginModel.setRole(login.getRole());
                loginModel.setPassword(login.getPassword());

                LoginAbilityModel loginAbilityModel = new LoginAbilityModel();

                if (login.getRole().equalsIgnoreCase("admin")) {
                    loginAbilityModel.setAction("manage");
                    loginAbilityModel.setSubject("admin");
                } else if (login.getRole().equalsIgnoreCase("user")) {
                    loginAbilityModel.setAction("manage");
                    loginAbilityModel.setSubject("ACL");
                }
                loginModel.setAbility(loginAbilityModel);

                loginModelArrayList.add(loginModel);


            }
//        Response response = new Response();
            response.setCode(200);
            response.setMsg("Get User Login Details");
            response.setData(loginModelArrayList);
        }
        return response;
    }
}
