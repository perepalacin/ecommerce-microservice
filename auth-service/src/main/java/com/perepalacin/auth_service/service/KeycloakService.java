package com.perepalacin.auth_service.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.perepalacin.auth_service.entity.dao.UserDao;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.perepalacin.auth_service.entity.dto.UserDto;
import com.perepalacin.auth_service.util.KeycloakProvider;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    //TODO: Create controller to get all user directions, edit direction and delete direction.

    private final UserService userService;

    public List<UserRepresentation> findAllUsers(){
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }


    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }


    public ResponseEntity<String> createUser(@NonNull UserDto userDTO) {

        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(userDTO.getPassword());

            usersResource.get(userId).resetPassword(credentialRepresentation);

            userService.createUser(UserDao.builder()
                            .id(UUID.fromString(userId))
                            .email(userDTO.getEmail())
                            .firstName(userDTO.getFirstName())
                            .lastName(userDTO.getLastName())
                            .build());
            return ResponseEntity.ok("User created successfully!!");
        } else if (status == 409) {
            log.error("User exist already!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User exist already!");
        } else {
            log.error("Error creating user, please contact with the administrator.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user, please contact with the administrator.");
        }
    }


    public ResponseEntity<String> deleteUser(String userId){
        Boolean isValidUpdate = userService.deleteUser(UUID.fromString(userId));
        if (!isValidUpdate) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have the permissions to perform this operation");
        }
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!");

    }


    public ResponseEntity<String> updateUser(String userId, @NonNull UserDto userDTO){

        Boolean isValidUpdate = userService.updateUser(UUID.fromString(userId), userDTO);

        if (!isValidUpdate) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have the permissions to perform this operation");
        }

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = KeycloakProvider.getUserResource().get(userId);
        usersResource.update(user);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!");
    }
}
