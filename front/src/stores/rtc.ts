import { create } from 'zustand';
//import { persist } from 'zustand/middleware';
import {OpenVidu, StreamManager, Subscriber} from 'openvidu-browser';

interface rtcState {
    sessionId: string | null;
    connectionId: string | null;
    ov: OpenVidu | null,
    streamManager: StreamManager | null,
    //subscribers: Subscriber[],
    subscriber: Subscriber | null,
    microphoneVolume: number;
    speakerVolume: number;

    setMicrophoneVolume: (volume: number) => void;
    getMicrophoneVolume: () => number;
    setSpeakerVolume: (volume: number) => void;
    getSpeakerVolume: () => number;

    setSessionId: (sessionId: string) => void;
    getSessionId: () => string | null;

    setConnectionId: (connectionId: string) => void;
    getConnectionId: () => string | null;

    setOpenVidu: (ov: OpenVidu) => void;
    getOpenVidu: () => OpenVidu | null;

    setStreamManager: (streamManager: StreamManager) => void;
    getStreamManager: () => StreamManager | null;

    setSubscriber: (subscriber: Subscriber | null) => void;
    getSubscriber: () => Subscriber | null;
    // setSubscribers: (subscriber: Subscriber[] | null) => void;
    // getSubscribers: () => Subscriber[] | null;

    clearRtc: () => void;
}

export const useRtcStore = create<rtcState>() (
    // persist(
    (set, get) => ({
        sessionId: null,
        connectionId: null,
        ov: null,
        streamManager: null,
        subscriber: null,
        microphoneVolume: 50,
        speakerVolume: 50,
        //subscribers: [],

        setMicrophoneVolume: (volume: number) => {set({microphoneVolume: volume})},
        getMicrophoneVolume: () => {return get().microphoneVolume},

        setSpeakerVolume: (volume: number) => {set({speakerVolume: volume})},
        getSpeakerVolume: () => {return get().speakerVolume},

        setSessionId: (sessionId: string) => {set({ sessionId: sessionId })},
        getSessionId: () => {return get().sessionId},

        setConnectionId: (connectionId: string) => {set({ connectionId: connectionId})},
        getConnectionId: () => {return get().connectionId},

        setOpenVidu: (ov: OpenVidu) => {set({ov: ov})},
        getOpenVidu: () => {return get().ov},

        setStreamManager: (streamManager: StreamManager) => {set({streamManager: streamManager})},
        getStreamManager: () => {return get().streamManager},

        setSubscriber: (subscriber: Subscriber | null) => {set({subscriber: subscriber})},
        getSubscriber: () => {return get().subscriber},
        // setSubscribers: (subscribers: Subscriber[]) => {set({subscribers: subscribers})},
        // getSubscribers: () => {return get().subscribers},

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
