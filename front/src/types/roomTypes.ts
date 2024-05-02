import { Client } from "@stomp/stompjs";

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

export interface roomState {
    client: Client | null,
    userInfo: userInfo | null,
    roomInfo: roomInfo | null,

    createRoom: (token : string, userId : number) => void
    enterRoom: (token :string, userId : number, roomId : string) => void
    clearRoom: () => void
}