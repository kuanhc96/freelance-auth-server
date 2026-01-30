package com.example.freelance_authserver.repository;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.freelance_authserver.entities.UserCredentialsEntity;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.mapper.UserCredentialsMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCredentialsRepository {
    private final UserCredentialsMapper userCredentialsMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private String getUserByEmailAndRole = "SELECT * FROM users WHERE email = :email AND role = :role";

    public Optional<UserCredentialsEntity> getUserByEmailAndRole(String email, UserRole role) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserCredentialsMapper.EMAIL, email);
        params.addValue(UserCredentialsMapper.ROLE, role.getValue());
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(getUserByEmailAndRole, params, userCredentialsMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
