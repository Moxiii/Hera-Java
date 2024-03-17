package com.moxi.hera.Auth;

import com.moxi.hera.Config.Jwt.JwtUtil;
import com.moxi.hera.Config.Jwt.TokenManager;
import com.moxi.hera.Response.LoginRes;
import com.moxi.hera.User.Model.User;
import com.moxi.hera.User.Repository.UserRepository;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping({"api/auth"})
public class AuthController {
@Autowired
private UserRepository userRepository;

@Autowired
private JwtUtil jwtUtil;

@Autowired
private AuthenticationManager authenticationManager;
@Autowired
private TokenManager tokenManager;
   @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody User user){
       try{
           BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
           String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
           user.setPassword(encodedPassword);
           SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
           String formatedDate = dateFormat.format(new Date());
           user.setInscription(formatedDate);
           userRepository.save(user);
           return new ResponseEntity<>("User created sucessfully" , HttpStatus.CREATED);
       }
       catch (Exception e){e.printStackTrace();
       return new ResponseEntity<>("ERROR on user creation " , HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @PostMapping("/login")
   @ResponseBody
    public ResponseEntity<?>Login(@RequestBody User user , HttpServletRequest req){
       BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       User existingUser = null;
       if(user.getEmail() !=null){
          existingUser =  userRepository.findUserByEmail(user.getEmail());
       } else if (user.getUsername() !=null) {
           existingUser = userRepository.findUserByUsername(user.getUsername());
       }

       if(existingUser != null){
           if(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
            String  validToken = tokenManager.getValidToken(existingUser.getUsername());
            if(validToken != null)
            { return ResponseEntity.ok(new LoginRes(existingUser.getUsername(), validToken));}
            else
            { UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(existingUser.getUsername(), user.getPassword());
                try{
                    Authentication authentication = authenticationManager.authenticate(token);
                    if (authentication != null && authentication.isAuthenticated()){
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                        SecurityContext sc = SecurityContextHolder.getContext();
                        sc.setAuthentication(authentication);
                        String jwtToken = jwtUtil.createToken(existingUser);
                        tokenManager.addToken(existingUser.getUsername(), jwtToken);
                        return ResponseEntity.ok(new LoginRes(existingUser.getUsername(), jwtToken));
                    }
                }catch (AuthenticationException e){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth failed");}
            }
           } else{ return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorect Password");}
       } else {  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");}
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
   }
}
