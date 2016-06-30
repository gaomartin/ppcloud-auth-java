package com.pplive.ppcloud.auth;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.pplive.ppcloud.constants.APPConstants;
import com.pplive.ppcloud.requestdto.AuthorizationRequestDO;
import com.pplive.ppcloud.utils.HmacSHA1Util;
import com.pplive.ppcloud.utils.JsonUtil;

public class Auth {
	
	private String accessKey;
	private String secretKey;

	public Auth() {
		
	}
	
	/**
	 * Constructor
	 * @param accessKey
	 * @param secretKey
	 */
	public Auth(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}
	
	/**
	 * generate token
	 * @return token
	 */
	public String generate() {
		return this.generate(this.accessKey, this.secretKey);
	}
	
	/**
	 * generate token
	 * @param accessKey
	 * @param secretKey
	 * @return token
	 * @throws Exception 
	 */
	public String generate(String accessKey, String secretKey) {
		if (StringUtils.isEmpty(accessKey) || StringUtils.isEmpty(secretKey)) {
			throw new IllegalArgumentException("empty accessKey or secretKey");
		}
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		
		Date expireDate = DateUtils.addMinutes(new Date(), 60);
		AuthorizationRequestDO authorizationRequestDO = new AuthorizationRequestDO();
		authorizationRequestDO.setRid(UUID.randomUUID().toString().replace("-", "").toLowerCase());
		authorizationRequestDO.setDeadline(expireDate.getTime());
		
		String json = JsonUtil.getJsonFromObject(authorizationRequestDO);
		System.out.println(String.format("json: %s", json));
		
		String encodeJsonBase64 = null;
		try {
			encodeJsonBase64 = new String(Base64.encodeBase64(json.getBytes(APPConstants.DEFAULT_CHARSET), false, true));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(String.format("system unsupport encoding %s", APPConstants.DEFAULT_CHARSET));
		}
		System.out.println(String.format("encode_json_base64: %s", encodeJsonBase64));
		
		byte[] sign = null;
		try {
			sign = HmacSHA1Util.getSignatureBytes(encodeJsonBase64, secretKey);
		} catch (Exception e) {
			throw new RuntimeException("sha1 sign error");
		}
		String encode_sign = new String(Base64.encodeBase64(sign, false, true));
		System.out.println(String.format("encode_sign: %s", encode_sign));
		
		String accessToken = String.format("%s:%s:%s", accessKey, encode_sign, encodeJsonBase64);
		System.out.println(String.format("token: %s", accessToken));
		
		return accessToken;
	}
}
