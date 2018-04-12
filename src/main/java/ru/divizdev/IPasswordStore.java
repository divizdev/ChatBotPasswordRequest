package ru.divizdev;

import java.util.Map;

public interface IPasswordStore {

    Map<String, String> getPasswordByUserId(Integer userId);
    Map<String, String> getPasswordByTelephone(String telephone, Integer userId);
}
