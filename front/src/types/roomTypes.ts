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

export interface questionInfo {
    algoQuestionId: number,
    algoQuestionUrl: string,
    algoQuestionTitle: string,
    algoQuestionContent: string,
}


export interface roomState {
    client: Client | null,
    userInfo: userInfo | null,
    roomInfo: roomInfo | null,
    questionInfos: questionInfo[] | null

    round: number
    questionIdx: number
    editor: monaco.editor.IStandaloneCodeEditor | null
    sec: number

    setClient: (client: Client) => void
    setUserInfo: (userInfo: userInfo) => void
    setRoomInfo: (roomInfo: roomInfo) => void
    setQuestionInfos: (questionInfos: questionInfo[]) => void

    setRound: (round: number) => void
    setSec: (sec: number) => void
    setQuestionIdx: (idx: number) => void
    setEditor: (editor: any) => void

    clearRoom: () => void
}