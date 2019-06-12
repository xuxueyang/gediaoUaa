package uaa.web.rest.demo.signature;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.SignatureUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;
import static org.springframework.core.annotation.AnnotatedElementUtils.isAnnotated;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;


public class SignatureUtil {
    private static String DELIMETER = ",";
    private static String NOT_FOUND = "NOT_FOUND";

    /**
     * 生成所有注有 SignatureField属性 key=value的 拼接
     */
    public static String toSplice(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        if (isAnnotated(object.getClass(), Signature.class)) {
            Signature sg = findAnnotation(object.getClass(), Signature.class);
            switch (sg.sort()) {
                case Signature.ALPHA_SORT:
                    return alphaSignature(object);
                case Signature.ORDER_SORT:
                    return "";// TODO orderSignature(object);
                default:
                    return alphaSignature(object);
            }
        }
        return toString(object);
    }

    private static String alphaSignature(Object object) {
        StringBuilder result = new StringBuilder();
        Map<String, String> map = new TreeMap<>();
        for (Field field : getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(SignatureField.class)) {
                field.setAccessible(true);
                try {
                    if (isAnnotated(field.getType(), Signature.class)) {
                        if (!Objects.isNull(field.get(object))) {
                            map.put(field.getName(), toSplice(field.get(object)));
                        }
                    } else {
                        SignatureField sgf = field.getAnnotation(SignatureField.class);
                        if (StringUtils.isNotEmpty(sgf.customValue()) || !Objects.isNull(field.get(object))) {
                            map.put(StringUtils.isNotBlank(sgf.customName()) ? sgf.customName() : field.getName()
                                , StringUtils.isNotEmpty(sgf.customValue()) ? sgf.customValue() : toString(field.get(object)));
                        }
                    }
                } catch (Exception e) {
//                    LOGGER.error("签名拼接(alphaSignature)异常", e);
                }
            }
        }

