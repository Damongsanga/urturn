import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { create } from "zustand";
import { persist } from 'zustand/middleware';

interface roomState {
    client: Client | null,

    createRoom: () => void
}

const url = import.meta.env.WEBSOCKET_URL
const port = import.meta.env.WEBSOCKET_PORT

export const useRoomStore = create<roomState>() (
    persist(
        (set) => ({
            client: null,
            createRoom: () => {
                const client = new Client({
                    brokerURL: 'ws://' + url + ':' + port + '/app/createRoom',
                    debug: function (str: string) {
                      console.log("debug:" + str);
                    },
                    reconnectDelay: 5000, //자동 재 연결
                    heartbeatIncoming: 4000,
                    heartbeatOutgoing: 4000,
                  });
            
                client.onConnect = function (frame) {
                    client.subscribe('/topic/test', (msg) => {
                        console.log('Received message: ' + msg.body);
                        client.publish({
                            destination: '/hello',
                            body: 'Hello world',
                          });
                    });
                    console.log('Connected: ' + frame);
                };
                
                client.onStompError = function (frame) {
                console.log('Broker reported error: ' + frame.headers['message']);
                console.log('Additional details: ' + frame.body);
                };
            
                client.activate();
                
                set((state) => ({ ...state, client }));
            },
            
        }),
        {
            name: 'room',
        },
    ),
);