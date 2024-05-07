import { Client } from '@stomp/stompjs';
import { create } from "zustand";
//import { persist } from 'zustand/middleware';
import { roomInfo, userInfo, questionInfo, roomState } from '../types/roomTypes';
import * as monaco from "monaco-editor";

export const useRoomStore = create<roomState>() (
    //persist(
        (set, get) => ({
            client: null,
            userInfo: null,
            roomInfo: null,
            questionInfos: [],

            round: 1,
            questionIdx: -1,
            editor: null,
            sec: 99999999,

            setClient: (client: Client) => {set({ client: client })},
            getClient: () => {return get().client},

            setUserInfo: (userInfo: userInfo) => {set({ userInfo: userInfo })},
            getUserInfo: () => {return get().userInfo},

            setRoomInfo: (roomInfo: roomInfo) => {set({ roomInfo: roomInfo })},
            getRoomInfo: () => {return get().roomInfo},

            setQuestionInfos: (questionInfos: questionInfo[]) => {set({ questionInfos: questionInfos })},
            getQuestionInfos: () => {return get().questionInfos},

            setRound: (round: number) => {set({ round: round })},
            getRound: () => {return get().round},

            setSec: (sec: number) => {set({ sec: sec })},
            getSec: () => {return get().sec},

            setQuestionIdx: (idx: number) => {set({ questionIdx: idx })},
            getQuestionIdx: () => {return get().questionIdx},

            setEditor: (editor: monaco.editor.IStandaloneCodeEditor) => {set({ editor: editor })},
            getEditor: () => {return get().editor},

            clearRoom: () => {
                set({ client: null });
                set({ userInfo: null });
                set({ roomInfo: null });
            },
        }),

        //{
        //    name: 'room',
        //    partialize: (state) => ({ ...state, client: null, editor: null }),
        //},
    //),


);