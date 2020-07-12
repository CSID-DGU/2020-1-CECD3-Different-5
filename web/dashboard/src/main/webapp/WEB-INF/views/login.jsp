<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@taglib  prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %><html>
<head>
	<title>login</title>

	<style>
	
		div.header{ text-align:center;	
				    position:relative;}
		
		div#loginWindow{ background-color:#76BBB6;
						 width:450px;
						 height:155px;
						 left:30%;
				         position:relative; 
				         margin-top:100px;
				         margin-bottom:150px;
				         padding-top:20px;
				         padding-bottom:20px;}
	
		p#title{ font-weight:bold;
		 		 font-size:25px; }
		 		 
		p.detalis{ color:gray;
				   font-size:12px;
				   }
				   
		p.login{ color:white;
				 font-size:20px;
				 font-weight:bold;
				 margin-bottom:1px;
				 margin-left:30px;}
		
		hr.out{ background-color:#28948D;
				border:0;
				height:5px;
				width:75%;}	
			
		hr.in{ background-color:#ffffff;
			   border:0;
			   height:2px;
			   width:95%;}
		
		button{ align:left; 
					  background-color:#2F504E;
					  color:white;
					  border:0;
					  outline:0;
					  width:80px;
					  height:20px;
					  font-size:10px;
					  align:right;
					  margin-top:-8px;}
					  
		input.login{ font-size:10px;
					 background-color:#2F504E;
					 color:white;
					 border:0;
					 outline:0; }
					 
		a{ text-decoration:none;
		   color:white; }
	</style>
</head>

<body>

<div class="header">
	<img src="<spring:url value='/resources/img/etoos.png'/>" width="200px" style="margin-top:30px;" >
	<p id="title"/>학생관리시스템
</div>

	<hr class="out"/>
	
	<div id="loginWindow">
	
		<hr class="in"/>
		
		<form>
		<p class="login"/>&nbsp;&nbsp;ID : <input id="uid" type="text" border="0" outline="0"/><br>
		<p class="login"/>PW : <input id="pwd" type="password" border="0" outline="0"/><br>
		
		<input class="login" type="submit" value="로그인"></input>
		
		</form>
		<hr class="in"/>
		<button><a href="search">회원가입</a></button>
		<button><a href="search">비밀번호 찾기</a></button>
		
		
	</div>
	
	<hr class="out"/>	
	
	<div class="footer">
		<p><img src="<spring:url value='/resources/img/etoos.png'/>" width="100px" top="10px">
		<p class="detalis"/>이투스교육 주식회사 서울시 서초구 남부순환로 2547 (서초동 1354-3) 사업자등록번호 220-85-32141
		<p class="detalis"/>Fax 02-543-2153 고객센터 1599-6405
		<p class="detalis"/>Copyright © ETOOS All right reserved.
	</div>
</body>
</html>
