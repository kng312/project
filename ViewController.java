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

import web.as.common.SessionUtil;
import web.as.service.AsinfoService;
import web.as.service.MemberService;
import web.as.vo.asinfoVO;
import web.as.vo.memberVO;

@Controller
public class ViewController {

	//생성자로 di를 하는것이 아니고 어노테이션으로 만든다.
	//하나의 빈만 찾을 수 있어서 사용시 주의해야한다.
	@Autowired
	MemberService service;
	
	@Autowired
	AsinfoService asservice;
	
	@RequestMapping("/")
	public String main(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception{
		
		// 사용자 세션.
		// 요청이 왔을때 membervo로 데이터를 받는다.
		memberVO membervo = (memberVO)SessionUtil.getAttribute("member");
		
		//데이터가 없으면 login 보낸다 
		if(membervo == null ) {	//로그인 세션 체크
			return "login";
		}
		//있으면 인덱스로 보낸다.
		else {
			return "index";
		}
	}
	//login.jsp로 가서 아이디 패스워드를 확인하고 함수가 실행됨
	//DB에 정보가 없다면 /member/login으로 들어온다.

	@ResponseBody//view를 생성하지않고 트루 펄스만 보내줄때 사용, ajax통신 으로 받았을경우는 사용

	@RequestMapping("/member/login")
	public boolean memberlogin(memberVO vo, HttpServletRequest req, HttpServletResponse res, Model model) throws Exception{
		
		//value object 로 데이터를 확인
		//쿼리를 날려서 사용자가 존재하는지 확인
				//1.service는 쿼리를 처리하는 코드 2. service는 인터페이스다 3. service는 실제 구현체를 할당한 적이 없다.
		//1. service는 쿼리를 처리하는 인터페이스다. 2. service는 실제구현체를 new로 할당한 적이 없다.
		//3. 할당은 스프링 프레임워크에서 DI해준다.
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
		
		if (cnt>0) 
		{
			return false;
		}
		else 
		{
			service.insertMember(vo);
		}
		
		return true;
	}
	
	@RequestMapping("/member/getList")
	@ResponseBody
	public List<asinfoVO> getList(@ModelAttribute("asinfoVO") asinfoVO vo) throws Exception{
		
		List<asinfoVO> list = asservice.selectAsInfoList(vo) ;
		
		return list;
		
	}
	 
	
	
	
	
}
