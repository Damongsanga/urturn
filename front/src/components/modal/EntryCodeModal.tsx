import { useState } from 'react';
import { Menu, MenuItem, Header, Icon, Input, Button } from 'semantic-ui-react';
import { useAxios } from '../../utils/useAxios';
import { useRoomStore } from '../../stores/room';
import { useAuthStore } from '../../stores/useAuthStore';
import './EntryCodeModal.css';
import { webSocketConnect } from '../../utils/solve/webSocketConnect';
import { useNavigate } from 'react-router-dom';

interface ModalProps {
	changeModal: () => void;
	successConnect: () => void;
}

export const EntryCodeModal = ({ changeModal, successConnect }: ModalProps) => {
	const [entryCode, setEntryCode] = useState('');
	const axios = useAxios();
	const navigate = useNavigate();
	const roomStore = useRoomStore();
	const authStore = useAuthStore();

	const checkCode = () => {
		console.log("참가자 checkCode 디버그");
		console.log(authStore.accessToken);
		console.log(authStore.memberId);
		console.log(entryCode);
		axios.get(`/room/enter/${entryCode}`)
		.then((res: { data: string }) => {
			const roomId = res.data;
			if(authStore.memberId === undefined)  { console.log("로그인 해야됩니다."); return;}
			webSocketConnect(navigate, authStore, roomStore, roomId);
			successConnect();
		})
		.catch(() => {
			alert("존재하지 않는 방입니다.");
		})
	}

	return (
		<>
			<div className='EntryCodeModalBackground'>
				<div className='EntryCodeModal'>
					<div className='EntryContent'>
						<div className='Title TitleSection'>
							{/* 상위 정보 메뉴 */}
							<Menu secondary size='large' className='TitleAlign Title'>
								<MenuItem name='Entry Code'>
									<Header as='h3' textAlign='center'>
										입장 코드 입력
									</Header>
								</MenuItem>
								<MenuItem name='close' position='right' onClick={changeModal}>
									<Icon className='Icon' name='close' size='large' />
								</MenuItem>
							</Menu>
						</div>
            {/* 입장 코드 입력 input폼 */}
						<div className='CodeInputForm'>
							<Input onChange={(e) => setEntryCode(e.target.value)} placeholder='입장 코드 입력' />
						</div>
            {/* 입장 버튼 */}
						<div className='InputButton'>
							<Button onClick={checkCode}>입장하기</Button>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};
