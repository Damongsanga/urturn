package com.ssafy.urturn.room.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.CANNOT_ENTER_ROOM;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_ROOM;
import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_ROOMINFO;

import com.ssafy.urturn.global.cache.CacheDatas;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.util.MemberUtil;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import com.ssafy.urturn.problem.service.GradingService;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.room.RoomStatus;
import com.ssafy.urturn.room.dto.LeaveRoomDto;
import com.ssafy.urturn.room.dto.RoomInfoDto;
import com.ssafy.urturn.room.dto.RoomInfoResponse;
import com.ssafy.urturn.room.dto.UserInfoResponse;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService {
    private final GradingService gradingService;
    private final ProblemService problemService;
    private final CacheDatas cachedatas;
    private final ReentrantLock lock;
    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RoomInfoResponse createRoom(Long userId){
        // 방 ID
        String roomId= UUID.randomUUID().toString();
        // 입장코드
        String entryCode=UUID.randomUUID().toString().substring(0,6);

        // 방 정보 DTO 생성
        RoomInfoDto roominfodto=new RoomInfoDto();
        roominfodto.setManagerId(userId);
        roominfodto.setRoomStatus(RoomStatus.WAITING);

        // 초대코드 키로 방 ID 캐시
        cachedatas.cacheRoomId(entryCode,roomId);

        // 방 ID 키로 방정보 캐시
        cachedatas.cacheroomInfoDto(roomId,roominfodto);

        return new RoomInfoResponse(roomId, entryCode, true);

    }

    public UserInfoResponse getUserInfo(Long myUserId, Long pairId){
        Member myUser = memberRepository.findById(myUserId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        if (pairId == null){
            return UserInfoResponse.builder()
                    .myUserNickName(myUser.getNickname())
                    .myUserProfileUrl(myUser.getProfileImage()).build();
        }

        Member pair = memberRepository.findById(pairId).orElseThrow(() -> new RestApiException(NO_MEMBER));
        return UserInfoResponse.builder()
                .myUserNickName(myUser.getNickname())
                .myUserProfileUrl(myUser.getProfileImage())
                .pairNickName(pair.getNickname())
                .pairProfileUrl(pair.getProfileImage()).build();
    }

    public String canEnterRoom(String entryCode) {
        // 캐시된 방 ID 가져오기
        String roomId = cachedatas.cacheRoomId(entryCode);
        if (roomId == null) {
            throw new RestApiException(NO_ROOM);
        }

        // 방 정보 가져오기
        RoomInfoDto roomInfo = cachedatas.cacheroomInfoDto(roomId);
        if (roomInfo == null) {
            throw new RestApiException(NO_ROOMINFO);
        }

        // 방 상태 확인
        if (roomInfo.getRoomStatus() != RoomStatus.WAITING) {
            throw new RestApiException(CANNOT_ENTER_ROOM, "해당 방은 현재 " + roomInfo.getRoomStatus()+ " 상태입니다.");
        }

        // 참여자 ID 설정
        roomInfo.setPairId(MemberUtil.getMemberId());

        // 방 정보 업데이트
        cachedatas.cacheroomInfoDto(roomId, roomInfo);

        return roomId;
    }

    public void leaveRoom(LeaveRoomDto leaveRoomDto){
        // 방장 인 경우
        if(leaveRoomDto.isHost()){
            // 방 삭제.
            cachedatas.evictRoomInfoDto(leaveRoomDto.getRoomId());
        }else{
            // 방 정보에서 pairId null로 수정
            cachedatas.cacheroomInfoDto(leaveRoomDto.getRoomId()).setPairId(null);
        }
    }

}
