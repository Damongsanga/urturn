import {
	Button,
	Menu,
	MenuItem,
	Popup,
} from 'semantic-ui-react';
import './FooterBar.css';
import { useRoomStore } from '../../stores/room';
import { convertLangToUpper } from '../../utils/solve/convertProgramLang';
import { inputValue } from '../../types/roomTypes';

interface FooterProp {
	$mode?: number;
	$reviewValues?: inputValue[];
	$onClickFunc?: () => void;
}
/* 
헤더 규칙 prop
mode : 0이면 문제확인 / 1이면 ide / 2이면 회고
$pairMode : 페어프로그래밍 모드이면 true, 아니면 false
*/
export const FooterBar = ({ $mode, $reviewValues }: FooterProp) => {
	const roomStore = useRoomStore();

	const readyToSolve = () => {
		const button = document.getElementById('runButton') as HTMLButtonElement;
		if(button){
			button.disabled = true;
			button.textContent = '준비완료';
			//button.style.color = 'white';
			button.style.cursor = 'not-allowed';
		}

		const idx = roomStore.roomInfo?.host ? 0 : 1;
		console.log(roomStore.roomInfo?.roomId);
		console.log(roomStore.questionInfos?.[idx]?.algoQuestionId);
		console.log(roomStore.roomInfo?.host);

		roomStore.client?.publish({
			destination: '/app/readyToSolve',
			body: JSON.stringify({
				roomId: roomStore.roomInfo?.roomId,
				algoQuestionId: roomStore.questionInfos?.[idx]?.algoQuestionId,
				isHost: roomStore.roomInfo?.host,
			}),
		});
	};

	const submitCode = () => {
		console.log('code: ' + roomStore.getEditor()?.getValue());
		console.log('roomId: ' + roomStore.getRoomInfo()?.roomId);
		console.log('language: ' + convertLangToUpper(roomStore.getLang()));
		console.log('round: ' + roomStore.getRound());
		console.log('questionId: ' + roomStore.getQuestionInfos()?.[roomStore.getQuestionIdx()]?.algoQuestionId);
		console.log('host:' + roomStore.getRoomInfo()?.host);
		roomStore.setCurrentlySubmitting(true);
		roomStore.client?.publish({
			destination: '/app/submitCode',
			body: JSON.stringify({
				code: roomStore.getEditor()?.getValue(),
				roomId: roomStore.getRoomInfo()?.roomId,
				language: convertLangToUpper(roomStore.getLang()),
				round: roomStore.getRound(),
				algoQuestionId: roomStore.getQuestionInfos()?.[roomStore.getQuestionIdx()]?.algoQuestionId,
				isHost: roomStore.getRoomInfo()?.host,
				isPair: roomStore.getPairProgramingMode(),
			}),
		});
	};

	function submitReview(): void {
		roomStore.client?.publish({
			destination: '/app/submitRetro',
			body: JSON.stringify({
				roomId: roomStore.getRoomInfo()?.roomId,
				retroKeep1: $reviewValues?.[0].keep,
				retroTry1: $reviewValues?.[0].try,
				retroKeep2: $reviewValues?.[1].keep,
				retroTry2: $reviewValues?.[1].try,
			}),
		});
	}

	return (
		<>
			<Menu className='FooterBar' borderless>

				{/* 문제 확인에 사용할 버튼 */}
				{$mode === 0 && (
					<MenuItem name='RunButton' className='RunButton'  position='right'>
						<Button id='runButton' onClick={readyToSolve} size='large' className='RunButtonColor'>
							준비하기
						</Button>
					</MenuItem>
				)}

				{/* ide에 사용할 버튼 */}
				{$mode === 1 && (
					<MenuItem name='RunButton' className='RunButton' position='right'>
						<Button size='large' className='RunButtonColor'>
							코드 실행
						</Button>
						{
							roomStore.getPairProgramingRole() !== 'Navigator' ?
								roomStore.getSec() <=10 ?
									<Button disabled={true} size='large' className='RunButtonColor'>
										교체직전 제출불가
									</Button>
								:roomStore.getCurrentlySubmitting() ?
									<Button disabled={true} size='large' className='RunButtonColor'>
										<i className="loading spinner icon"></i>
										코드 채점중
									</Button>
								:
									<Button onClick={submitCode} size='large' className='RunButtonColor'>
										코드 제출하기
									</Button>
							:
								<Button disabled={true} size='large' className='RunButtonColor'>
									상대방 코드 작성중
								</Button>
						}
					</MenuItem>
				)}

				{/* 회고에 사용할 자리 */}
				{$mode === 2 && (
					<MenuItem name='RunButton' className='RunButton' position='right'>
						{roomStore.roomInfo?.host ? (
							<Button onClick={submitReview} size='large' className='ReviewButtonColor'>
								회고 저장하기
							</Button>
						) : (
							<Popup
								content='방장만 제출할 수 있습니다'
								trigger={
									<div>
										<Button
											disabled
											className='ReviewButtonColor'
										>
											회고 저장하기
										</Button>
									</div>
								}
							/>
						)}
					</MenuItem>
				)}
			</Menu>
		</>
	);
};
