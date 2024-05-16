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

export interface testcase{
    id: number,
    stdin: string,
    expectedOutput: string
} 

export interface questionInfo {
    algoQuestionId: number,
    algoQuestionUrl: string,
    algoQuestionTitle: string,
    algoQuestionContent: string,
    testcases: testcase[]
}

export interface reviewInfo {
    title: string
    content: string
}

export interface inputValue {
    id: number
    keep: string
    try: string
}

export interface roomState {
    client: Client | null,
    userInfo: userInfo | null,
    roomInfo: roomInfo | null,
    questionInfos: questionInfo[] | null
    reviewInfos: reviewInfo[][]
    inputValues: inputValue[]

    round: number
    questionIdx: number
    editor: monaco.editor.IStandaloneCodeEditor | null
    code: string | null
    console: string
    lang: string
    sec: number
    pairProgramingMode: boolean
    pairProgramingRole: string | null
    CurrentlySubmitting: boolean

    setClient: (client: Client) => void
    getClient: () => Client | null

    setUserInfo: (userInfo: userInfo) => void
    getUserInfo: () => userInfo | null

    setRoomInfo: (roomInfo: roomInfo) => void
    getRoomInfo: () => roomInfo | null
    
    setQuestionInfos: (questionInfos: questionInfo[]) => void
    getQuestionInfos: () => questionInfo[] | null

    setReviewInfos: (reviewInfos: reviewInfo[][]) => void
    getReviewInfos: () => reviewInfo[][]

    setInputValues: (inputValues: inputValue[]) => void
    getInputValues: () => inputValue[]

    setRound: (round: number) => void
    getRound: () => number

    setSec: (sec: number) => void
    getSec: () => number

    setQuestionIdx: (idx: number) => void
    getQuestionIdx: () => number

    setEditor: (editor: monaco.editor.IStandaloneCodeEditor) => void
    getEditor: () => monaco.editor.IStandaloneCodeEditor | null

    setCode: (code: string) => void
    getCode: () => string | null
    
    setConsole: (console: string) => void,
    getConsole: () => string,

    setLang: (lang: string) => void
    getLang: () => string

    setPairProgramingMode: (mode: boolean) => void
    getPairProgramingMode: () => boolean

    setPairProgramingRole: (role: string) => void
    getPairProgramingRole: () => string | null

    setCurrentlySubmitting: (submitting: boolean) => void
    getCurrentlySubmitting: () => boolean

    clearRoom: () => void
}