        for (Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, String> entry = iterator.next();
            result.append(entry.getKey()).append("=").append(entry.getValue());
            if (iterator.hasNext()) {
                result.append(DELIMETER);
            }
        }
        return result.toString();
    }

    /**
     * 针对array, collection, simple property, map做处理
     */
    private static String toString(Object object) {
        Class<?> type = object.getClass();
        if (BeanUtils.isSimpleProperty(type)) {
            return object.toString();
        }
        if (type.isArray()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Array.getLength(object); ++i) {
                sb.append(toSplice(Array.get(object, i)));
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Collection.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<?> iterator = ((Collection<?>) object).iterator(); iterator.hasNext(); ) {
                sb.append(toSplice(iterator.next()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        if (ClassUtils.isAssignable(Map.class, type)) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<? extends Map.Entry<String, ?>> iterator = ((Map<String, ?>) object).entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, ?> entry = iterator.next();
                if (Objects.isNull(entry.getValue())) {
                    continue;
                }
                sb.append(entry.getKey()).append("=").append(toSplice(entry.getValue()));
                if (iterator.hasNext()) {
                    sb.append(DELIMETER);
                }
            }
            return sb.toString();
        }
        return NOT_FOUND;
    }
//    TODO 過濾
//    private SignatureHeaders generateSignatureHeaders(Signature signature, HttpServletRequest request) throws Exception {//NOSONAR
//        Map<String, Object> headerMap = Collections.list(request.getHeaderNames())
//            .stream()
//            .filter(headerName -> SignatureHeaders.HEADER_NAME_SET.contains(headerName))
//            .collect(Collectors.toMap(headerName -> headerName.replaceAll("-", "."), headerName -> request.getHeader(headerName)));
//        PropertySource propertySource = new MapPropertySource("signatureHeaders", headerMap);
//        SignatureHeaders signatureHeaders = RelaxedConfigurationBinder.with(SignatureHeaders.class)
//            .setPropertySources(propertySource)
//            .doBind();
//        Optional<String> result = ValidatorUtils.validateResultProcess(signatureHeaders);
//        if (result.isPresent()) {
//            throw new ServiceException("WMH5000", result.get());
//        }
//        //从配置中拿到appid对应的appsecret
//        String appSecret = limitConstants.getSignatureLimit().get(signatureHeaders.getAppid());
//        if (StringUtils.isBlank(appSecret)) {
//            LOGGER.error("未找到appId对应的appSecret, appId=" + signatureHeaders.getAppid());
//            throw new ServiceException("WMH5002");
//        }
//
//        //其他合法性校验
//        Long now = System.currentTimeMillis();
//        Long requestTimestamp = Long.parseLong(signatureHeaders.getTimestamp());
//        if ((now - requestTimestamp) > EXPIRE_TIME) {
//            String errMsg = "请求时间超过规定范围时间10分钟, signature=" + signatureHeaders.getSignature();
//            LOGGER.error(errMsg);
//            throw new ServiceException("WMH5000", errMsg);
//        }
//        String nonce = signatureHeaders.getNonce();
//        if (nonce.length() < 10) {
//            String errMsg = "随机串nonce长度最少为10位, nonce=" + nonce;
//            LOGGER.error(errMsg);
//            throw new ServiceException("WMH5000", errMsg);
//        }
//        if (!signature.resubmit()) {
//            String existNonce = redisCacheService.getString(nonce);
//            if (StringUtils.isBlank(existNonce)) {
//                redisCacheService.setString(nonce, nonce);
//                redisCacheService.expire(nonce, (int) TimeUnit.MILLISECONDS.toSeconds(RESUBMIT_DURATION));
//            } else {
//                String errMsg = "不允许重复请求, nonce=" + nonce;
//                LOGGER.error(errMsg);
//                throw new ServiceException("WMH5000", errMsg);
//            }
//        }
//    　　 //设置appsecret
//        signatureHeaders.setAppsecret(appSecret);
//        return signatureHeaders;
//    }

    private List<String> generateAllSplice(Method method, Object[] args, String headersToSplice) {
        List<String> pathVariables = Lists.newArrayList(), requestParams = Lists.newArrayList();
        String beanParams = StringUtils.EMPTY;
        for (int i = 0; i < method.getParameterCount(); ++i) {
            MethodParameter mp = new MethodParameter(method, i);
            boolean findSignature = false;
            for (Annotation anno : mp.getParameterAnnotations()) {
                if (anno instanceof PathVariable) {
                    if (!Objects.isNull(args[i])) {
                        pathVariables.add(args[i].toString());
                    }
                    findSignature = true;
                } else if (anno instanceof RequestParam) {
                    RequestParam rp = (RequestParam) anno;
                    String name = mp.getParameterName();
                    if (StringUtils.isNotBlank(rp.name())) {
                        name = rp.name();
                    }
                    if (!Objects.isNull(args[i])) {
                        List<String> values = Lists.newArrayList();
                        if (args[i].getClass().isArray()) {
                            //数组
                            for (int j = 0; j < Array.getLength(args[i]); ++j) {
                                values.add(Array.get(args[i], j).toString());
                            }
                        } else if (ClassUtils.isAssignable(Collection.class, args[i].getClass())) {
                            //集合
                            for (Object o : (Collection<?>) args[i]) {
                                values.add(o.toString());
                            }
                        } else {
                            //单个值
                            values.add(args[i].toString());
                        }
                        values.sort(Comparator.naturalOrder());
                        requestParams.add(name + "=" + StringUtils.join(values));
                    }
                    findSignature = true;
                } else if (anno instanceof RequestBody || anno instanceof ModelAttribute) {
                    beanParams = SignatureUtil.toSplice(args[i]);
                    findSignature = true;
                }

                if (findSignature) {
                    break;
                }
            }
            if (!findSignature) {
    //            LOGGER.info(String.format("签名未识别的注解, method=%s, parameter=%s, annotations=%s", method.getName(), mp.getParameterName(), StringUtils.join(mp.getMethodAnnotations())));
            }
        }
        List<String> toSplices = Lists.newArrayList();
        toSplices.add(headersToSplice);
        toSplices.addAll(pathVariables);
        requestParams.sort(Comparator.naturalOrder());
        toSplices.addAll(requestParams);
        toSplices.add(beanParams);
        return toSplices;
    }
//    SignatureUtils.signature(allSplice.toArray(new String[]{}), signatureHeaders.getAppsecret());
    static {
//    //初始化请求头信息
//        SignatureHeaders signatureHeaders = new SignatureHeaders();
//        signatureHeaders.setAppid("111");
//        signatureHeaders.setAppsecret("222");
//        signatureHeaders.setNonce(SignatureUtil.generateNonce());
//        signatureHeaders.setTimestamp(String.valueOf(System.currentTimeMillis()));
//        List<String> pathParams = new ArrayList<>();
//    //初始化path中的数据
//        pathParams.add(SignatureUtil.encode("18237172801", signatureHeaders.getAppsecret()));
//    //调用签名工具生成签名
//        signatureHeaders.setSignature(SignatureUtil.signature(signatureHeaders, pathParams, null, null));
//        System.out.println("签名数据: " + signatureHeaders);
//        System.out.println("请求数据: " + pathParams);
    }
    {
//        拼接结果: appid=111^_^appsecret=222^_^nonce=c9e778ba668c8f6fedf35634eb493af6304d54392d990262d9e7c1960b475b67^_^timestamp=1538207443910^_^w8rAwcXDxcDKwsM=^_^
//        签名数据: SignatureHeaders{appid=111, appsecret=222, timestamp=1538207443910, nonce=c9e778ba668c8f6fedf35634eb493af6304d54392d990262d9e7c1960b475b67, signature=0a7d0b5e802eb5e52ac0cfcd6311b0faba6e2503a9a8d1e2364b38617877574d}
//        请求数据: [w8rAwcXDxcDKwsM=]
    }
}
