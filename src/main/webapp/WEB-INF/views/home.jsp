<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/css/start.css" rel="stylesheet" />
<link href="/css/header.css" rel="stylesheet" />
<style>
#main {
    width: 100%;
    height: 2000px;
    margin-top: 200px;

  }

  .landing .section1 {
    position: relative;
    width: 1240px;
    height: 920px;
    margin: 150px auto 0;
    padding-top: 45px;
  }

  .landing .section1.ui-ani-appear .txt {
    transform: translateX(0);
    opacity: 1;
  }

  .landing .section1 .txt {
    transform: translateX(50px);
    opacity: 0;
    transition: all 400ms 600ms ease-out;
  }

  .landing .section1 .txt {
    font-weight: 700;
    font-size: 50px;
    line-height: 70px;
    color: #2c2c2c;
  }

  .landing .section1 .img {
    position: absolute;
    top: 269px;
    width: 1240px;
    border-radius: 32px;
    overflow: hidden;
  }

  .landing img {
    width: 100%;
  }

  .landing .section1.ui-ani-appear .img2 {
    transform: translateX(0);
    opacity: 1;
  }

  .landing .section1 .img2 {
    transform: translateX(50px);
    opacity: 0;
    transition: all 800ms;
  }

  .landing .section1 .img2 {
    position: absolute;
    width: 1240px;
    top: 0;
  }
</style>
</head>
<body>
 
 <div id="wrap" class="active-start">
   <%@include file="/WEB-INF/include/Header.jsp"%>
   <main id="main" class="landing">
      <div class="section1 ui-ani-appear" data-intro-ani="" style="transition-delay: 0ms;">
        <div class="txt">다채로운<br>보험 생활의 시작</div> <!-- // 230629 수정 : 문구 -->
        <div class="img"><img src="/img/main_background01.jpg" alt=""></div>
        <div class="img2"><img src="/img/main_person.png" alt=""></div>
      </div>
   </main>
   
   
 </div><!-- wrap -->
 <!-- const naverEl= document.querySelector(".naverBtn");
   console.log(naverEl);
   <section id="main" class="landing">
   <div class="section1 ui-ani-appear" data-intro-ani="" style="transition-delay: 0ms;">
     <div class="txt">다채로운<br>보험 생활의 시작</div> // 230629 수정 : 문구
     <div class="img"><img src="/images/main_background01.jpg" alt=""></div>
     <div class="img2"><img src="/images/main_person.png" alt=""></div>
   </div> -->
   
 <script src="/js/header.js"></script>
 <script>
 function getCookie(name) {
     var value = "; " + document.cookie;
     var parts = value.split("; " + name + "=");
     if (parts.length === 2) return parts.pop().split(";").shift();
	 }
	
	 // Extract cookies
	 var accessCookie = getCookie("access");
	 var refreshCookie = getCookie("refresh");
	 var userIdCookie = getCookie("userId");
	
	 // Store tokens and userId in local storage
	 localStorage.setItem("access_token", accessCookie);
	 localStorage.setItem("refresh_token", refreshCookie);
	 localStorage.setItem("user_id", userIdCookie);
	
	 // Verify the stored values
	 console.log("Access token saved: " + localStorage.getItem("access_token"));
	 console.log("Refresh token saved: " + localStorage.getItem("refresh_token"));
	 console.log("User ID saved: " + localStorage.getItem("user_id"));
 </script>
 
</body>
</html>



