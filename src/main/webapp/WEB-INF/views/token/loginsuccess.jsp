<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>토큰 리다이렉션 jsp입니다.</h2>
<li id="goMypage"><a href="/Mypage/View">마이페이지이동</a>

<script>
//쿠키에서 값을 가져오는 함수
function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length === 2) return parts.pop().split(";").shift();
}

// 쿠키 값 가져오기
var accessCookie = getCookie("access");
var refreshCookie = getCookie("refresh");

// 로컬 스토리지에 저장
localStorage.setItem("access_token", accessCookie);
localStorage.setItem("refresh_token", refreshCookie);

// 저장된 값 확인 (예시로 콘솔에 출력)
console.log("Access token saved: " + localStorage.getItem("access_token"));
console.log("Refresh token saved: " + localStorage.getItem("refresh_token"));
</script>




</body>
</html>