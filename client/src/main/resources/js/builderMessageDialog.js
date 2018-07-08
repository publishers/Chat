function appendText(message, isMineMessage) {
    document.getElementById('messages').innerHTML =
        buildMessage(message, isMineMessage) +
        document.getElementById('messages').innerHTML;
}

function buildMessage(message, isMineMessage) {
    var side = "left";
    if (isMineMessage) {
        side = "right";
    }
    var time = new Date();
    var messageTemplate =
        "<div id='messageStyle' style='float:" + side + "'>" +
        "   <div id='timePosition' style='float: " + side + "'>" + time.toLocaleTimeString() + "</div>" +
        "   <div id='message'>" + message + "</div>" +
        "</div>";
    return messageTemplate;
}
