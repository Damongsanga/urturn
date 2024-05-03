import { Client } from '@stomp/stompjs';
import { create } from "zustand";
import { persist } from 'zustand/middleware';
import { questionInfo, roomState } from '../types/roomTypes';
import { NavigateFunction } from 'react-router-dom';
import { loadMarkdownFromCDN } from '../utils/solve/loadMarkdownFromCDN';

const url = import.meta.env.VITE_API_WEBSOCKET_URL

export const useRoomStore = create<roomState>() (
    persist(
        (set, get) => ({
            navigate: null,
            client: null,
            userInfo: null,
            roomInfo: null,
            questionInfos: [],

            setNavigate: (navigate: NavigateFunction) => {
                set({ navigate: navigate })
            },

            createRoom: ( token:string, userId: number ) => {
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
                        //순환참조 발생 코드
                        set((state) => ({ ...state, roomInfo: roomInfo }));
                    });
                    client.subscribe('/user/' + userId + '/userInfo', (msg) => {
                        console.log('Received message: userInfo' + msg.body);
                        const userInfo = JSON.parse(msg.body);
                        set((state) => ({ ...state, userInfo: userInfo }));
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
                        const navi = get().navigate;
                        navi!('/check');
                        set((state) => ({...state, questionInfos: questionInfos}));
                    });
                    client.subscribe('/user/' + userId + '/startToSolve', () => {
                        
                        const navi = get().navigate;
                        navi!('/solve');
                    });
                    
                    console.log('Connected: ' + frame);

                    client.publish({
                        destination: '/app/createRoom',
                        body: JSON.stringify({
                           memberId : userId,
                        }),
                    });
                };
                
                client.onStompError = function (frame) {
                console.log('Broker reported error: ' + frame.headers['message']);
                console.log('Additional details: ' + frame.body);
                };
            
                client.activate();
                
                set((state) => ({ ...state, client }));

            },

            enterRoom: (token : string, userId : number, roomId : string) => {
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
                        //순환참조 발생 코드
                        set((state) => ({ ...state, roomInfo: roomInfo }));
                    });
                    client.subscribe('/user/' + userId + '/userInfo', (msg) => {
                        console.log('Received message: userInfo' + msg.body);
                        const userInfo = JSON.parse(msg.body);
                        set((state) => ({ ...state, userInfo: userInfo }));
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
                        const navi = get().navigate;
                        navi!('/check');
                        set((state) => ({...state, questionInfos: questionInfos}));
                    });
                    console.log('Connected: ' + frame);

                    client.publish({
                        destination: '/app/enterRoom',
                        body: JSON.stringify({
                           roomId : roomId,
                        }),
                    });
                };
                
                client.onStompError = function (frame) {
                console.log('Broker reported error: ' + frame.headers['message']);
                console.log('Additional details: ' + frame.body);
                };
            
                client.activate();
                
                set((state) => ({ ...state, client }));
            },

            clearRoom: () => {
                set({ client: null });
                set({ userInfo: null });
                set({ roomInfo: null });
            }
            
        }),
        {
            name: 'room',
        },
    ),


);