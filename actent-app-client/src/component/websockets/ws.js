import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

var stompClient = null;
var count = 0;

export function showMessageOutput(messageOutput) {

    var messageArea = document.getElementById('messageArea');
    var messageElement = document.createElement('li');

    if(count !== null) {
        messageElement.classList.add('chat-message-first');
        count = null;
    }else{
        messageElement.classList.add('chat-message');
    }

    var avatarElement = document.createElement('i');
    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(messageOutput.senderFirstName);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(messageOutput.messageContent);
    textElement.appendChild(messageText);

    var timeElement = document.createElement('p');
    var time = document.createTextNode(messageOutput.sendTime);
    timeElement.appendChild(time);

    messageElement.appendChild(textElement);
    messageElement.appendChild(timeElement);
    messageArea.appendChild(messageElement);
}

export function connect(chatId) {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('Connected ' + frame);
        stompClient.subscribe(`/topic/messages/${chatId}`, messageOutput => {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

export function disconnect() {
    stompClient.disconnect();
    console.log('Disconnected')
}

export function sendMessage(chatId) {

    const messageSend = {
        chatId: chatId,
        messageContent: document.getElementById('outlined-email-input').value.trim()
    };
    console.log(messageSend);
    stompClient.send("/chat/message", {},
        JSON.stringify(messageSend));
    document.getElementById('outlined-email-input').value = '';
}