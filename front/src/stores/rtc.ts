import { create } from 'zustand';
//import { persist } from 'zustand/middleware';
import { OpenVidu } from 'openvidu-browser';

interface rtcState {
    sessionId: string | null;
    connectionId: string | null;
    ov: OpenVidu | null,

    setSessionId: (sessionId: string) => void;
    getSessionId: () => string | null;

    setConnectionId: (connectionId: string) => void;
    getConnectionId: () => string | null;

    setOpenVidu: (ov: OpenVidu) => void;
    getOpenVidu: () => OpenVidu | null;

    clearRtc: () => void;
}

export const useRtcStore = create<rtcState>() (
    // persist(
    (set, get) => ({
        sessionId: null,
        connectionId: null,
        ov: null,

        setSessionId: (sessionId: string) => {set({ sessionId: sessionId })},
        getSessionId: () => {return get().sessionId},

        setConnectionId: (connectionId: string) => {set({ connectionId: connectionId})},
        getConnectionId: () => {return get().connectionId},

        setOpenVidu: (ov: OpenVidu) => {set({ov: ov})},
        getOpenVidu: () => {return get().ov},

        clearRtc: () => {
            set({ sessionId: null });
            set({ connectionId: null});
        }
    }),

    // {
    //    name: 'rtc',
    // },
    // ),


);
