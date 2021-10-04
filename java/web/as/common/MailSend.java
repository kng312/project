package web.as.common;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import web.as.vo.MailMstVO;




public class MailSend { 
	static Properties mailServerProperties; 
	static Session getMailSession; 
	static MimeMessage generateMailMessage; 
	
	public void send(MailMstVO vo) throws Exception
	{
        String[] emailList = vo.getRecipients();			// 메일 받는사람 리스트 
        String emailMsgTxt = vo.getContent(); 				// 내용
        String emailSubjectTxt = vo.getTitle();				// 제목
        
        // 메일보내기 
        postMail(emailList, emailSubjectTxt, emailMsgTxt);
	}


	//SMTP 프로토콜 : 메일 서버끼리 통신하기 위한 프로토콜 -> 웹통신이랑 다름
	private void postMail(String recipients[], String subject, String msgText) throws MessagingException, UnsupportedEncodingException, Exception {
		//시스템 정보를 가져온다.
		Properties prop = System.getProperties();
		
		//메일을 보내기 위한 로컬호스트 서버 인증 정보
		//로컬호스트에서 지메일 메일서버에 보내기 위한 추가 정보
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587");
        
        
        //메일을 보내기위한 로컬호스트 보안 정보를 가져온다.
        Authenticator auth = new MailAuth();		//구글 인증 가져오기
        
        //세션은 지메일 서버와 통신하기 위한 정보를 들고있다.
        Session session = Session.getDefaultInstance(prop, auth);		//메일을 송수신하기 위한 세션 생성
        
        //메일 내용을 보내기 위한 코드
        //메일을 보내는 방법 중 가장 표준적인(RFC822) 방법
        MimeMessage msg = new MimeMessage(session);	//메일 발송 메시지 클래스
    
        try {
        	
            msg.setSentDate(new Date());	//보내는 날짜
            
            //InternetAddress : 내가 보내려고 하는 주소를 RFC822형태로 바꿔준다.
            InternetAddress[] to = new InternetAddress[recipients.length];	//받는 사람 목록
            
            msg.setFrom(new InternetAddress(Constant.MAIL_SENDER, Constant.MAIL_SENDER_NM));
            
            //메일을 받는 사람만큼 돈다.
            for (int i = 0; i < recipients.length; i++) {
            	if(recipients[i]!=null && !"".equals(recipients[i])) {
            		to[i] = new InternetAddress(recipients[i]);
            	}
	        }
            
	        msg.setRecipients(Message.RecipientType.TO, to);
	        
            //제목 인코딩 정보            
            msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
            
            //내용
            MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(msgText, "text/html;charset=utf-8");

	        //creates multi-part
	        Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			
	        // sets the multi-part as e-mail's content
	        msg.setContent(multipart);
	        
            Transport.send(msg);
            
        } catch(AddressException ae) {            
            System.out.println("AddressException : " + ae.getMessage());           
        } catch(MessagingException me) {            
            System.out.println("MessagingException : " + me.getMessage());
        } catch(UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException : " + e.getMessage());			
        }
                
    }
	
}

