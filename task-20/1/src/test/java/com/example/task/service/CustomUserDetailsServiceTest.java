package com.example.task.service;

import com.example.task.dao.AppUserDao;
import com.example.task.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private AppUserDao appUserDao;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {
        String existingUsername = "testuser";
        AppUser existingUser = new AppUser(
                existingUsername, "encoded_password", "USER"
        );

        when(appUserDao.findByUsername(existingUsername)).thenReturn(existingUser);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(existingUsername);

        assertNotNull(userDetails);
        assertEquals(existingUsername, userDetails.getUsername());
        assertEquals(existingUser.getPassword(), userDetails.getPassword());

        assertTrue(userDetails.getAuthorities().stream().anyMatch(
                grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))
        );
        verify(appUserDao, times(1)).findByUsername(existingUsername);
    }

    @Test
    public void loadUserByUsername_WithNonExistingUser_ShouldThrowUsernameNotFoundException() {
        String nonExistingUsername = "nonexistentuser";

        when(appUserDao.findByUsername(nonExistingUsername)).thenReturn(null);

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistingUsername)
        );
        assertEquals("User with username [nonexistentuser] not found", thrown.getMessage());
        verify(appUserDao, times(1)).findByUsername(nonExistingUsername);
    }
}