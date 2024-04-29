import { Client } from "@stomp/stompjs";

export interface player {
    profileImgUrl: string,
    nickname: string
}

export interface roomState {
    client: Client | null,
    players: player[],
    


    createRoom: () => void
    clearRoom: () => void
}