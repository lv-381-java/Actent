import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import React from "react";
import {getImageUrl} from "../profile/ProfileView";

let stompClient = null;
let count = 0;

function checkTime(time) {
    if(time < 10){
        return `0${time}`;
    }else{
        return time;
    }
}

function getMessageSendDate(time) {

    if(time !== null){

        let date = new Date(time);

        const dateTime = {
            hours: checkTime(date.getHours()),
            minutes: checkTime(date.getMinutes()),
            seconds: checkTime(date.getSeconds())
        };

        return dateTime;

    }else {
        return null;
    }

}

export function showMessageOutput(messageOutput) {

    let messageArea = document.getElementById('messageArea');
    let messageElement = document.createElement('li');

    if (count !== null) {
        messageElement.classList.add('chat-message-first');
        count = null;
    } else {
        messageElement.classList.add('chat-message');
    }

    let avatarElement = document.createElement('i');
    messageElement.appendChild(avatarElement);

    let usernameElement = document.createElement('span');
    let usernameText = document.createTextNode(messageOutput.senderFirstName);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    if(messageOutput.messageType === 'TEXT'){
        let textElement = document.createElement('p');
        let messageText = document.createTextNode(messageOutput.messageContent);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
    }

    if(messageOutput.messageType === 'IMAGE'){
        let imageElement = document.createElement('img');
        imageElement.classList.add('chat-image');
        imageElement.setAttribute("src", `${getImageUrl(messageOutput.imageFilePath)}`);
        messageElement.appendChild(imageElement);
    }

    let timeElement = document.createElement('p');
    let dateTime = getMessageSendDate(messageOutput.sendTime);
    let time = document.createTextNode(dateTime.hours + ":" + dateTime.minutes + ":" + dateTime.seconds);
    timeElement.appendChild(time);

    messageElement.appendChild(timeElement);
    messageArea.appendChild(messageElement);
}

export function connect(chatId) {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log('Connected ' + frame);
        stompClient.subscribe(`/topic/messages/${chatId}`, messageOutput => {
            console.log(messageOutput.body);
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

export function disconnect() {
    stompClient.disconnect();
    console.log('Disconnected')
}

export function sendMessage(chatId, userId, message) {

    const messageSend = {
        senderId: userId,
        messageContent: message,
        chatId: chatId
    };
    console.log(messageSend);
    stompClient.send("/chat/message", {},
        JSON.stringify(messageSend));
    document.getElementById('outlined-email-input').value = '';
}

export function sendImage(chatId, senderId, filePath){
    const image = {
        senderId: senderId,
        filePath: filePath,
        chatId: chatId
    };
    console.log(image);
    stompClient.send("/chat/image", {},
        JSON.stringify(image));
}