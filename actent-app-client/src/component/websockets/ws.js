import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import React from "react";
import {getImageUrl} from "../profile/ProfileView";
import Confirm from '../chat/Confirm';
import * as ReactDOM from "react-dom";
import {configureAxios, getCurrentUser} from "../../util/apiUtils";
import {API_WS_URL} from "../../constants/apiConstants";

let stompClient = null;
let count = 0;

function checkTime(time) {
    if (time < 10) {
        return `0${time}`;
    } else {
        return time;
    }
}

export function getMessageSendDate(time) {

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

export function showMessageOutputPrepend(messageOutput) {

    if(messageOutput.delete){
        deleteMessage(messageOutput.id, messageOutput.senderId);
        let messageArea = document.getElementById("messageArea");
        messageArea.scrollTop = messageArea.scrollHeight;
        return;
    }

    if(messageOutput.update){
        let date = new Date();
        updateMessage(messageOutput.messageId, date, messageOutput.messageContent);
        let messageArea = document.getElementById("messageArea");
        messageArea.scrollTop = messageArea.scrollHeight;
        return;
    }

    let messageArea = document.getElementById('messageArea');
    let messageElement = document.createElement('li');

    if (count !== null) {
        messageElement.classList.add('chat-message-first');
        count = null;
    } else {
        messageElement.classList.add('chat-message');
    }
    messageElement.setAttribute("id","msg_" + messageOutput.id + "_sender_" + messageOutput.senderId);

    let avatarElement = document.createElement('i');
    messageElement.appendChild(avatarElement);

    let usernameElement = document.createElement('span');
    let usernameText = document.createTextNode(messageOutput.senderFirstName);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    if(messageOutput.messageType === 'TEXT'){
        let textElement = document.createElement('p');
        textElement.classList.add('textField');
        textElement.setAttribute("id", `textField_${messageOutput.id}`);
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

    let editTimeElement = document.createElement('p');
    editTimeElement.classList.add('editTime');
    editTimeElement.setAttribute("id", `editTime_${messageOutput.id}`);

    if(messageOutput.lastEditTime != null){
        if(messageOutput.messageType === 'TEXT'){
            let editTime = getMessageSendDate(messageOutput.lastEditTime);
            let editTimeP = document.createTextNode("Edited at: " + editTime.hours + ":" + editTime.minutes + ":" + editTime.seconds);
            editTimeElement.appendChild(editTimeP);
        }
    }

    messageElement.appendChild(editTimeElement);

    messageElement.onclick = function(){
        let t = new Date().getTime();
        configureAxios();
        getCurrentUser().then(res => {
            let mas = this.id.split("_");
            if(res.data.id == mas[3]){
                ReactDOM.render(<Confirm
                        key={t} messageId={mas[1]}
                        senderId={mas[3]}
                        open={true}
                        chatId={messageOutput.chatId}
                    />,
                    document.getElementById("bool"));
            }
        });
    };

    messageArea.prepend(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

export function showMessageOutput(messageOutput) {

    if(messageOutput.delete){
        deleteMessage(messageOutput.id, messageOutput.senderId);
        let messageArea = document.getElementById("messageArea");
        messageArea.scrollTop = messageArea.scrollHeight;
        return;
    }

    if(messageOutput.update){
        let date = new Date();
        updateMessage(messageOutput.messageId, date, messageOutput.messageContent);
        let messageArea = document.getElementById("messageArea");
        messageArea.scrollTop = messageArea.scrollHeight;
        return;
    }

    let messageArea = document.getElementById('messageArea');
    let messageElement = document.createElement('li');

    if (count !== null) {
        messageElement.classList.add('chat-message-first');
        count = null;
    } else {
        messageElement.classList.add('chat-message');
    }
    messageElement.setAttribute("id","msg_" + messageOutput.id + "_sender_" + messageOutput.senderId);

    let avatarElement = document.createElement('i');
    messageElement.appendChild(avatarElement);

    let usernameElement = document.createElement('span');
    let usernameText = document.createTextNode(messageOutput.senderFirstName);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);

    if(messageOutput.messageType === 'TEXT'){
        let textElement = document.createElement('p');
        textElement.classList.add('textField');
        textElement.setAttribute("id", `textField_${messageOutput.id}`);
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

    let editTimeElement = document.createElement('p');
    editTimeElement.classList.add('editTime');
    editTimeElement.setAttribute("id", `editTime_${messageOutput.id}`);

    if(messageOutput.lastEditTime != null){
        if(messageOutput.messageType === 'TEXT'){
            let editTime = getMessageSendDate(messageOutput.lastEditTime);
            let editTimeP = document.createTextNode("Edited at: " + editTime.hours + ":" + editTime.minutes + ":" + editTime.seconds);
            editTimeElement.appendChild(editTimeP);
        }
    }

    messageElement.appendChild(editTimeElement);

    messageElement.onclick = function(){
        let t = new Date().getTime();
        configureAxios();
        getCurrentUser().then(res => {
            let mas = this.id.split("_");
            if(res.data.id == mas[3]){
                ReactDOM.render(<Confirm
                        key={t} messageId={mas[1]}
                        senderId={mas[3]}
                        open={true}
                        chatId={messageOutput.chatId}
                    />,
                    document.getElementById("bool"));
            }
        });
    };

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

export function connect(chatId) {
    const socket = new SockJS(API_WS_URL);
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

export function deleteURL(messageId, senderId){
    const deleteMes = {
        messageId: messageId,
        senderId: senderId,
    };
    stompClient.send(`/chat/message/delete`, {}, JSON.stringify(deleteMes));
}

function deleteMessage(messageId, senderId){
    let messageArea = document.getElementById('messageArea');
    let message = document.getElementById(`msg_${messageId}_sender_${senderId}`);
    messageArea.removeChild(message);
}

export function updateURL(chatId, messageContent, senderId, messageId){
    const updateMes = {
        chatId: chatId,
        messageContent: messageContent,
        senderId: senderId,
        messageId: messageId,
    };
    stompClient.send("/chat/message/update", {}, JSON.stringify(updateMes));
}

function updateMessage(messageId, date, editMessage){
    document.getElementById(`textField_${messageId}`).textContent = editMessage;
    let time = getMessageSendDate(date);
    document.getElementById(`editTime_${messageId}`).textContent = "Edited at: " + time.hours + ":" + time.minutes + ":" + time.seconds;
}