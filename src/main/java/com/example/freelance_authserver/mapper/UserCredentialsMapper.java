package com.example.freelance_authserver.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.example.freelance_authserver.entities.UserCredentialsEntity;
import com.example.freelance_authserver.enums.UserRole;

@Component
public class UserCredentialsMapper implements RowMapper<UserCredentialsEntity> {
    public static final String USER_ID = "user_id";
    public static final String USER_GUID = "user_guid";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";

    @Nullable
    @Override
    public UserCredentialsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserCredentialsEntity.builder()
                    .userId(rs.getLong(USER_ID))
                    .userGUID(rs.getString(USER_GUID))
                    .email(rs.getString(EMAIL))
                    .password(rs.getString(PASSWORD))
                    .role(UserRole.getValue(rs.getString(ROLE)))
                    .build();
    }
}
