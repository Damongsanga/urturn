import { Client } from '@stomp/stompjs';
import { create } from "zustand";
import { persist } from 'zustand/middleware';
import { roomState } from '../types/roomTypes';

const url = import.meta.env.VITE_API_WEBSOCKET_URL
const port = import.meta.env.VITE_API_WEBSOCKET_PORT

export const useRoomStore = create<roomState>() (
    persist(
        (set) => ({
            client: null,
            players: [],

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

            clearRoom: () => {
                set({ client: null });
                set({ players: [
                    { profileImgUrl: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png', nickname: 'empty' },
                    { profileImgUrl: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png', nickname: 'empty' },
                    ] 
                });
            }
            
        }),
        {
            name: 'room',
        },
    ),


);