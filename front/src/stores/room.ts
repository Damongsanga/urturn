import { Client } from '@stomp/stompjs';
import { create } from "zustand";
import { persist } from 'zustand/middleware';
import { roomInfo, userInfo, questionInfo, roomState } from '../types/roomTypes';
import * as monaco from "monaco-editor";

export const useRoomStore = create<roomState>() (
    persist(
        (set) => ({
            client: null,
            userInfo: null,
            roomInfo: null,
            questionInfos: [],

            round: 1,
            questionIdx: -1,
            editor: null,
            sec: 99999999,

            setClient: (client: Client) => {set({ client: client })},
            setUserInfo: (userInfo: userInfo) => {set({ userInfo: userInfo })},
            setRoomInfo: (roomInfo: roomInfo) => {set({ roomInfo: roomInfo })},
            setQuestionInfos: (questionInfos: questionInfo[]) => {set({ questionInfos: questionInfos })},

            setRound: (round: number) => {set({ round: round })},
            setSec: (sec: number) => {set({ sec: sec })},
            setQuestionIdx: (idx: number) => {set({ questionIdx: idx })},
            setEditor: (editor: monaco.editor.IStandaloneCodeEditor) => {set({ editor: editor })},

            clearRoom: () => {
                set({ client: null });
                set({ userInfo: null });
                set({ roomInfo: null });
            },
        }),

        {
            name: 'room',
            partialize: (state) => ({ ...state, client: null, editor: null }),
        },
    ),


);