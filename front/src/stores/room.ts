import { Client } from '@stomp/stompjs';
import { create } from "zustand";
//import { persist } from 'zustand/middleware';
import { roomInfo, userInfo, questionInfo, roomState, reviewInfo, inputValue } from '../types/roomTypes';
import * as monaco from "monaco-editor";

const basicCode = 
`import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {

        
    }
}
`

export const useRoomStore = create<roomState>() (
    //persist(
        (set, get) => ({
            client: null,
            userInfo: null,
            roomInfo: null,
            questionInfos: [],
            reviewInfos: [],
            inputValues: [
                { id: 1, keep: '', try: '' },
                { id: 2, keep: '', try: '' }
            ],

            round: 1,
            questionIdx: -1,
            code: basicCode,
            editor: null,
            console: '',
            lang: 'java',
            sec: 99999999,
            pairProgramingMode: false,
            pairProgramingRole: null,
            CurrentlySubmitting: false,

            setClient: (client: Client) => {set({ client: client })},
            getClient: () => {return get().client},

            setUserInfo: (userInfo: userInfo) => {set({ userInfo: userInfo })},
            getUserInfo: () => {return get().userInfo},

            setRoomInfo: (roomInfo: roomInfo) => {set({ roomInfo: roomInfo })},
            getRoomInfo: () => {return get().roomInfo},

            setQuestionInfos: (questionInfos: questionInfo[]) => {set({ questionInfos: questionInfos })},
            getQuestionInfos: () => {return get().questionInfos},

            setReviewInfos: (reviewInfos: reviewInfo[][]) => {set({ reviewInfos: reviewInfos })},
            getReviewInfos: () => {return get().reviewInfos},

            setInputValues: (inputValues: inputValue[]) => {set({ inputValues: inputValues })},
            getInputValues: () => {return get().inputValues},

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

            setCurrentlySubmitting: (submitting: boolean) => {set({ CurrentlySubmitting: submitting })},
            getCurrentlySubmitting: () => {return get().CurrentlySubmitting},

            clearRoom: () => {
                try{get().client?.deactivate()}catch{};

                set({ client: null });
                set({ userInfo: null });
                set({ roomInfo: null });
                set({ questionInfos: [] });
                set({ reviewInfos: [] });
                set({ inputValues: [
                    { id: 1, keep: '', try: '' },
                    { id: 2, keep: '', try: '' }
                    ] });
                set({ round: 1 });
                set({ questionIdx: -1 });
                set({ editor: null });
                set({ code: basicCode });
                set({ console: '' });
                set({ lang: 'java' });
                set({ sec: 99999999 });
                set({ pairProgramingMode: false });
                set({ pairProgramingRole: null });
                set({ CurrentlySubmitting: false });
            },
        }),

        //{
        //    name: 'room',
        //    partialize: (state) => ({ ...state, client: null, editor: null }),
        //},
    //),


);