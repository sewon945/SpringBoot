<%@page import="com.example.demo.model.BootMember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<title>Insert title here</title>
<style>
   #msgArea{
      width : 200px;
      height : 300px;
      background-color: whitesmoke;
      overflow:auto;
   }
</style>
</head>
<body>
	<!-- 내 닉네임 -->
	<%
		BootMember member = (BootMember)session.getAttribute("member");
		String nick = member.getNick();
	%>

<div class="container">
        <div class="col-6">
             <label><b>채팅방</b></label>
             <div class="input-group-append">
                 <button type="button" id="button-quit" onclick="quit()">방 나가기</button>
             </div>
        </div>
        <div>
        <!-- 채팅 내용 출력 -->
             <div id="msgArea" class="col"></div>
             <div class="col-6">
                 <div class="input-group mb-3">
                 <!-- 채팅 내용 입력 -->
                     <input type="text" id="msg" class="form-control" aria-label="Recipient's username" aria-describedby="button-addon2">
                    <!-- 채팅 내용 전송 -->
                    <div class="input-group-append">
                    <!-- 버튼 누르면 send함수 호출시키기 -->
                        <button class="btn btn-outline-secondary" type="button" onclick="send()" id="button-send">전송</button>
                    </div>
                 </div>
             </div>
        </div>
    </div>
    <script>
    	// 1. 소켓 연결 해주기 (소켓 객체 생성(경로설정)) - 어느 서버로 연결할 것인지 정의하기
    	const webSocket = new WebSocket("ws://localhost:8090/myapp/ws/chat")
    	
    	// 2. 연결 / 해제 / 메세지 보낼 대 어떻게 할 건지 정의해주기
    	// 입장하면 ENTER 메세지부터 보내기
    	
    	// 내가 정의한 것 호출(실행)될 수 있게 적어줌
    	webSocket.onopen = onOpen;   // 채팅방 입장
    	webSocket.onmessage = onMessage;   // 서버로부터 메세지를 받았을 때 (ENTER, TALK)
    	
    	// 연결(채팅방에 입장했을 때)
    	function onOpen() {
    		// 서버에 메세지 보내기-서버로 보낼 메세지 만들기(ENTER, 채팅방 아이디(roomId), 누가 입장하는지(sender), 입장할 때 메세지 내용은 생략)
    		// JSON 형태의 문자열로 만들어주기
    		// ChatMessage 파일에서 키값은 필드 이름(messageType)과 맞춰서 써주고 HomeController에서 model.addAttribute 키값(roomId) 넣어주기
    		var msg = {"messageType" : "ENTER" , "roomId" : "${roomId}" , "sender" : "<%=nick%>"}
    		webSocket.send(JSON.stringify(msg))  // send - 메세지 보낼 때
    	}
    	
    	// 채팅방 나갈 때 호출할 함수 - 방 나가기 버튼 클릭했을 때
    	function quit() {
    		var msg = {"messageType" : "QUIT" , "roomId" : "${roomId}" , "sender" : "<%=nick%>"}
    		webSocket.send(JSON.stringify(msg))    // msg를 JSON 객체 서버로 보내기
    		webSocket.close()   // 연결 해제
    		
    		// 채팅방생성 버튼 보이는 곳으로 주소창을 통해서 페이지 이동시켜주기
    		location.href='/myapp/room'
    		
    	}
    	
    	
    	// 채팅방에서 채팅 버튼 클릭될 때 호출
    	function send() {
    		let msg = $("#msg").val()
    		// 내가 보낼 메세지 정의하기
    		let talkMsg = {"messageType" : "TALK" , "roomId" : "${roomId}" , "sender" : "<%=nick%>" , "message" : msg}
    		// JSON 형태의 문자열로 서버에 보내기
    		webSocket.send(JSON.stringify(talkMsg))
    		// 내가 쓴 채팅 input 태그 다시 비워주기
    		$("#msg").val("")
    	}
    
    	// 서버로부터 메세지를 받았을 때 호출 (msg - 서버가 보내준 메세지(메시지를 받는 것이기에 인자값 넣어줌))
    	function onMessage(msg) { 
    		//console.log(msg)
    		// 파씽하기
    		var data = msg.data
            var msgData = JSON.parse(data)
            
            // 다른 사람이 입장했을 때만 누구인지 출력(내가 입장했을 때는 출력X)
            if(msgData.sender == "<%=nick%>"){  // 내가 채팅을 보냈을 때 (ENTER X, TALK O)
            	if(msgData.messageType != 'ENTER'){  // ENTER가 아닐 경우 - TALK
		            // 내가 채팅내용을 채팅방에 보내주기
		            var str = "<div align='right'>";  // 오른쪽 정렬
		            // 채팅 내용
		            str += "<b>나 : " + msgData.message + "</b>";
		            str += "</div>";
		            $("#msgArea").append(str);         
            	}
            }else {  // 다른 사람이 입장하거나 나가거나 채팅을 보냈을 때
            	if(msgData.messageType == 'ENTER') {  // 입장했을 때
            		// 채팅내용을 채팅방에 보내주기
                    var str = "<div>";
                    // 채팅 내용
                    str += "<b>" + msgData.sender + "님이 입장했습니다.</b>";
                    str += "</div>";
                    $("#msgArea").append(str);            		
            	} else if(msgData.messageType == 'QUIT') {  // 채팅방 나갔을 때
            		// 채팅내용을 채팅방에 보내주기
                    var str = "<div>";
                    // 채팅 내용
                    str += "<b>" + msgData.sender + "님이 퇴장했습니다.</b>";
                    str += "</div>";
                    $("#msgArea").append(str);
            	}
            	
            	else {  // 채팅 보냈을 때
            		// 채팅내용을 채팅방에 보내주기
                    var str = "<div>";
                    // 채팅 내용
                    str += "<b>" + msgData.sender + " : " + msgData.message + "</b>";
                    str += "</div>";
                    $("#msgArea").append(str);           		
            	}
            }
    		
    		

            
    	}
    </script>
</body>
</html>