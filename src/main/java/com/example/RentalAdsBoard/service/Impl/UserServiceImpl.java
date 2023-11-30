package com.example.RentalAdsBoard.service.Impl;

import com.example.RentalAdsBoard.dao.BaseDao;
import com.example.RentalAdsBoard.dao.UserDao;
import com.example.RentalAdsBoard.entity.User;
import com.example.RentalAdsBoard.service.UserService;

import com.example.RentalAdsBoard.util.PasswordEncoder;
import com.example.RentalAdsBoard.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService  {
    @Autowired
    UserDao userDao;
    @Autowired
    BaseDao<User> baseDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public ResultVo getUserById(Integer userId){
        User user;
        try {
            user=userDao.getById(userId);
            user.setPassword(null);
        } catch (Exception e){
            return new ResultVo().error("Get user by id failed");
        }
        return new ResultVo().success(user);
    }
    @Override
    public ResultVo updateUserById(UserVo userVo){
        User user;
        try {
            user=userDao.getById(userVo.getUserId());
            user.setEmail(userVo.getEmail());
            baseDao.save(user);
        }catch (Exception e){
            return new ResultVo().error("Update user by id failed");
        }
        return new ResultVo().success(user);
    }
    @Override
    public ResultVo updateUserPassword(UserVo userVo){
        User user;
        try {
            user=userDao.getById(userVo.getUserId());
            String newPassword= user.getPassword();
            if (passwordEncoder.matchPassword(newPassword,user.getPassword())) user.setPassword(newPassword);
            else return new ResultVo().error("Update user pwd failed 1");
            baseDao.save(user);
        }catch (Exception e){
            return new ResultVo().error("Update user pwd failed 2");
        }
        return new ResultVo().success(user);
    }
    @Override
    public ResultVo deleteUserById(Integer userId){
        try {
            baseDao.delete(userDao.getById(userId));
        }catch (Exception e){
            return new ResultVo().error("Delete user by id failed");
        }
        return new ResultVo().success();
    }

    @Override
    public ResultVo getUsersList(){
        List<User> list;
        try {
            list=userDao.getUsersList();
        }catch (Exception e){
            return new ResultVo().error("Get user list failed");
        }
        return new ResultVo().success(list);
    }
    @Override
    public ResultVo register(RegisterVo registerVo){
        User user=new User();
        user.setUsername(registerVo.getUsername());
        user.setPassword(passwordEncoder.encodePassword(registerVo.getPassword()));
        user.setEmail(registerVo.getEmail());
        user.setStatus(Integer.parseInt(registerVo.getRole()));
        user.setAvatar("666");
        try {
            if (userDao.getByUsername(registerVo.getUsername())!=null){
                return new ResultVo().error("Null username");
            }
            baseDao.save(user);
        }catch (Exception e){
            return new ResultVo().error("Register failed");
        }
        return new ResultVo().success(user);
    }

    @Override
    public ResultVo login(LoginVo loginVo){
        User user;
        String pwd;
        try {
            user=userDao.getByUsername(loginVo.getUsername());
            if (user==null) return new ResultVo().error("Null user");
            pwd = user.getPassword();
            user.setPassword(null);
        } catch (Exception e){
            return new ResultVo().error("Login failed");
        }
        System.out.println(loginVo.getPassword());
        System.out.println(pwd);
        if(passwordEncoder.matchPassword(loginVo.getPassword(),pwd)){
            return new ResultVo().success(user);
        }
        else
            return new ResultVo().error("Wrong password");
    }

    @Override
    public ResultVo manageAuthority(AuthorityVo authorityVo) {
        try {
            User user=userDao.getByUsername(authorityVo.getUsername());
            user.setStatus(authorityVo.getLevel());
            baseDao.save(user);
        }catch (Exception e){
            return new ResultVo().error("Manage failed");
        }
        return new ResultVo().success();
    }


}
