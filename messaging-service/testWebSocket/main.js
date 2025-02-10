'use strict';

const usernamePage = document.querySelector('#username-page');
const loginPage = document.querySelector('#login-page');
const registerPage = document.querySelector('#register-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const loginForm = document.querySelector('#loginForm');
const registerForm = document.querySelector('#registerForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

const showRegister = document.querySelector('#showRegister');
const showLogin = document.querySelector('#showLogin');

let stompClient = null;
let userId = null;
let fullname = null;
let email = null;
let selectedUserId = null;

showRegister.addEventListener('click', () => {
    loginPage.classList.add('hidden');
    registerPage.classList.remove('hidden');
});

showLogin.addEventListener('click', () => {
    registerPage.classList.add('hidden');
    loginPage.classList.remove('hidden');
});


// Handle Registration
async function handleRegister(event) {
    event.preventDefault();

    const username = document.querySelector('#regUsername').value.trim();
    const email = document.querySelector('#regEmail').value.trim();
    const password = document.querySelector('#regPassword').value.trim();

    try {
        const response = await fetch('http://localhost:8085/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username,
                email,
                password
            })
        });

        if (response.ok) {
            const data = await response.json();
            alert('Registration successful! Please login.');
            registerPage.classList.add('hidden');
            loginPage.classList.remove('hidden');
        } else {
            const error = await response.text();
            alert('Registration failed: ' + error);
        }
    } catch (error) {
        alert('Registration failed: ' + error.message);
    }
}

// Handle Login
async function handleLogin(event) {
    event.preventDefault();

    const email = document.querySelector('#loginEmail').value.trim();
    const password = document.querySelector('#loginPassword').value.trim();

    try {
        const response = await fetch('http://localhost:8085/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email,
                password
            })
        });

        if (response.ok) {
            const userData = await response.json();
            userId = userData.userId;
            fullname = userData.username;
            email = userData.email;

            // Connect to chat after successful login
            connectToChat();
        } else {
            const error = await response.text();
            alert('Login failed: ' + error);
        }
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

function connect(event) {
    userId = document.querySelector('#userId').value.trim();
    fullname = document.querySelector('#fullname').value.trim();
    email = document.querySelector('#email').value.trim();

    if (userId && fullname && email) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('http://localhost:8083/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${userId}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/topic`, onMessageReceived);
    console.log(email);
    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({userId: userId, fullName: fullname, email: email, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('http://localhost:8083/api/messaging/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.fullName !== fullname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.userId;

    const userImage = document.createElement('img');
    userImage.src = 'user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    console.log("Selected User: ",selectedUserId);
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function displayMessage(senderId, content, filePath, fileName, fileSize) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === userId) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }

    // Add text content if present
    if (content.length > 0) {
        const message = document.createElement('p');
        message.textContent = content;
        messageContainer.appendChild(message);
    }

    // Add file-related content if a file is attached
    if (filePath) {
        // Create a div for the download link
        const downloadDiv = document.createElement('div');
        downloadDiv.className = 'download-container'; // Add your styling class here

        // Create the download link
        const downloadLink = document.createElement('a');
        downloadLink.href = filePath; // Use the filePath directly for the sender
        downloadLink.textContent = `${fileName} (${formatFileSize(fileSize)})`;
        downloadLink.target = '_blank';

        // Create a download icon (you can use an SVG or a font icon)
        const downloadIcon = document.createElement('img');
        downloadIcon.src = 'download-svgrepo-com.svg'; // Update with your icon path
        downloadIcon.alt = 'Download';
        downloadIcon.className = 'download-icon'; // Add any styling class for the icon

        // Append icon and link to the div
        downloadDiv.appendChild(downloadIcon);
        downloadDiv.appendChild(downloadLink);

        // Append the download div to the message container
        messageContainer.appendChild(downloadDiv);
    }

    // Append the message container to the chat area
    chatArea.appendChild(messageContainer);

    // Scroll the chat area to the bottom
    chatArea.scrollTop = chatArea.scrollHeight;
}


async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`http://localhost:8083/api/messaging/messages/${userId}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content, chat.filePath, chat.fileName, chat.fileSize);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

async function uploadFile(file, userId, username) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId);
    formData.append('username', username);

    console.log("UPLOAD FORMDATA ::::",formData);
    // Upload file to port 8084
    const response = await fetch('http://localhost:8084/api/upload', {
        method: 'POST',
        body: formData,
    });
    console.log(response);
    if (!response.ok) {
        throw new Error('File upload failed');
    }

    const fileResponse = await response.json();
    return fileResponse;  // Extract file URL from the response
}

async function downloadFile(fileUrl) {
    try {
        const response = await fetch(`http://localhost:8084/api/upload/download?fileUrl=${encodeURIComponent(fileUrl)}`);

        if (!response.ok) {
            throw new Error('Failed to get presigned URL');
        }

        const presignedUrl = await response.text();
        return presignedUrl;

    } catch (error) {
        console.error("Error downloading file:", error);
    }
}

async function sendMessage(event) {
    event.preventDefault();

    const messageContent = messageInput.value.trim();
    const fileInput = document.querySelector('#fileInput');
    const file = fileInput.files[0];
    let fileUrl = '';
    let uploadResponse = null;
    let filePath = "";
     let fileName = "";
    let fileSize = "";
    try {
        if (file) {
            uploadResponse = await uploadFile(file, userId, "yugma");  // Upload and get file URL
            filePath = uploadResponse.filePath;
            fileName = uploadResponse.fileName;
            fileSize = uploadResponse.fileSize;
        }

        const chatMessage = {
            senderId: userId,
            recipientId: selectedUserId,
            content: messageContent,
            timestamp: new Date(),
            filePath: filePath,
            fileName: fileName,
            fileSize: fileSize
        };

        if (stompClient) {
            stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            displayMessage(userId, messageContent, filePath, fileName, fileSize);
            messageInput.value = '';
            fileInput.value = '';  // Clear the file input field
        }

        chatArea.scrollTop = chatArea.scrollHeight;
    } catch (error) {
        console.error("Error sending message or uploading file:", error);
    }
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    // Display message directly
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(
            message.senderId,
            message.content,
            message.filePath,
            message.fileName,
            message.fileSize
        );
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = parseInt(nbrMsg.textContent || 0) + 1;
    }
}

// Function to format file size
function formatFileSize(sizeInBits) {
    const sizeInBytes = sizeInBits / 8;
    if (sizeInBytes < 1024) {
        return `${sizeInBytes.toFixed(2)} Bytes`;
    } else if (sizeInBytes < 1048576) {
        return `${(sizeInBytes / 1024).toFixed(2)} KB`;
    } else {
        return `${(sizeInBytes / 1048576).toFixed(2)} MB`;
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({userId: userId, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true); // step 1
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();