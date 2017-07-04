package com.workmanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

public class UtilTools {

	private static Logger log = LoggerFactory.getLogger(UtilTools.class);
	private static final String PKCS12 = "PKCS12";
	private static final String SHA1WithRSA = "SHA1WithRSA";
	private static final String CHARSET = "utf-8";

	/**
	 * generate the signature
	 * 
	 * @param source
	 * @param pfxPath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String generateSignature(String source, String pfxPath,
			String password) throws Exception {
		byte[] signature = null;
		PrivateKey privateKey = getPrivateKeyInstance(pfxPath, password);
		Signature sig = Signature.getInstance(SHA1WithRSA);
		sig.initSign(privateKey);
		sig.update(source.getBytes("UTF-8"));
		signature = sig.sign();
		log.info("-signature=[{}]", base64Encode(signature));
		return base64Encode(signature);
	}
	
	public static void main(String[] args) throws Exception {
		
//		String s = "";
//		String p = "";
//		String pa = "";
//		System.out.println(generateSignature(s,p,pa));
		testSDK();
	}
	
	/**
	 * check the signature
	 * 
	 * @param datasource
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSignature(String datasource, String sign,
			String certificatePath) throws Exception {
		try {
			X509Certificate x509Certificate = (X509Certificate) getInstance(certificatePath);
			Signature signature = Signature.getInstance(SHA1WithRSA);
			signature.initVerify(x509Certificate);
			signature.update(datasource.getBytes(CHARSET));
			return signature.verify(base64Decoder(sign));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 加载私钥
	 * 
	 * @param strPfx
	 * @param strPassword
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static PrivateKey getPrivateKeyInstance(String strPfx,
			String strPassword) {
		try {
			KeyStore ks = KeyStore.getInstance(PKCS12);
			FileInputStream fis = new FileInputStream(strPfx);
			char[] chars = null;
			if ((strPassword == null) || strPassword.trim().equals("")) {
				chars = null;
			} else {
				chars = strPassword.toCharArray();
			}
			ks.load(fis, chars);
			fis.close();
			log.debug("keystore type=" + ks.getType());
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements()) {
				keyAlias = (String) enumas.nextElement();
				log.debug("alias=[" + keyAlias + "]");
			}
			log.debug("is key entry={}", ks.isKeyEntry(keyAlias));
			return (PrivateKey) ks.getKey(keyAlias, chars);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获得证书
	 * 
	 * @param certificatePath
	 * @return
	 */
	private static Certificate getInstance(String certificatePath)
			throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream(certificatePath);
			CertificateFactory certificateFactory = CertificateFactory
					.getInstance("X.509");
			return certificateFactory.generateCertificate(is);
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return null;
	}

	private static void testSDK() throws Exception {
		String source = "versionId=3&merchantId=00000000000345&orderId=23423423&orderAmount=20&orderDate=2016-08-01&orderOverDate=&currency=MOP&transType=0101&retUrl=http://120.25.75.119:8080/omgshop/PayResultServlet&bizType=00&returnUrl=http://120.25.75.119:8080/omgshop/PayResultServlet&prdDisUrl=http://120.25.75.119:8080/omgshop/index.jsp&prdName=测试商品购买流程&prdShortName=&prdDesc=包括的商品有以下列表............等....&merRemark=&rptType=&prdUnitPrice=&buyCount=1&defPayWay=&buyMobNo=&cpsFlg=0&merno=00000000000346&tokenNo=&signType=CFCA";
		String pfxPath = "D:\\macaupasstest.pfx";
		String password = "12345678";
		//String sign = "l82eR3tqTUH5jVNDe2KJGFnN7Sr4ako1Tuvf6f9q8DSpPWDs7zWA1iIp4erQyQVRXzu3b/1ZZsxXDYzlZkZjfFRC565/xDBT1mkqdCbWlYPdiZkPXtCekL6yZLanHIFji+i30pT9otFVeujp/iKVXwlH1rHmDt4f6O463V3CqcbOR32lHY2QSWdVt+w980gYGiVA8LXbDCbqFZLb6SL6Afs0ail6Guqf49ieVtGAzgYAU4HBBf8p07hfXS4EqarGk3bk+zMcqJ6/BovluId2bmH5LvGvUcG99Uz6FGC5vMPl5jyS73NFcZ3Xksf7jdJd950afFtm/57OO8rvpmKLIg==";
		String sign = UtilTools.generateSignature(source, pfxPath, password);
		System.out.println(sign);
		
//		String certificatePath = "F:\\在線支付平臺發佈\\商戶接入\\商戶測試\\00000000000334.cer";
//		boolean a = UtilTools.checkSignature(source, sign, certificatePath);
		//System.out.println("验证签名=" + a);
	}

	private static void testActivity() throws Exception,
			UnsupportedEncodingException {
		String source = "versionId=3&merchantId=00000000000001&productNumber=01&orderId=1460199901322913_154824&bankCode=05010000&orderAmount=37300&orderDate=2016-04-24&orderOverDate=&currency=MOP&transType=0102&retUrl=http://open.macaumarket.com/api/member/payCallBack&bizType=00&returnUrl=http://pay.macaumarket.com/backPayAmt&errorPage=null&prdDisUrl=http://www.macaumarket.com&prdName=5B3334353730373035333035395D&prdShortName=&prdDesc=5B3334353730373035333035395D&merRemark=E8A5BFE6B48BE8A197E789B9E8B3A3&rptType=2&prdUnitPrice=&buyCount=&defPayWay=&buyMobNo=&cpsFlg=0&merno=00000000000002&signType=CFCA";
		String pfxPath = "F:\\澳門通錢包\\發佈\\156up\\cert\\activity2.pfx";//"/home/tdpay/apache-tomcat-6.0.43/webapps/tdrmp/00001243.pfx";
		String password = "hh123456";//"hkrt123";
		String generateSignature = UtilTools.generateSignature(source, pfxPath, password);
		System.out.println("====" + URLEncoder.encode(generateSignature,"utf-8"));
		String sign = "Q9TYKxOkgHcRcOMMER+EJhvR+QobFxQWou1nkoqtFCrnwz5Uueh8AOPnmW2qGUlvnIYj1Xi+cygTRsY+7GBZblUqfL10L1KEyIOeUyiI0jLQ1dg9YMoG9ZxreVKYM5a6LwoRjGIIZ+AunddV+/8r3tzSV6yth4El4I9cKVd7xJebx/H9X9bGN8+okoqtCKcXi8HMxdjMbDLTkUmR+4Tj3qffbtE0CzwG1koLyYylaFr9Og/+/2tPcqV7RZW1nw4D8E2qeloIAKKhGsMwI32Wc9MrDak9DsXerwMrxQbuunJIKkRWCKy/oeNJT6Fc6FgW+de72rHZKNDMGJ8OyoMDsw==";
		String certificatePath = "F:\\澳門通錢包\\生產備份\\00000000000001.cer";//"F:\\澳門通錢包\\生產包\\20151214\\cert\\00000000000001.cer";
		boolean a = UtilTools.checkSignature(source, sign, certificatePath);
		System.out.println("验证签名=" + a);
	}
	
	public static String byteToHexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			a = a + hex;
		}

		return a;
	}
	
	private static byte[] c(String context) {
		int len = context.length() / 2;
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++) {
			b[i] = (byte) Integer.parseInt(context.substring(i * 2, i * 2 + 2),
					16);
		}

		return b;
	}

	private static void createPublicKeyObject(PublicKey publicKey)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(publicKey);
		byte[] str = baos.toByteArray();
	}
	
	private static String base64Encode(byte[] b){
		return Base64.getEncoder().encodeToString(b);
	}
	private static byte[] base64Decoder(String s){
		return Base64.getDecoder().decode(s);
	}
}
