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
import org.springframework.util.StringUtils;
import uaa.config.ApplicationProperties;
import uaa.config.Constants;
import uaa.config.UaaProperties;
import uaa.domain.uaa.UaaGraph;
import uaa.domain.uaa.UaaToken;
import uaa.domain.uaa.UaaUser;
import uaa.domain.uaa.UaaVisitRecord;
import uaa.repository.uaa.UaaGraphRepository;
import uaa.repository.uaa.UaaTokenRepository;
import uaa.repository.uaa.UaaVisitCountRepository;
import uaa.service.dto.login.UserInfo;
import util.CaptchaGenerator;
import util.UUIDGenerator;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class UaaLoginService {
//    private static ApplicationProperties APPLICATION_PROPERTIES = (ApplicationProperties) SpringLoginUtils.getBean(ApplicationProperties.class);


    @Autowired
    private UaaProperties uaaProperties;

    @Autowired
    private UaaGraphRepository uaaGraphRepository;

    @Autowired
    private UaaTokenRepository uaaTokenRepository;

    //生成TOKEN
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private OAuth2RequestFactory oAuth2RequestFactory;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;
//    //END 生成token
    @Autowired
    private UaaVisitCountRepository uaaVisitCountRepository;

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
        //删除token
        List<UaaToken> allByCreatedid = uaaTokenRepository.findAllByCreatedid(uaaUser.getId());
        if(allByCreatedid!=null||allByCreatedid.size()>0){
            for(UaaToken token: allByCreatedid){
                uaaTokenRepository.delete(token);
            }
        }
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
        uaaToken.setId(UUIDGenerator.getUUID());
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

    public UserInfo prepareForUserInfo(UaaUser uaaUser){
//        if(uaaUser==null)
        if(Constants.USER_STATUS_DELETE.equals(uaaUser.getStatus()))
            return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(uaaUser.getEmail());
        userInfo.setName(uaaUser.getName());
        userInfo.setNickName(uaaUser.getNickName());
        return userInfo;
    }
    public UaaToken getUserByToken(String token){
        //如果token不存在或过期则返回null
        UaaToken oneByAccesstoken = uaaTokenRepository.findOneByAccesstoken(token);
        if(oneByAccesstoken==null)
            return null;
        Instant toTime = oneByAccesstoken.getCreatedDate().plusSeconds(oneByAccesstoken.getValidtime());
        if(!toTime.isAfter(Instant.now())){
            //删除token
            uaaTokenRepository.delete(oneByAccesstoken.getId());
            return null;
        }
        return oneByAccesstoken;
    }

    public int visitcount(HttpServletRequest request) {
        //保存，并计数
        UaaVisitRecord record = new UaaVisitRecord();
        record.setId(UUIDGenerator.getUUID());
        record.setCreatedDate(Instant.now());
        record.setIp(getIpAddr(request));
        uaaVisitCountRepository.save(record);
//        return uaaVisitCountRepository.countAll();
        List<UaaVisitRecord> all = uaaVisitCountRepository.findAll();
        return all.size();
    }
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(" x-forwarded-for ");
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader(" Proxy-Client-IP ");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getHeader(" WL-Proxy-Client-IP ");
        }
        if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    public OAuth2AccessToken getToken(String userId){
        String clientId = uaaProperties.getWebClientConfiguration().getClientId();
        Map<String, String> parameters = new HashMap<>();
        parameters.put(AUTHORIZED_GRANT_TYPES, AUTHORIZED_GRANT_TYPES_PASSWORD);
        parameters.put(AUTHORIZED_GRANT_TYPES_USERNAME, userId);

        ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
        TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

        if (clientId != null && !clientId.equals("")) {
            if (!clientId.equals(tokenRequest.getClientId())) {
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
        }

        if (authenticatedClient != null) {
            OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        }
        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            throw new InvalidRequestException("Missing grant type");
        }
        if (tokenRequest.getGrantType().equals("implicit")) {
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        }

        if (isAuthCodeRequest(parameters)) {
            // The scope was requested or determined during the authorization step
            if (!tokenRequest.getScope().isEmpty()) {
                tokenRequest.setScope(Collections.<String>emptySet());
            }
        }

        if (isRefreshTokenRequest(parameters)) {
            // A refresh token has its own default scopes, so we should ignore any added by the factory here.
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
        }

        //OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
        OAuth2Request storedOAuth2Request = oAuth2RequestFactory.createOAuth2Request(authenticatedClient, tokenRequest);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add((new SimpleGrantedAuthority("anonymoususer")));

        Authentication userAuth = new UsernamePasswordAuthenticationToken(userId, "", grantedAuthorities);
        OAuth2AccessToken token = tokenServices.createAccessToken(new OAuth2Authentication(storedOAuth2Request, userAuth));
        if (token == null) {
            throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
        }

        return token;
    }
    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }


}
