package cn.com.burgerking.utils;

 

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;  
  
  
/**  
 * des加密解密  
 *   
 * @author  
 *   
 */  
public class DesEncryptAES {  

   static Key key;  
 
   public DesEncryptAES(String str) {  
       setKey(str);// 生成密匙  
   }  
 
   public DesEncryptAES() {  
       setKey("6Ta4OaHZdpA=");  
   }  
 
   /**  
    * 根据参数生成KEY  
    */  
   public void setKey(String strKey) {  
       try {  
           SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
           this.key  = keyFactory.generateSecret(new DESKeySpec(strKey.getBytes("UTF8")));  
       } catch (Exception e) {  
           throw new RuntimeException(  
                   "Error initializing SqlMap class. Cause: " + e);  
       }  
   }  
 
     
   /**  
    * 加密String明文输入,String密文输出  
    */  
   public static String encrypt(String strMing) {  
       byte[] byteMi = null;  
       byte[] byteMing = null;  
       String strMi = "";  
       BASE64Encoder base64en = new BASE64Encoder();  
       try {  
           byteMing = strMing.getBytes("UTF8");  
           byteMi = getEncCode(byteMing);  
           strMi = base64en.encode(byteMi);  
       } catch (Exception e) {  
           throw new RuntimeException(  
                   "Error initializing SqlMap class. Cause: " + e);  
       } finally {  
           base64en = null;  
           byteMing = null;  
           byteMi = null;  
       }  
       return strMi;  
   }  
 
   /**  
    * 解密 以String密文输入,String明文输出  
    *   
    * @param strMi  
    * @return  
    */  
   public static String decrypt(String strMi) {  
       BASE64Decoder base64De = new BASE64Decoder();  
       byte[] byteMing = null;  
       byte[] byteMi = null;  
       String strMing = "";  
       try {  
           byteMi = base64De.decodeBuffer(strMi);  
           byteMing = getDesCode(byteMi);  
           strMing = new String(byteMing, "UTF8");  
       } catch (Exception e) {  
           throw new RuntimeException(  
                   "Error initializing SqlMap class. Cause: " + e);  
       } finally {  
           base64De = null;  
           byteMing = null;  
           byteMi = null;  
       }  
       return strMing;  
   }  
 
   /**  
    * 加密以byte[]明文输入,byte[]密文输出  
    *   
     * @param byteS  
     * @return  
     */  
    private static byte[] getEncCode(byte[] byteS) {  
        byte[] byteFina = null;  
        Cipher cipher;  
        try {  
            cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.ENCRYPT_MODE, key,SecureRandom.getInstance("SHA1PRNG"));  
            byteFina = cipher.doFinal(byteS);  
        } catch (Exception e) {  
            throw new RuntimeException(  
                    "Error initializing SqlMap class. Cause: " + e);  
        } finally {  
            cipher = null;  
        }  
        return byteFina;  
    }  
  
    /**  
     * 解密以byte[]密文输入,以byte[]明文输出  
     *   
     * @param byteD  
     * @return  
     */  
    private static byte[] getDesCode(byte[] byteD) {  
        Cipher cipher;  
        byte[] byteFina = null;  
        try {  
            cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.DECRYPT_MODE, key,SecureRandom.getInstance("SHA1PRNG"));  
            byteFina = cipher.doFinal(byteD);  
        } catch (Exception e) {  
            throw new RuntimeException(  
                    "Error initializing SqlMap class. Cause: " + e);  
        } finally {  
            cipher = null;  
        }  
        return byteFina;  
    }  
  
      
  
    public static void main(String args[])  {  
        DesEncryptAES des = new DesEncryptAES();  
  
        String str1 = "{a:1,b:2,c:'dabd'}";  
        // DES加密  
        String str2 = des.encrypt(str1);  
        DesEncryptAES des1 = new DesEncryptAES();  
        System.out.println("密文:" + str2);  
        System.out.println("明文:" + des1.decrypt(str2)); 
        // DES解密  
        String deStr = des1.decrypt("7BJvK8J4lUAEaEeyAD0yo6KOH4ivcSZ4");  
        System.out.println("明文:" + deStr);  
        
    }  
  
}  
