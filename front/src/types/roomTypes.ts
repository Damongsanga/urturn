import { Client } from "@stomp/stompjs";
import * as monaco from "monaco-editor";

export interface roomInfo {
    roomId: number,
    entryCode: string,
    host: boolean,
}

export interface userInfo {
    myUserProfileUrl: string,
    myUserNickName: string
    relativeUserProfileUrl: string,
    relativeUserNickName: string
}

export interface testCase{
    id: number,
    stdin: string,
    expectedOutput: string
} 

export interface questionInfo {
    algoQuestionId: number,
    algoQuestionUrl: string,
    algoQuestionTitle: string,
    algoQuestionContent: string,
    testCases: testCase[]
}


export interface roomState {
    client: Client | null,
    userInfo: userInfo | null,
    roomInfo: roomInfo | null,
    questionInfos: questionInfo[] | null

    round: number
    questionIdx: number
    editor: monaco.editor.IStandaloneCodeEditor | null
    lang: string
    sec: number

    setClient: (client: Client) => void
    getClient: () => Client | null

    setUserInfo: (userInfo: userInfo) => void
    getUserInfo: () => userInfo | null

    setRoomInfo: (roomInfo: roomInfo) => void
    getRoomInfo: () => roomInfo | null
    
    setQuestionInfos: (questionInfos: questionInfo[]) => void
    getQuestionInfos: () => questionInfo[] | null

    setRound: (round: number) => void
    getRound: () => number

    setSec: (sec: number) => void
    getSec: () => number

    setQuestionIdx: (idx: number) => void
    getQuestionIdx: () => number

    setEditor: (editor: any) => void
    getEditor: () => monaco.editor.IStandaloneCodeEditor | null

    setLang: (lang: string) => void
    getLang: () => string

    clearRoom: () => void
}