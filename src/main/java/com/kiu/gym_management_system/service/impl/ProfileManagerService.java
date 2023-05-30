package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.config.BCrypt;
import com.kiu.gym_management_system.entity.LoginEntity;
import com.kiu.gym_management_system.entity.MembersEntity;
import com.kiu.gym_management_system.entity.ScheduleEntity;
import com.kiu.gym_management_system.repository.ProfileRepository;
import com.kiu.gym_management_system.repository.LoginRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.ProfileManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileManagerService implements ProfileManger {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    LoginRepository loginRepository;

    @Override
    public Response getUserProfileDetails(int id) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get User Profile Details");
        response.setData(profileRepository.findById(id));
        return response;
    }

    @Override
    public Response getUserProfileDetailsByStatus(int status) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get User Profile Pending Count");
        response.setData(profileRepository.findByStatus(status));
        return response;
    }

    @Override
    public Response getDataForFilterAdmin(int status) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get User Profile Filtered Details ");
        response.setData(profileRepository.findByStatus(status));
        return response;
    }

    @Override
    public Response getAllUserProfileDetails() {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All User Profile Details");
        response.setData(profileRepository.findAll());
        return response;
    }

    @Override
    public Response approvedUser(int status, int id) {
        Optional<MembersEntity> membersEntityOptional = profileRepository.findById(id);
        Response response = new Response();

        if (membersEntityOptional.isPresent()) {
            MembersEntity obj = membersEntityOptional.get();
            obj.setStatus(status);
            profileRepository.save(obj);

            List<MembersEntity> membersEntityList = profileRepository.findByIdAndStatus(id, status);
            List<LoginEntity> loginEntityList = new ArrayList<>();
            for (MembersEntity members : membersEntityList) {
                String userRole = "user";
                String name = members.getName();
                String userName = name.toLowerCase();
                char ch = '_';
                userName = userName.replace(' ', ch)+ members.getId();

                String password = passwordBcrypt(members.getNic());

                LoginEntity loginEntity = new LoginEntity();
                loginEntity.setEmail(members.getEmail());
                loginEntity.setPassword(password);
                loginEntity.setRole(userRole);
                loginEntity.setFullName(members.getName());
                loginEntity.setUsername(userName);
                loginEntityList.add(loginEntity);
                loginRepository.saveAll(loginEntityList);
            }


            response.setCode(200);
            response.setMsg("Get All User Profile Details");
            response.setData(membersEntityList);
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }

    public String passwordBcrypt(String originalPassword) {
//        String  originalPassword = "password";
        String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        System.out.println(generatedSecuredPasswordHash);

        boolean matched = BCrypt.checkpw(originalPassword, generatedSecuredPasswordHash);
        System.out.println(matched);
        return generatedSecuredPasswordHash;
    }


}
