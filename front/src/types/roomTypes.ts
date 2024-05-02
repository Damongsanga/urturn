import { Client } from "@stomp/stompjs";
import { NavigateFunction } from "react-router-dom";

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
    algoQuestionUrl: string
}


export interface roomState {
    navigate: NavigateFunction | null,
    client: Client | null,
    userInfo: userInfo | null,
    roomInfo: roomInfo | null,
    questionInfos: questionInfo[] | null

    setNavigate: (navigate: NavigateFunction) => void

    createRoom: (token : string, userId : number) => void
    enterRoom: (token :string, userId : number, roomId : string) => void
    clearRoom: () => void
}