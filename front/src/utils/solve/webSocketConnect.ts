import { NavigateFunction} from "react-router-dom";
import { Client } from "@stomp/stompjs";

import { AuthState } from "../../stores/useAuthStore";
import { questionInfo, roomState } from "../../types/roomTypes";
import { loadMarkdownFromCDN } from "./loadMarkdownFromCDN";

const url = import.meta.env.VITE_API_WEBSOCKET_URL

export const webSocketConnect = (navi: NavigateFunction, authStore: AuthState, roomStore: roomState, roomId: string | null = null) => {
    console.log("기다려 냥파스 라이프")
    console.log(navi);
    console.log(authStore);
    console.log(roomStore);
    console.log(roomId);
    
    const userId = authStore.memberId;
    const token = authStore.accessToken;

    const setTimer= (maxSec: number) => {
        
        roomStore.setSec(maxSec);
    
        const timer = setInterval(() => {
            if(roomStore.sec > 0){
                roomStore.setSec(roomStore.sec - 1);
            }
            else if (roomStore.sec <= 0) {
                roomStore.client?.publish({
                    destination: '/app/switchCode',
                    body: JSON.stringify({ 
                        code: roomStore.editor?.getValue(),
                        roomId: roomStore.roomInfo?.roomId,
                        round: roomStore.round,
                        algoQuestionId: roomStore.questionInfos?.[roomStore.questionIdx]?.algoQuestionId,
                        isHost: roomStore.roomInfo?.host,
                        })
                })
                clearInterval(timer);
            }
        }, 1000)
    }
    

    console.log("웹소켓 요청: " + url + '/ws');
    const client = new Client({
        brokerURL: url + '/ws',
        
        connectHeaders: {
            Authorization: 'Bearer ' + token,
        },
        
        debug: function (str: string) {
          console.log("debug:" + str);
        },
        reconnectDelay: 5000000, //자동 재 연결
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

    client.onConnect = function (frame) {
        client.subscribe('/user/' + userId + '/roomInfo', (msg) => {
            console.log('Received message: roomInfo' + msg.body);
            const roomInfo = JSON.parse(msg.body);
            roomStore.setRoomInfo(roomInfo);
        });
        client.subscribe('/user/' + userId + '/userInfo', (msg) => {
            console.log('Received message: userInfo' + msg.body);
            const userInfo = JSON.parse(msg.body);
            roomStore.setUserInfo(userInfo);
        });
        client.subscribe('/user/' + userId + '/questionInfo', (msg) => {
            console.log('Received message: questionInfo ' + msg.body);
            const questionInfos: questionInfo[] = JSON.parse(msg.body);
            
            questionInfos.forEach(
                async (questionInfo: questionInfo) => {
                    const content = await loadMarkdownFromCDN(questionInfo.algoQuestionUrl);
                    questionInfo.algoQuestionContent = content;
                }
            )
            navi('/check');
            roomStore.setQuestionInfos(questionInfos);
        });
        client.subscribe('/user/' + userId + '/startToSolve', () => {
            console.log('Start to solve');
            console.log('roomstore: ' + roomStore.roomInfo?.roomId);
            console.log('roomstore: ' + roomStore.roomInfo?.host);
            const idx = roomStore.roomInfo?.host ? 0 : 1;
            console.log('idx: ' + idx);
            roomStore.setQuestionIdx(idx);
            setTimer(10);
            navi!('/solve');
        });
        client.subscribe('/user/' + userId + '/switchCode', (msg) => {
            const data = JSON.parse(msg.body);
            roomStore.editor?.setValue(data.code);
            roomStore.setRound(data.round);

            const idx = roomStore.questionIdx === 0 ? 1 : 0;
            roomStore.setQuestionIdx(idx);
            setTimer(10);

        });
        
        console.log('Connected: ' + frame);


        if(roomId===null){
            client.publish({
                destination: '/app/createRoom',
                body: JSON.stringify({
                   memberId : userId,
                }),
            });
        }
        else{
            client.publish({
                destination: '/app/enterRoom',
                body: JSON.stringify({
                    roomId : roomId,
                }),
            });
        }
    };
    
    client.onStompError = function (frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
    };

    client.activate();
    
    roomStore.setClient(client);
}