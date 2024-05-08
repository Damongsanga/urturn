import { useCallback } from 'react';
import { OpenVidu } from 'openvidu-browser';
import { useAxios } from '../utils/useAxios.ts';
import { useRtcStore } from "../stores/rtc.ts";

export function useOpenVidu() {
    const axios = useAxios();
    const rtcStore = useRtcStore();


    const getToken = async () => {
        console.log("get to");
        try {
            const response = await axios.post('/sessions');
            const sessionId = response.data;
            rtcStore.setSessionId(sessionId);
            const response2 = await axios.post('sessions/'+ sessionId+ '/connection');
            rtcStore.setConnectionId(response2.data.connectionId);
            console.log("sessionId: ", sessionId)
            console.log("respone2",response2.data);
            return response2.data.token;
        } catch (error) {
            console.error('Error fetching token:', error);
            throw error;
        }
    };

    const connect = useCallback(async () => {
        let OV = rtcStore.getOpenVidu();
        console.log("connect하고 ov", OV);
        console.log("OpenVidu 인스턴스:", typeof OV);
        if (OV===null) {
            OV = new OpenVidu();
            rtcStore.setOpenVidu(OV);
        }
        console.log("connect null인 경우 ov", OV);
        
        console.log("initSession 메서드 유무:", typeof OV.initSession);
        const session = OV.initSession();
        const token = await getToken();
        await session.connect(token);
        session.on('streamCreated', event => {
            const subscriber = session.subscribe(event.stream, 'video-container');
            console.log('New stream: ' + subscriber.stream.streamId);
        });
    }, [rtcStore.getOpenVidu, rtcStore.setOpenVidu, getToken]);
    //의존성 체크 필요
    
    // const disconnect = useCallback(() => {
    //     const sessionId = rtcStore.getSessionId();
    //     const OV = rtcStore.getOpenVidu();
    //
    // }, []);

    // useEffect(() => {
    //     return () => {
    //         disconnect();
    //     };
    // }, [disconnect]);

    return { connect };
}
