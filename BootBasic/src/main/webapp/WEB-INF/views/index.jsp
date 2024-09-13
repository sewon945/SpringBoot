<%@page import="com.example.demo.model.BootMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- jquery 쓸 수 있게 가져와주기(jquery cdm) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>

	<button onclick="getAllMembers()">전체 회원 정보(비동기 방식-JS)</button>
	<p></p>
	<table border=1 id="list">
	
	</table>
	
	<!-- 수정 (순서 1) -->
	<form id="frm2">
							<!-- 구별할 수 있게 id 적어주기 -->
		<input type="hidden" name="uid" id="fuid">   <!-- 사용자 눈에는 안 보이게 해놓기 -->
		아이디 : <input type="text" name="id" id="fid" readonly><br>  <!-- readonly : 수정 못하게끔 -->
		비밀번호 : <input type="password" name="pw" id="fpw"><br>
		닉네임 : <input type="text" name="nick" id="fnick"><br>
		<input type="button" onclick="update()" value="정보수정">
		<p></p>
	</form>
	
	
	<!-- login 순서 1 -->
	<form action="login" method="post">
		아이디 : <input type="text" name="id"><br>
		비밀번호 : <input type="password" name="pw"><br>
		<input type="submit" value="로그인">	
		<p></p>
	</form>
	
	<!-- login 순서 6 -->
	<!-- 닉네임 출력 -->
	<% BootMember member = (BootMember)session.getAttribute("member"); %>
	<% if(member!=null) {%>
		<%=member.getNick() %>
		<!-- 채팅룸 -->
		<button onclick="location.href='room'">채팅룸 페이지 이동</button>
	<%} %>
<p></p>
	<!-- join 순서 1 -->
	<!-- 경로가 실행 됐으면 하는 경우에는 action에 슬래시 빼고 경로 적어주기('여기로부터join'이기 때문에 슬래시 없어도 됨) -->
	<form action="join" method="post">
		아이디 : <input type="text" name="id"><br>
		비밀번호 : <input type="password" name="pw"><br>
		닉네임 : <input type="text" name="nick"><br>
		<input type="submit" value="회원가입(동기 방식)">	
		<p></p>
	</form>
	
	<!-- form 태그는 기본적으로 동기 방식
	비동기 방식으로 바꿔주기 위해서는 action 지우기, submit도 action을 실행 시켜주기 위한 것이므로 동기 방식임 -->
	<form id="frm">
		아이디 : <input type="text" name="id"><br>
		비밀번호 : <input type="password" name="pw"><br>
		닉네임 : <input type="text" name="nick"><br>
		<input type="button" onclick="joinAsync()" value="회원가입(비동기 방식)">	
		<p></p>
	</form>
	
	<script>
		function joinAsync() {
			// 해당 form 태그 안에 있는 input 태그들에 작성되어 있는 값 가져오기
			let frmData = $("#frm").serialize()
			// serialize : form 태그 객체 내용 한 번에 받기(사용자가 입력한 값)
			// (표준 URL 인코딩 표기법 -> get 요청 시에 사용하는 파라미터와 비슷하게 생김)
			console.log(frmData)
			// ex) id=bidonggi&pw=123&nick=%EB%B9%84%EB%8F%99%EA%B8%B0
			
			// 비동기 통신
			$.ajax({
				url : "joinasync", // action
				type : "post",     // method
				data : frmData,   // 요청 데이터($("#frm").serialize())
				success : function(data) {
					console.log(data)
				},
				error : function() {
					alert("오류 발생")
				}
			})
		}
		
		function getAllMembers() {
			// ajax 써주기
			$.ajax({   // 값 가지고 오기
				url : "member",   
				type : "get",
				success : printList,
				error : function() {
					alert("오류 발생")
				}
			})
		}
		
		// 회원 정보를 불러오면 실행될 함수(회원정보를 가지고 온 다음에 넘겨줘야 함)
		function printList(data) {  // data: 서버에서 가져온 회원 정보 리스트
			let html = ""
			
			for(i in data) {   // i : index(인덱스) - 회원정보 3개이면? 0, 1, 2
				html += "<tr>"
				
				html +="<td>" + data[i].id + "</td>"
				html +="<td>" + data[i].pw + "</td>"
				html +="<td>" + data[i].nick + "</td>"
				// 삭제 버튼 만들고 함수 호출 (순서 1)
				html += "<td><button onclick='deleteMember(" + data[i].uid + ")'>삭제</button></td>"
				// 수정 버튼 만들고 함수 호출 - 4가지값 다 넘기기 (순서 2)
				// data[i] 타입이 Object인 문자열 -> JSON 형태로 변환해서 보내줘야 함
				html += "<td><button onclick='updateMember(" + JSON.stringify(data[i]) + ")'>수정</button></td>"
				
				html += "<tr/>"
			}
		
			$("#list").html(html)   // 완성시킨 html 넣어주기
		}
		
		// 수정 함수 (순서 3)
		function updateMember(member) {
			console.log(member)
			$("#fuid").val(member.uid)   // val : value
			$("#fid").val(member.id)
			$("#fpw").val(member.pw)
			$("#fnick").val(member.nick)
		}
		
		// update 함수
		function update() {
			let frmData = $("#frm2").serialize()
			
			$.ajax({
				url : "update",
				type : "patch",
				data : frmData, // frm에 있는 값 보내기
				success : function(data) {
					console.log(data)
					// 수정 (순서 7)
					// 수정을 하고 나서 내 정보 박스를 다시 빈칸으로 만들어줌(수정한 정보가 공개되면 안되기 때문에)
					$("#fuid").val("") 
					$("#fid").val("")
					$("#fpw").val("")
					$("#fnick").val("")
					getAllMembers()
				},
				error : function() {
					alert("오류 발생")
				}
			})
		}
		
		// 삭제 함수 (순서 2)
		function deleteMember(uid) {
			// HTTP 요청 메서드(타입)    (SQL문)
			// GET     : READ   (Select) - 결과값이 나오는 시간이 빠르다 -> (데이터를 header 실음)
			// POST    : CREATE (Insert) -> (데이터를 body 실음)
			// DELETE  : DELETE (Delete) -> GET
			// PUT     : UPDATE (Update) -> 전체를 수정
			// PATCH   : UPDATE (Update) -> 일부를 수정 *(많이 사용함) -> POST(데이터를 body 실음)
			
			$.ajax({
				url :"delete/" + uid,   // patch - 1번을 삭제했다면 delete/1
				type : "delete",
				success : function(data) {  // 콜백 함수
					console.log(data)
					getAllMembers()   // 삭제 버튼을 누르면 새로고침을 하지 않아도 새로고침이 알아서 되면서 삭제가 되는 함수 위에서 가져옴
				},
				error : function() {
					alert("오류 발생")
				}
			})
		}
	</script>
</body>
</html>