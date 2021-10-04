<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="kr">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>AS 관리- Login</title>

    <!-- Custom fonts for this template-->
    <link href="/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="/css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body class="bg-gradient-primary">

    <div class="container">

        <!-- Outer Row -->
        <div class="row justify-content-center">

            <div class="col-xl-10 col-lg-12 col-md-9">

                <div class="card o-hidden border-0 shadow-lg my-5">
                    <div class="card-body p-0">
                        <!-- Nested Row within Card Body -->  
                        <div class="row">
                        	<!--부트스트랩 반응형으로 만듬  -->
                            <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                            <div class="col-lg-6">
                                <div class="p-5">
                                    <div class="text-center">
                                        <h1 class="h4 text-gray-900 mb-4">AS 관리 로그인</h1>
                                    </div>
                                    <!-- 아이디,비밀번호 입력하는 form -->
                                    <form id="ff" name="ff">
                                        <div class="form-group">
                                            <input type="text" id="id" name="id" class="form-control form-control-user"
                                                placeholder="아이디">
                                        </div>
                                        <div class="form-group">
                                            <input type="password" class="form-control form-control-user"
                                                id="passwd" name="passwd" placeholder="비밀번호">
                                        </div>
                                        <!-- 로그인 버튼 클릭시 login()으로 이동 -->
                                        <a href="#" onclick="login();" class="btn btn-primary btn-user btn-block">
                                            Login
                                        </a>
                                        <hr>
                                        
                                    </form>
                                    <hr>
                                    <!-- 회원가입 및 아이디 찾기로 가는 코드 -->
                                    <div class="text-center">
                                        <a  class="small" href="/register">회원가입</a>
                                    </div>
                                    
                                    <div class="text-center">
                                        <a  class="small" href="/findId">아이디 찾기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>

    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="/vendor/jquery/jquery.min.js"></script>
    <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="/vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="/js/sb-admin-2.min.js"></script>


<script>

	// 아이디, 패스워가 비어있을 경우 입력하는 창을 띄워줌
	function login(){
		
		if($('#id').val()==''){
			alert('아이디를 입력하세요');
			return;
		}
		
		if($('#passwd').val()==''){
			alert('패스워드를 입력하세요');
			return;
		}

		//ajax 통신을 위해 form 데이터를 serialize 한다.
		var formData = $("#ff").serialize();
		
		//비동기 통신 JSON으로 보냄
		$.ajax({
					data :formData,
					type:"POST",				//HTTP Method type 형식
					url : "/member/login",		//url 주소
					cache : false,
					success : login_Handler		//통신이 성공하면 실행
				});
		
		
	}
	
	function login_Handler(data)
	{
		if( data )
		{
			location.href="/index";
		}
		else
		{
			alert("존재하지 않는 아이디 또는 패스워드 입니다.");
		}
	}
	
</script>

</body>

</html>