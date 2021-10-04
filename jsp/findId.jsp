<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>회원 가입</title>


    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">


    <link href="css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body class="bg-gradient-primary">

    <div class="container">

        <div class="card o-hidden border-0 shadow-lg my-5">
            <div class="card-body p-1">
                
                <div class="row">
                    
                    <div class="col-lg-8">
                        <div class="p-5">
                            <div class="text-center">
                                <h1 class="h4 text-gray-900 mb-4">아이디 찾기</h1>
                            </div>
                            <form id='ff' name='ff'>
                                <div class="form-group row">
                                    <div class="col-sm-12 mb-3 mb-sm-0">
                                        <input type="text" class="form-control form-control-user" id="id" name="id"
                                            placeholder="아이디">

                                </div>

                                </div>
                                <a href="#" onclick="findId()" class="btn btn-primary btn-user btn-block">
                                    아이디 찾기
                                </a>
                                
                                <a href="#" onclick="cancel()" class="btn btn-primary btn-user btn-block">
                                    취소
                                </a>

                            </form>
                            <hr>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

    
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    
    <script src="js/sb-admin-2.min.js"></script>

	<script>
	
		function findId(){
			if($('#id').val()==''){
				alert("아이디를 입력하세요");
				return;
			}
			
			if($('#id').val() !=''){
				var formData = $("#ff").serialize();
					$.ajax({
							data :formData,
							type:"POST",
							url : "/member/find",
							cache : false,
							success : find_Handler
							});
			}
			
		}
		
		function find_Handler(data){
			if(data)
			{

				alert("아이디가 있습니다.");
				
			}
			else
			{
				alert(" 아이디가 없습니다.");
			}
		}
		
		function cancel()
		{
			location.href="/";
		}
		
	
	</script>

</body>

</html>