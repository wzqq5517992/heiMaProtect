package com.itcast.mapper;

import com.itcast.entity.User;

public interface UserMapper {
    User selectByPrimaryKey(String id);
}
