import { Client } from "@stomp/stompjs";

export const  initWebSocket = (url : string, port : number, handlerList : [[string, Function]]) : Client => {
    const client = new Client({
        brokerURL: 'ws://' + url + ':' + port.toString() + '/ws',
        debug: function (str: string) {
          console.log("debug:" + str);
        },
        reconnectDelay: 5000, //자동 재 연결
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

    client.onConnect = function (frame) {
        handlerList.forEach(([topic, handler]) => {
            client.subscribe(topic, (msg) => {
                handler(msg);
            });
        });
        // client.subscribe('/topic/test', (msg) => {
        //     console.log('Received message: ' + msg.body);
        //     client.publish({
        //         destination: '/hello',
        //         body: 'Hello world',
        //       });
        // });
        console.log('Connected: ' + frame);
    };
    
    client.onStompError = function (frame) {
    console.log('Broker reported error: ' + frame.headers['message']);
    console.log('Additional details: ' + frame.body);
    };

    client.activate();

    // if (typeof WebSocket !== 'function') {
    //     client.webSocketFactory = function () {
    //         const socket = new SockJS('http://localhost:15674/stomp');
    //         return {
    //           ...socket,
    //           onmessage: (ev) => {
    //             // Handle the message event here
    //           },
    //           // Add any other properties or methods you need for IStompSocket
    //         };
    //       };
    // }

    return client
}