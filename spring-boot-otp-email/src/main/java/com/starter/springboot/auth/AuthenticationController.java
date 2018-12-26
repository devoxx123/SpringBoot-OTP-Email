package com.starter.springboot.auth;

import com.starter.springboot.domain.User;
import com.starter.springboot.rest.dto.LoginDTO;
import com.starter.springboot.security.jwt.JWTConfigurer;
import com.starter.springboot.security.jwt.JWTToken;
import com.starter.springboot.security.jwt.TokenProvider;
import com.starter.springboot.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authorize(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		log.debug("Credentials: {}", loginDTO);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());
		try {
			Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			boolean rememberMe = (loginDTO.isRememberMe() == null) ? false : loginDTO.isRememberMe();

			String jwt = tokenProvider.createToken(authentication, rememberMe);
			response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);

			return ResponseEntity.ok(new JWTToken(jwt));
		} catch (AuthenticationException exception) {
			return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);

	}
	@RequestMapping(value = "/getuserbyname", method = RequestMethod.GET)
	public ResponseEntity<String> findByUserName(@RequestParam("username") String username) {
		log.info("Fetch UserDetails with name : {}", username);
		String userDetails = userService.findEmailByUsername(username);
		return new ResponseEntity<String>(userDetails, HttpStatus.OK);
		
	}

}
