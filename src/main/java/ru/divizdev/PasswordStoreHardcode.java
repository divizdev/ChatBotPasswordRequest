package ru.divizdev;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PasswordStoreHardcode implements IPasswordStore {

    final Map<Integer, String> _userIdTelephone = new HashMap<>();
    final Map<String, Map<String, String>> _passwords = new HashMap<>();
    final RandomPassword _randomPassword = new RandomPassword(6);


    public PasswordStoreHardcode(){

        Map<String, String>  systemPassword = new HashMap<>();
        systemPassword.put("СУРВ GUI G38", _randomPassword.nextString());
        systemPassword.put("СУРВ Fiori G71", _randomPassword.nextString());

        _passwords.put("+79167239616", systemPassword);

    }

    @Override
    @Nullable
    public Map<String, String> getPasswordByUserId(Integer userId){
        String telephone = _userIdTelephone.get(userId);
        if (telephone != null) {
            return _passwords.get(telephone);
        }
        return null;
    }

    @Override
    @Nullable
    public Map<String, String> getPasswordByTelephone(String telephone, Integer userId){

        _userIdTelephone.put(userId, telephone);
        return _passwords.get(telephone);
    }



}
