import { Client } from '@stomp/stompjs';
import { create } from "zustand";
import { persist } from 'zustand/middleware';
import { questionInfo, roomState } from '../types/roomTypes';
import { NavigateFunction } from 'react-router-dom';
import { loadMarkdownFromCDN } from '../utils/solve/loadMarkdownFromCDN';
import * as monaco from "monaco-editor";

const TIME_INTERVAL = 10; //sec

const url = import.meta.env.VITE_API_WEBSOCKET_URL

export const useRoomStore = create<roomState>() (
    persist(
        (set, get) => ({
            navigate: null,
            client: null,
            userInfo: null,
            roomInfo: null,
            questionInfos: [],

            questionIdx: -1,
            round: 1,
            editor: null,
            sec: 99999999,
            
            setTimer: () => {
                try{set({ sec: TIME_INTERVAL })}catch(e){}
                const timer = setInterval(() => {
                    if(get().sec >0){
                        try{set({ sec: get().sec - 1 })}catch(e){}
                    }
                    else if (get().sec <= 0) {
                        console.log("round:" + get().round);
                        get().client?.publish({
                            destination: '/app/switchCode',
                            body: JSON.stringify({ 
                                code: get().editor?.getValue(),
                                roomId: get().roomInfo?.roomId,
                                round: get().round,
                                algoQuestionId: get().questionInfos?.[get().questionIdx]?.algoQuestionId,
                                isHost: get().roomInfo?.host,
                             })
                        })
                        clearInterval(timer);
                    }
                }, 1000)
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
                        try{set((state) => ({ ...state, roomInfo: roomInfo }));}catch(e){}
                    });
                    client.subscribe('/user/' + userId + '/userInfo', (msg) => {
                        console.log('Received message: userInfo' + msg.body);
                        const userInfo = JSON.parse(msg.body);
                        try{set((state) => ({ ...state, userInfo: userInfo }));}catch(e){}
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
                        const idx = get().roomInfo?.host ? 0 : 1;
                        try{set({ questionIdx: idx })} catch(e){}
                        get().setTimer();
                        const navi = get().navigate;
                        navi!('/solve');
                    });
                    client.subscribe('/user/' + userId + '/switchCode', (msg) => {
                        const data = JSON.parse(msg.body);
                        get().editor?.setValue(data.code);
                        try{set({ round: data.round })}catch(e){}
                        
                        const idx = get().questionIdx === 0 ? 1 : 0;
                        try{set({ questionIdx: idx })}catch(e){}
                        get().setTimer();

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
                        try{set((state) => ({ ...state, roomInfo: roomInfo }));}catch(e){}
                    });
                    client.subscribe('/user/' + userId + '/userInfo', (msg) => {
                        console.log('Received message: userInfo' + msg.body);
                        const userInfo = JSON.parse(msg.body);
                        try{set((state) => ({ ...state, userInfo: userInfo }));}catch(e){}
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
                        const idx = get().roomInfo?.host ? 0 : 1;
                        try{set({ questionIdx: idx })} catch(e){}
                        get().setTimer();
                        const navi = get().navigate;
                        navi!('/solve');
                    });
                    client.subscribe('/user/' + userId + '/switchCode', (msg) => {
                        const data = JSON.parse(msg.body);
                        get().editor?.setValue(data.code);
                        try{set({ round: data.round })}catch(e){}

                        const idx = get().questionIdx === 0 ? 1 : 0;
                        try{set({ questionIdx: idx })}catch(e){}
                        get().setTimer();

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
                
                set({ client: client });
            },

            setNavigate: (navigate: NavigateFunction) => {set({ navigate: navigate })},
            setEditor: (editor: monaco.editor.IStandaloneCodeEditor) => {set({ editor: editor })},
            setQuestionIdx: (idx: number) => {set({ questionIdx: idx })},

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