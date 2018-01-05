package com.xdk.develop.df.teacherpart.util;

import java.util.regex.Pattern;

public class ImportCsvValidate {
    /**  
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））  
     * @param mobile 移动、联通、电信运营商的号码段  
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）  
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>  
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>  
     *<p>电信的号段：133、153、180（未启用）、189</p>  
     * @return 验证成功返回true，验证失败返回false  
     */    
    public static boolean isMobile(String mobile){
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }  
    /** 
     * 区号+座机号码+分机号码 
     * @param fixedPhone 
     * @return 
     */  
    public static boolean isFixedPhone(String fixedPhone){
        String reg="(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";  
        return Pattern.matches(reg, fixedPhone);
    }  
    /**  
     * 匹配中国邮政编码  
     * @param邮政编码
     * @return 验证成功返回true，验证失败返回false  
     */   
    public static boolean isPostCode(String postCode){
        String reg = "[1-9]\\d{5}";
        return Pattern.matches(reg, postCode);
    }  
      
      
      
    public static void main(String[] args) {
        String mobile = "18600000000";
        boolean ret = isMobile(mobile);  
        System.out.println(ret);
          
        String postCode = "110200";
        ret = isPostCode(postCode);  
        System.out.println(ret);
          
        String isFixedPhone = "0571-8888880-111";
        ret = isFixedPhone(isFixedPhone);  
        System.out.println(ret);
          
    }  
  
}  