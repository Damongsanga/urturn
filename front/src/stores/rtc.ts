import { create } from "zustand";
import { persist } from 'zustand/middleware';
import { rtcState } from "../types/rtcTypes";


const TIME_INTERVAL = 10; //sec

export const useRoomStore = create<rtcState>() (
    persist(
        (set, get) => ({
            sessionId: null,
            connectionId: null,
            connectToken: null,

            ov: null,

            createConnection: () => {

                return false;
            },

            joinConnection: () => {
                return false;
            },

            clearRtc: () => {
                
            }
            
        }),
        {
            name: 'rtc',
        },
    ),


);