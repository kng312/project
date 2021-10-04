package web.as.common;

import javax.mail.Authenticator;

import javax.mail.PasswordAuthentication;
 

// 구글 권한을 얻어오는 클래스.
public class MailAuth extends Authenticator{
    
    PasswordAuthentication pa;
    
    public MailAuth() 
    {
        String mail_id = Constant.MAIL_ID;
        String mail_pw = Constant.MAIL_PWD;
        
        //암호화를 해야하기 때문에 클래스로 생성
        pa = new PasswordAuthentication(mail_id, mail_pw);
    }
    
    public PasswordAuthentication getPasswordAuthentication() 
    {
        return pa;
    }

}
