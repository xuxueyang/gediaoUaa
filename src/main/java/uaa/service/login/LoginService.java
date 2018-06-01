package uaa.service.login;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.ApplicationProperties;
import uaa.config.Constants;
import uaa.domain.uaa.UaaGraph;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.repository.uaa.UaaGraphRepository;
import uaa.repository.uaa.UaaTokenRepository;
import uaa.service.dto.login.UserInfo;
import util.CaptchaGenerator;
import util.UUIDGenerator;

import java.util.*;

@Service
@Transactional
public class LoginService {
//    private static ApplicationProperties APPLICATION_PROPERTIES = (ApplicationProperties) SpringLoginUtils.getBean(ApplicationProperties.class);

    @Autowired
    private UaaGraphRepository uaaGraphRepository;

    @Autowired
    private UaaTokenRepository uaaTokenRepository;

    //生成TOKEN
//    //Start 生成token
//    @Autowired
//    private ClientDetailsService clientDetailsService;
//    @Autowired
//    private AuthorizationServerTokenServices tokenServices;
//
//    @Autowired
//    private OAuth2RequestFactory oAuth2RequestFactory;
//    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
//    //END 生成token


    private final String AUTHORIZED_GRANT_TYPES_USERNAME = "username";
    private final String AUTHORIZED_GRANT_TYPES_PASSWORD = "password";
    private final String AUTHORIZED_GRANT_TYPES_REFRESH_TOKEN = "refresh_token";
    private final String AUTHORIZED_GRANT_TYPES = "grant_type";
    private final String GRAPH_TYPE_CODE = "code";
    private final String GRAPH_TYPE_NUM  = "num";

    //生成graph
    public Map<String,Object> createGraph() throws Exception{
        //生成验证码和相应的编码图片
        //生成图形验证码
        String captchaCode = CaptchaGenerator.generateCaptchaCode(4);
        //Base64编码
        String captchaCode_base64 = CaptchaGenerator.outputImageBase64(300, 80, captchaCode);
        //保存
        UaaGraph uaaGraph = new UaaGraph();
        uaaGraph.setId(UUIDGenerator.getUUID());
        uaaGraph.setValue(captchaCode);
        uaaGraph.setType(GRAPH_TYPE_CODE);
        uaaGraphRepository.save(uaaGraph);

        Map<String ,Object> map = new HashMap<>();
        map.put("graph",captchaCode_base64);
        map.put("id",uaaGraph.getId());
        return map;
    }

    //验证graph
    public boolean verifyGraph(String id,String value){
        if(id==null||value==null){
            return false;
        }
        UaaGraph uaaGraph = uaaGraphRepository.findById(id);
        if(uaaGraph==null){
            return false;
        }
        if(!uaaGraph.getValue().equals(value)){
            return false;
        }else{
            //删除
            uaaGraphRepository.deleteById(id);
            return true;
        }
    }
    public Map login(UaaUser uaaUser) {
        //登录，创建token
        Map map = new HashMap<String,Object>();
        //生成token
//        OAuth2AccessToken token = createToken(APPLICATION_PROPERTIES.getClient(),uaaUser.getId(), uaaUser.getTenantCode());
        String token = createToken("qinglonghui",uaaUser.getId(), uaaUser.getTenantCode());

        //技术原因，先只用token就好了
        map.put(Constants.ACCESS_TOKEN,token);
//        map.put(Constants.REFRESH_TOKEN,token.getRefreshToken().getValue());
        //存userInfo
        map.put(Constants.USERINFO,prepareForUserInfo(uaaUser));
        //存到数据库里，token
        UaaToken uaaToken = new UaaToken();
        uaaToken.setAccesstoken(token);
//        uaaToken.setRefreshtoken(token.getRefreshToken().getValue());
        uaaToken.setCreatedid(uaaUser.getId());
        uaaToken.setValidtime(Constants.TOKEN_VALID_TIME);
        uaaTokenRepository.save(uaaToken);

        return map;
    }

    private String createToken(String qinglonghui, String id, String tenantCode) {
        return UUIDGenerator.getUUID();
    }

    private UserInfo prepareForUserInfo(UaaUser uaaUser){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(uaaUser.getEmail());
        userInfo.setName(uaaUser.getName());
        userInfo.setNickName(uaaUser.getNickName());
        return userInfo;
    }

//    /**
//     * 创建token
//     *
//     * @param clientId
//     * @param acctId
//     * @param tenantCode
//     * @return
//     */
//    public OAuth2AccessToken createToken(final String clientId, String acctId, final String tenantCode) {
//        HashMap<String, String> parameters = new HashMap<>();
//        parameters.put(AUTHORIZED_GRANT_TYPES, AUTHORIZED_GRANT_TYPES_PASSWORD);
//        parameters.put(AUTHORIZED_GRANT_TYPES_USERNAME, acctId);
//
//        ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
//        TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);
//        if (clientId != null && !clientId.equals("")) {
//            // Only validate the client details if a client authenticated during this
//            // request.
//            if (!clientId.equals(tokenRequest.getClientId())) {
//                // double check to make sure that the client ID in the token request is the same as that in the
//                // authenticated client
//                throw new InvalidClientException("Given client ID does not match authenticated client");
//            }
//        }
//        if (authenticatedClient != null) {
//            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
//        }
//        if (!org.springframework.util.StringUtils.hasText(tokenRequest.getGrantType())) {
//            throw new InvalidRequestException("Missing grant type");
//        }
//        if (tokenRequest.getGrantType().equals("implicit")) {
//            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
//        }
//
//        if (isAuthCodeRequest(parameters)) {
//            // The scope was requested or determined during the authorization step
//            if (!tokenRequest.getScope().isEmpty()) {
//                System.out.println("Clearing scope of incoming token request");
//                tokenRequest.setScope(Collections.<String>emptySet());
//            }
//        }
//
//        if (isRefreshTokenRequest(parameters)) {
//            // A refresh token has its own default scopes, so we should ignore any added by the factory here.
//            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
//        }
//
//        OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authenticatedClient, tokenRequest);
//
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//
//        grantedAuthorities.add((new SimpleGrantedAuthority(Constants.ANONYMOUS_USER)));
//
//        Authentication userAuth = new UsernamePasswordAuthenticationToken(tenantCode + Constants.TOKEN_BODY_SEPARATOR + acctId, "", grantedAuthorities);
//        OAuth2AccessToken token = tokenServices.createAccessToken(new OAuth2Authentication(storedOAuth2Request, userAuth));
//        if (token == null) {
//            throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
//        }
//        return token;
//    }
//    private boolean isAuthCodeRequest(Map<String, String> parameters) {
//        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
//    }
//    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
//        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
//    }

}
