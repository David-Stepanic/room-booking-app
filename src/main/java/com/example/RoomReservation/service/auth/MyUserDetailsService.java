package com.example.RoomReservation.service.auth;

import com.example.RoomReservation.model.User;
import com.example.RoomReservation.model.UserPrincipal;
import com.example.RoomReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repo.findByUsername(username);

        if (user == null) throw new UsernameNotFoundException("User not found!");

        return new UserPrincipal(user);
    }
}
