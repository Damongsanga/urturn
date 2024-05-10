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
            code: null,
            editor: null,
            console: '',
            lang: 'JAVASCRIPT',
            sec: 99999999,
            pairProgramingMode: false,
            pairProgramingRole: null,

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
            
            setCode: (code: string) => {set({ code: code })},
            getCode: () => {return get().code},


            setConsole: (console: string) => {set({ console: console })},
            getConsole: () => {return get().console},

            setLang: (lang: string) => {set({ lang: lang })},
            getLang: () => {return get().lang},

            setPairProgramingMode: (mode: boolean) => {set({ pairProgramingMode: mode })},
            getPairProgramingMode: () => {return get().pairProgramingMode},

            setPairProgramingRole: (role: string | null) => {set({ pairProgramingRole: role })},
            getPairProgramingRole: () => {return get().pairProgramingRole},

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