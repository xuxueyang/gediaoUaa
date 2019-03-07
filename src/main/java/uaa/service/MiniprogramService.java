package uaa.service;

import com.alibaba.fastjson.JSONObject;
import core.ReturnCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.service.dto.WechatDTO;
import uaa.service.dto.WechateDecryptInDTO;
import uaa.web.rest.util.AES;
import uaa.web.rest.util.ConnectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class MiniprogramService {


	/**
	 * 微信小程序获取openId等信息
	 * @param code
	 * @return
	 */
	public WechatDTO getWechatInfo(String code, String fromSource) {
		String url="";
        url= Constants.wechatUrl+"appid="+Constants.gediaoAppid+"&secret="+Constants.gediaoSecret+"&js_code="+code+"&grant_type=authorization_code";

		String response= ConnectUtil.doGet(url);
		WechatDTO wechatDTO=JSONObject.parseObject(response, WechatDTO.class);
		return wechatDTO;
	}

	/**
	 * 获取基本信息
	 * @param code
	 * @param wechateDecryptInDTO
	 * @return
	 */
	public Map<String, Object> getWechatInfo(String code,WechateDecryptInDTO wechateDecryptInDTO) {
		WechatDTO wechatDTO = getWechatInfo(code,wechateDecryptInDTO.getFromSource());
		Map<String, Object> result = new HashMap<>();

		if(wechatDTO!=null&&wechatDTO.getErrcode()!=null) {
//	    	 LogUtil.error(SecurityUtils.getCurrentUserIdStr(), SecurityUtils.getCurrentTenantCode(),
//		                "/api/mini/{code}", "获取微信基础信息数据异常",wechatDTO.getErrcode(), wechatDTO.getErrmsg());
	    	 result.put("returnCode", "6100");
	    	 result.put("data", "微信基础数据获取失败");
	    	 return result;
	     }

		if(wechatDTO.getSession_key()!=null) {
			//解密unionId
			String response = AES.wxDecrypt(wechateDecryptInDTO.getEncryptedData(), wechatDTO.getSession_key()
					,wechateDecryptInDTO.getIv());
			if(StringUtils.isBlank(response)) {
//				LogUtil.error(SecurityUtils.getCurrentUserIdStr(), SecurityUtils.getCurrentTenantCode(),
//		                "/api/mini/{code}", "获取微信UnionId数据异常","", "");
				result.put("returnCode", "6100");
				result.put("data", "微信解密失败");
				return result;
			}else {
				Map<String, Object> map = JSONObject.parseObject(response, Map.class);
				wechatDTO.setUnionid((String) map.get("unionId"));
			}
		}
		result.put("returnCode", ReturnCode.GET_SUCCESS);
		result.put("data", wechatDTO);

		return result;
	}
//
//	/**
//	 * 获取手机号
//	 * @param wechatId 微信唯一标识
//	 * @return
//	 */
//	public LoginPassWordDTO getLoginPassword(String wechatId,String tenantCode){
//		//根据唯一标识找到账号信息
//		LoginPassWordDTO loginPassWordDTO=null;
//		List<Acct> acctByOpenIdList= verifyOpenIdExist(wechatId, tenantCode);
//		Acct acctByOpenId =null;
//		if(acctByOpenIdList!=null&&acctByOpenIdList.size()>0) {
//			loginPassWordDTO= new LoginPassWordDTO();
//			acctByOpenId= acctByOpenIdList.get(0);
//			loginPassWordDTO.setUsername(acctByOpenId.getTel());
//			loginPassWordDTO.setPassword(acctByOpenId.getPassword());
//		}
//		return loginPassWordDTO;
//
//	}
//
//	/**
//	 * 手机号解密
//	 * @param mobileDTO
//	 * @return
//	 */
//	public String Decrypt(WechateDecryptInDTO mobileDecryptInDTO) {
//		String response = AES.wxDecrypt(mobileDecryptInDTO.getEncryptedData(), mobileDecryptInDTO.getSessionKey()
//										,mobileDecryptInDTO.getIv());
//		WechatMobileDecryptOutDTO dto=JSONObject.parseObject(response, WechatMobileDecryptOutDTO.class);
//		return dto.getPhoneNumber()==null?"":dto.getPhoneNumber();
//	}
//
//
//	/**
//	 * 手机号与微信唯一标识注册
//	 * @param wechatRegistryMobileDTO
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, String> miniRegistry(WechatRegistryMobileDTO wechatRegistryMobileDTO) throws Exception {
//
//		Map<String, String> map = new HashMap<>();
//
//		String wechatId = wechatRegistryMobileDTO.getWechatId();
//		//String mobile = wechatRegistryMobileDTO.getTel();
//
//		String tenantCode = SecurityUtils.getCurrentTenantCode();
//		String instanceCode=SecurityUtils.getCurrentInstanceCode();
//		String spaceCode = SecurityUtils.getCurrentSpaceCode();
//
//		//解密手机号
//		WechateDecryptInDTO mobileDecryptInDTO = new WechateDecryptInDTO();
//		mobileDecryptInDTO.setEncryptedData(wechatRegistryMobileDTO.getEncryptedData());
//		mobileDecryptInDTO.setIv(wechatRegistryMobileDTO.getIv());
//		mobileDecryptInDTO.setSessionKey(wechatRegistryMobileDTO.getSessionKey());
//		String mobile=Decrypt(mobileDecryptInDTO);
//		Long acctId=null;
//
//		/*Acct acctByOpenId = acctRepository.findByOpenIdAndTenantCodeAndStatus(wechatId, tenantCode,"A");
//		Acct acctByTel = acctRepository.findByTelAndTenantCodeAndStatus(mobile, tenantCode,"A");*/
//		List<Acct> acctByOpenIdList= verifyOpenIdExist(wechatId, tenantCode);
//		List<Acct> acctByTelList = verifyPhoneExist(mobile, tenantCode);
//		Acct acctByOpenId =null;
//		Acct acctByTel = null;
//		if(acctByOpenIdList!=null&&acctByOpenIdList.size()>0) {
//			acctByOpenId=acctByOpenIdList.get(0);
//		}
//
//		if(acctByTelList!=null && acctByTelList.size()>0) {
//			acctByTel=acctByTelList.get(0);
//		}
//
//		//均不存在创建账号，并发送短信
//		if(acctByOpenId==null&&acctByTel==null) {
//
//			PersonalRegistryMobileDTO registryDTO = new PersonalRegistryMobileDTO();
//			registryDTO.setTel(mobile);
//			String password = CaptchaGenerator.generateCaptchaCode(8);
//			registryDTO.setPassword(password);
//
//			final LoginConfig loginConfig = LibraryConfigUtil.getLoginConfig(instanceCode, tenantCode);
//
//			ResultInfo resultInfo=registryService.registryMobileNoCaptchaCode(registryDTO, loginConfig, instanceCode, tenantCode, spaceCode);
//			String result= resultInfo.getFirstErrorCode();
//			if(ReturnCode.CREATE_SUCCESS.equals(result)) {
//				Acct acctByTel2 = acctRepository.findOneByTelAndTenantCode(mobile, tenantCode);
//				acctByTel2.setOpenId(wechatId);
//				acctByTel2.setType(wechatRegistryMobileDTO.getFromSource());//来源于哪个小程序
//				acctByTel2=acctRepository.save(acctByTel2);
//				acctId=acctByTel2.getId();
//
//			}else {
//				//return ReturnCode.ERROR_CREATE;
//				map.put("returnCode", ReturnCode.ERROR_CREATE);
//				return map;
//			}
//
//
//		}else if(acctByOpenId!=null&&acctByTel==null) {//一个存在update
//			acctByOpenId.setTel(mobile);
//			acctRepository.save(acctByOpenId);
//			acctId=acctByOpenId.getId();
//
//		}else if(acctByOpenId==null&&acctByTel!=null) {//一个存在update
//			acctByTel.setOpenId(wechatId);
//			acctRepository.save(acctByTel);
//			acctId=acctByTel.getId();
//
//		}else {//均存在，校验一致性
//			//从pc端注册的无openId,需update
//			if(StringUtils.isBlank(acctByTel.getOpenId())) {
//				acctByTel.setOpenId(wechatId);
//				acctRepository.save(acctByTel);
//
//			}else {
//				if(!(acctByOpenId.getTel().equals(mobile)&&wechatId.equals(acctByTel.getOpenId()))) {
//					//return ReturnCode.ERROR_RESOURCE_EXIST_CODE;
//					map.put("returnCode", ReturnCode.ERROR_RESOURCE_EXIST_CODE);
//					return map;
//				}
//			}
//
//				acctId=acctByTel.getId();
//
//		}
//		//存放微信头像
//		User user = userRepository.findOneByAcctId(acctId);
//		user.setImage(wechatRegistryMobileDTO.getImg());
//		user.setNickName(wechatRegistryMobileDTO.getNickName());
//		userRepository.save(user);
//
//		map.put("returnCode", ReturnCode.CREATE_SUCCESS);
//		map.put("tel",mobile );
//		return map;
//	}

}
