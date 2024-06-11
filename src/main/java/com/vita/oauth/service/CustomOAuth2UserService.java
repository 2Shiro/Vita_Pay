package com.vita.oauth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.vita.oauth.domain.CustomOAuth2User;
import com.vita.oauth.domain.GoogleResponse;
import com.vita.oauth.domain.NaverResponse;
import com.vita.oauth.domain.OAuth2Response;
import com.vita.oauth.domain.UserDTO;
import com.vita.oauth.domain.UserVo;
import com.vita.oauth.mapper.UserMapper;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	 private final UserMapper userMapper;

	    public CustomOAuth2UserService(UserMapper userMapper) {

	        this.userMapper = userMapper;
	    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String oauth_name = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        System.out.println("여기까지는 되나?");
        UserVo userVo = userMapper.findByUsername(oauth_name);
        System.out.println("메퍼에서 에러인가");
        if (userVo == null) {

        	UserVo userEntity = new UserVo();
           System.out.println("널이다라고 정상으로 오나?");
           userEntity.setOauth_name(oauth_name);
           userEntity.setEmail(oAuth2Response.getEmail());
           userEntity.setName(oAuth2Response.getName());
           
           
			/*
			 * UserVo userEntity = new UserVo(); userEntity.setUser_name(username); // 이제
			 * naver nwognwognweogds
			 * userEntity.setUser_email(oAuth2Response.getEmail());//이메일
			 * userEntity.setUser_social_num(oAuth2Response.getName());//이게 진짜이름
			 * userEntity.setRole("ROLE_USER"); userEntity.setUser_passwd("12345");
			 * System.out.println("userEntity의 값 :"+userEntity);
			 */
           
            userMapper.save(userEntity);
            
            // 저장된 사용자 정보를 다시 조회
            userVo = userMapper.findByUsername(oauth_name);
            

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(oauth_name);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            userDTO.setUserId(userVo.getId());

            System.out.println("null이 아닐때 userDTO : " + userDTO);
            return new CustomOAuth2User(userDTO);
        }
        else {
        	   System.out.println("아니면 일로오나?");
			/*
			 * existData.setEmail(oAuth2Response.getEmail());
			 * existData.setName(oAuth2Response.getName());
			 * 
			 * userRepository.save(existData);
			 */
        	   //이거 유저가 네이버로그인 구글로그인 카카오 로그인 하면 3개가 만들어짐
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(oauth_name);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            userDTO.setUserId(userVo.getId());
            System.out.println("유저가 있을때 userDTO : " + userDTO);
            return new CustomOAuth2User(userDTO);
        }
    }
  }

