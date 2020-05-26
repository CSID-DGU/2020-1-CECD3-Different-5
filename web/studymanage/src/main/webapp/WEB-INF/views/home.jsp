<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- https://kuzuro.blogspot.com/2018/04/blog-post_49.html : 한글 인코딩 -->
<html>
<head>
	<title>login</title>
</head>
<body>
<img src="image/logo.png"/><br/>
<p style="font-size:30px; font-weight:bold; font-family:Malgun Gothic; " align="center">학생관리시스템</p>
<hr style="width:90%; margin-bottom:50px;" color="#56ABA6" />
<form>
	<div style="background-color:#76BBB6; color:white; border-radius: 5px; width:500px;height:275px;margin:auto;font-family:Malgun Gothic; font-size:30px ;font-weight:bold;" >
		<p style="font-size:13px; color:#76BBB6;">d</p>
		<hr width="95%" color="white"/>
			<label style="margin-left:20px">ID&nbsp;</label>
			<input id="id" type="text" style="width:250px; height:30px; font-size:20px;"/><p style=""></p>
			<label style="margin-left:20px">PW</label>
			<input id="pw" type="password" style="width:250px;  height:30px; font-size:20px"/>
			<button type="submit" style="margin-top:-80px; margin-left:5px; width:140px;height:160px;background-color:#334B4A; color:white; border-radius: 5px; font-size:30px; font-weight:bold;">로그인 </button>
		<hr width="95%" color="white"/>
		<button type="button" style="background-color:#334B4A; color:white; margin-left:56% ">회원가입</button><!-- onclic="location.href='이동할 링크'" -->
		<button type="button" style="background-color:#334B4A; color:white; ">비밀번호 찾기</button>
	</div>
</form>
<hr width="90%" color="#56ABA6" float="center"/>
<img src="image/logo.png" style="position:absolute; bottom:0px;"/>
<p style="position:absolute; bottom:0px; width:600px; font-size:13px; color:#7F7F7F; font-family:Malgun Gothic;">이투스교육 주식회사 서울시 서초구 남부순환로 2547 (서초동 1354-3) 사업자등록번호 220-85-32141 Fax 02-543-2153 고객센터 1599-6405<br>Copyright © ETOOS All right reserved.</p>
</body>
</html>
