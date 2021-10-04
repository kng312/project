package web.as.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import web.as.common.MailForm;
import web.as.common.MailSend;
import web.as.common.SessionUtil;
import web.as.service.AsinfoService;
import web.as.service.MemberService;
import web.as.vo.MailMstVO;
import web.as.vo.asinfoVO;
import web.as.vo.memberVO;

//해당 클래스가 Controller임을 나타내기 위한 어노테이션
@Controller
public class ViewController {

	//생성자로 di를 하는것이 아니고 어노테이션으로 만든다.
	//하나의 빈만 찾을 수 있어서 사용시 주의해야한다.
	@Autowired
	MemberService service;
	
	@Autowired
	AsinfoService asservice;
	
	
	//요청에 대해 어떤 Controller, 어떤 메소드가 처리할지를 맵핑하기 위한 어노테이션
	@RequestMapping("/")
	public String main(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception
	{
		// 사용자 세션.
		// 요청이 왔을때 membervo로 데이터를 받는다.
		memberVO membervo = (memberVO)SessionUtil.getAttribute("member");
		
		//데이터가 없으면 login 보낸다 
		if(membervo == null ) 
		{
			return "login";		//로그인 세션 체크
		}
		//있으면 인덱스로 보낸다.
		else 
		{
			return "index";
		}
	
	}
	
	//DB에 정보가 없다면 /member/login으로 들어온다.
	
	@ResponseBody	//view를 생성하지않고 트루 펄스만 보내줄때 사용, ajax통신 으로 받았을경우는 사용
	@RequestMapping("/member/login")
	public boolean memberlogin(memberVO vo, HttpServletRequest req, HttpServletResponse res, Model model) throws Exception
	{
		//value object 로 데이터를 확인
		//쿼리를 날려서 사용자가 존재하는지 확인
		//1. service는 쿼리를 처리하는 인터페이스다. 2. service는 실제구현체를 new로 할당한 적이 없다. 3. 할당은 스프링 프레임워크에서 DI해준다.
		//3-1 : 스프링프레임워크에서 DIP의 원리에 의해 @Service, @Autowired를 이용하여 구현한다.
		//3-2 : Bean 3개의 서로다른 컨테이너로 Bean을 저장, 이를 생명주기로 관리한다.	-서블릿 컨테이너 / 루트 컨테이너 / 외부컨테이너
		vo = service.selectMemberOne(vo);
			
		if(vo!=null) {
			// TODO : vo를 세션에 등록해줌.
			SessionUtil.setAttribute("member", vo);
			
			return true;	// 로그인 완료
		}else {
			return false;	//로그인 실패
		}
		
	}
	
	@RequestMapping("/findId")
	public String findId(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception
	{
		return "findId";
	}
	
	@RequestMapping("/index")
	public String index(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception{
		
		return "index";
	}

	@RequestMapping("/register")
	public String register(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception{
		
		return "register";
	}
	
	@ResponseBody 
	@RequestMapping("/member/join")
	//@ModelAttribute 날라오는 vo와 쿼리데이터의 벨류가 같으니 알아서 정리를 해줘라
	public boolean join(@ModelAttribute("memberVO") memberVO vo ) throws Exception{
		
		int cnt = service.selectMemberCount(vo);
		
		//카운트가 0보다 크면 이미 회원가입이 되어있는 경우
		if (cnt>0) 
		{
			return false;
		}
		//아닐경우 정보를 insert
		else 
		{
			service.insertMember(vo);
		}
		
		return true;
	}
	
	@ResponseBody 
	@RequestMapping("/member/find")
	//아이디 찾기
	public boolean find(memberVO vo, HttpServletRequest req, HttpServletResponse res, Model model ) throws Exception{
		
		//조회를 해본다.
		 vo = service.findMemberOne(vo);
		
		if (vo != null) 
		{
			//ID가 있으면 true
			return true;
		}
		else 
		{
			return false;
		}

	}
	
	@ResponseBody
	@RequestMapping("/asinfo/search")
	public List<asinfoVO> getList(@ModelAttribute("asinfoVO") asinfoVO vo) throws Exception{
		
		//List에 as정보를 저장
		List<asinfoVO> list = asservice.selectAsInfoList(vo) ;
		
		return list;
		
	}
	
	@ResponseBody
	@RequestMapping("/asinfo/view")
	public asinfoVO getView(@ModelAttribute("asinfoVO") asinfoVO vo) throws Exception{
		
		vo = asservice.selectAsInfo(vo) ;
		
		return vo;
		
	}
	
	
	
	@ResponseBody
	@RequestMapping("/asinfo/save")
	public boolean save(@ModelAttribute("asinfoVO") asinfoVO vo) throws Exception{
		int c=0;
		
		memberVO membervo = (memberVO)SessionUtil.getAttribute("member");
		
		vo.setId(membervo.getId());
		
		if(vo.getNum()!=null && !"".equals(vo.getNum()) ) {   //update
			c = asservice.updateAsinfo(vo);
			
		} else {  //insert
			c = asservice.insertAsinfo(vo);
			
		}
		
		//1.메일 자체에 대한 내용을 만든다.
		//2.SMTP설정 정보를 만든다.
		//메일 발송.
		if(vo.getMailYn()!= null && "Y".equals(vo.getMailYn())) {
			
			
			// MailMstVO : 메일내용을 제외한 메일을 전송하기 위한 자체정보, 수신자/송신자 정보
			MailMstVO mailvo = new MailMstVO();
	         
	         mailvo.setTitle("AS 결과 메일 입니다.");      //제목
	         mailvo.setContent(vo.getAnswer());       //내용
	         //==============================================================
	         
	         //MailForm : 메일 내용
	         MailForm mform = new MailForm();
	         mailvo.setContent(mform.getHtml(mailvo));
	         //==============================================================

	         
	         String[] recipients = new String[1];		//수신자 메일
	         
	         //수신자 정보를 넣어준다.
	         recipients[0] = vo.getEmail();
	         
	         mailvo.setRecipients(recipients);
	         
	         //=============================================================
	         //MailSend : 메일 보내기
	         MailSend mail = new MailSend();
	         mail.send(mailvo);
			
		}
		
		if(c>0) {
			return true;
		}else {
			
			return false;
		}
		
		
		
	}
	
	@ResponseBody
	@RequestMapping("/asinfo/delete")
	public boolean delete(@ModelAttribute("asinfoVO") asinfoVO vo) throws Exception{
		int	c = asservice.deleteAsinfo(vo);
		
		if(c>0) {
			return true;
		}else {
			
			return false;
		}
		
		
		
	}
}
