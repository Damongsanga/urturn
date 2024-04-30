import { Menu, MenuItem, Header, Icon, Input, Button } from 'semantic-ui-react';
import './EntryCodeModal.css';

interface ModalProps {
	changeModal: () => void;
	// 모달을 닫는 함수
}

export const EntryCodeModal = ({ changeModal }: ModalProps) => {
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
							<Input placeholder='입장 코드 입력' />
						</div>
            {/* 입장 버튼 */}
						<div className='InputButton'>
							<Button>입장하기</Button>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};
