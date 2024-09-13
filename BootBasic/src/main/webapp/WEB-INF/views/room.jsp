<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	<input type="text" id="room-name">
	<button onclick="createRoom()">채팅방 생성</button>
	<table id="rooms">
	
	</table>
	<script>
		
		$(document).ready(function() {
			showRooms()  // 현재 페이지가 로딩되면 바로 채팅방 불러오는 함수 호출
		})
		
		function showRooms() {
			$.ajax({
				url : "rooms",
				type : "get",
				success : roomsHtml,
				error : function() {
					alert("오류 발생")
				}
			})
		}
		
		
		// table 만들어 주는 함수
		function roomsHtml(data) {
			let html = ""
			for(i in data) {  // i에 저장되는 값 : 인덱스
				html += "<tr>"  // html에 누적시키기
				html += "<td>" + data[i].roomName + "</td>"
				// 버튼 누르면 페이지 자체가 바뀌게 경로 넣어주기 (\"\" - 채팅방 구별할 수 있게 경로 적고 data[i]안에서 room id 꺼내서 적어주기)
				html += "<td><button onclick='location.href=\"chat/" + data[i].roomId + "\"'>입장</button></td>"   
				html += "</tr>"
			}
			$("#rooms").html(html)
		}
		
		
		function createRoom() {
			$.ajax({
				url : "create",
				type : "post",  // 생성하는 것이기에 post
				data : {"roomName" : $("#room-name").val()},   // id가 room-name이라는 input태그에 있는 값만 가져오려는 것
				success : function(data) {
					showRooms()
				},
				error : function() {
					alert("오류 발생")
				}
			})
		}
	</script>
</body>
</html>