import { OpenVidu } from 'openvidu-browser';

export interface a {

}


export interface rtcState {
    sessionId: string | null;
    connectionId: string | null;
    connectToken: string | null;

    ov: OpenVidu | null;

    createConnection: () => boolean;
    joinConnection: () => boolean;




    clearRtc: () => void
}