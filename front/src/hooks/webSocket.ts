
import { useNavigate } from "react-router-dom";
import { Client } from "@stomp/stompjs";

import { useAuthStore } from "../stores/useAuthStore";
import { useRoomStore } from "../stores/room";
import { loadMarkdownFromCDN } from "../utils/solve/loadMarkdownFromCDN";
import { questionInfo, reviewInfo } from "../types/roomTypes";
import {useOpenVidu} from "./openVidu.ts";
import {useRtcStore} from "../stores/rtc.ts";

const url = import.meta.env.VITE_API_WEBSOCKET_URL
const MAX_TIME = 30

export const useWebSocket = () => {
    const navi = useNavigate();
    const authStore = useAuthStore();
    const roomStore = useRoomStore();
    const rtcStore = useRtcStore();
    const openVidu = useOpenVidu();
    
    const connect = (roomId: string|null = null) => {
        const userId = authStore.memberId;
        const token = authStore.accessToken;

        const setTimer= (maxSec: number) => {
            
            roomStore.setSec(maxSec);
        
            const timer = setInterval(() => {
                if(roomStore.getSec() > 0){
                    roomStore.setSec(roomStore.getSec() - 1);
                }
                else if (roomStore.getSec() <= 0) {
                    if(roomStore.getPairProgramingMode()===false || roomStore.getPairProgramingRole() === 'Driver'){
                        roomStore.getClient()?.publish({
                            destination: '/app/switchCode',
                            body: JSON.stringify({ 
                                code: roomStore.getCode(),
                                roomId: roomStore.getRoomInfo()?.roomId,
                                round: roomStore.getRound(),
                                algoQuestionId: roomStore.getQuestionInfos()?.[roomStore.getQuestionIdx()]?.algoQuestionId,
                                isHost: roomStore.getRoomInfo()?.host,
                                isPair: roomStore.getPairProgramingMode(),
                            })
                        })
                    }
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
                if(roomStore.getRoomInfo()?.host && userInfo.relativeUserNickName===null){
                    openVidu.masterCreate();
                }
                if(roomStore.getRoomInfo()?.host && userInfo.relativeUserNickName){
                    console.log("sendOVSession : " + JSON.stringify({ roomId: roomStore.getRoomInfo()?.roomId, sessionId: rtcStore.getSessionId() }));
                    client.publish({
                        destination: '/app/sendOVSession',
                        body: JSON.stringify({
                            roomId: roomStore.getRoomInfo()?.roomId,
                            sessionId: rtcStore.getSessionId(),
                        })
                    })
                }
                roomStore.setUserInfo(userInfo);
            });
            client.subscribe('/user/' + userId + '/receiveOVSession', (msg) => {
                console.log('Received message: receiveOVSession' + msg.body);
                const sessionId = msg.body;
                rtcStore.setSessionId(sessionId);
                openVidu.partnerJoin(sessionId);
            })
            client.subscribe('/user/' + userId + '/questionInfo', (msg) => {
                const questionInfos: questionInfo[] = JSON.parse(msg.body);
                console.log('Received message: questionInfo' + msg.body);
                questionInfos.forEach(
                    async (questionInfo: questionInfo) => {
                        const content = await loadMarkdownFromCDN(questionInfo.algoQuestionUrl);
                        questionInfo.algoQuestionContent = content;
                    }
                )
                roomStore.setQuestionInfos(questionInfos);
                navi('/trans/check');
            });
            client.subscribe('/user/' + userId + '/startToSolve', () => {
                const idx = roomStore.getRoomInfo()?.host ? 0 : 1;
                console.log('idx: ' + idx);
                roomStore.setQuestionIdx(idx);
                setTimer(MAX_TIME);
                navi('/trans/solve');
            });
            client.subscribe('/user/' + userId + '/switchCode', (msg) => {
                const data = JSON.parse(msg.body);
                console.log('Received message: switchCode' + msg.body);
                roomStore.setCode(data.code);
                roomStore.setRound(data.round);

                const idx = roomStore.getQuestionIdx() === 0 ? 1 : 0;
                roomStore.setQuestionIdx(idx);
                setTimer(MAX_TIME);
                navi('/trans/solveSwitch');
            });
            client.subscribe('/user/' + userId + '/switchRole', (msg) => {
                console.log('Received message: switchRole' + msg.body);
                const round = Number(msg.body);
                roomStore.setRound(round);
                if(roomStore.getPairProgramingRole() === 'Driver'){
                    roomStore.setPairProgramingRole('Navigator');
                    console.log("역할: Navigator");
                    
                }
                else if(roomStore.getPairProgramingRole() === 'Navigator'){
                    roomStore.setPairProgramingRole('Driver');
                    console.log("역할: Driver");
                }
                setTimer(MAX_TIME);
                navi('/trans/pairSolveSwitch');
            })
            client.subscribe('/user/' + userId + '/submit/result', (msg) => {
                console.log('Received message: submit/result' + msg.body);
                const data = JSON.parse(msg.body);
                const result = data.result;

                let consoleMsg = '';

                if(result===true){
                    consoleMsg += '정답입니다!\n\n\n';
                }
                else{
                    consoleMsg += '오답입니다...\n\n\n';
                }

                const testcaseResults = data.testcaseResults;
                for(let i = 0; i < testcaseResults.length; i++){
                    const t = testcaseResults[i];
                    consoleMsg += (i+1) + '번 테스트케이스 : ' +  t.status + '\n';
                    if(t.stderr!==null){
                        consoleMsg += t.stderr + '\n';
                    }
                    consoleMsg += '\n';
                }
                if(roomStore.getPairProgramingMode()===false || roomStore.getPairProgramingRole() === 'Driver'){
                    rtcStore.getOpenVidu()?.session.signal({
                        data: consoleMsg,
                        to: [],
                        type: 'console'
                    })
                }
                roomStore.setConsole(consoleMsg);
            })
            client.subscribe('/user/' + userId + '/role', (msg) => {
                console.log('Received message: submit/role' + msg.body);
                const role = msg.body;
                roomStore.setPairProgramingMode(true);
                roomStore.setPairProgramingRole(role);
                console.log('role: ' + role);
                if(role==='Navigator'){
                    const idx = roomStore.getQuestionIdx() === 0 ? 1 : 0;
                    roomStore.setQuestionIdx(idx);
                }
                navi('/trans/pairsolve');
            })
            client.subscribe('/user/' + userId + '/showRetroCode', (msg) => {
                console.log('Received message: showRetroCode' + msg.body);

                const data = JSON.parse(msg.body);
       
                roomStore?.getQuestionInfos()?.forEach((questionInfo: questionInfo) => {
                    const idx = questionInfo.algoQuestionId;
                    const tmp : reviewInfo[] = [];

                    data[idx].codes.forEach((userCode: any) => {
                        tmp.push({
                            title: String(userCode.round) + " 라운드",
                            content: userCode.code
                        })
                    })
                    tmp.push({
                        title: "정답",
                        content: data[idx].code
                    })
                    roomStore.getReviewInfos().push(tmp);
                });
                roomStore.setPairProgramingRole('Navigator');
                roomStore.setSec(0);

                navi('/trans/review');
            })
            client.subscribe('/user/' + userId + '/finishSocket/githubUpload', (msg) => {
                const data: boolean = JSON.parse(msg.body);
                if(data===true){
                    client.deactivate();
                    // uri
                    console.log("dddddd")
                    location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=repo&redirect_uri=https://urturn.site/auth/github/upload`;
                }
            })
            console.log('Connected: ' + frame);

        };
        
        client.onStompError = function (frame) {
            console.log('Broker reported error: ' + frame.headers['message']);
            console.log('Additional details: ' + frame.body);
        };

        client.activate();

        setTimeout(() => {
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
        }, 1000);
        
        roomStore.setClient(client);
    }

    return {
        connect
    }
}