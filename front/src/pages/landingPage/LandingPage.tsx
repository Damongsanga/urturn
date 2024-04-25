import { MenuItem, Menu, Header, Icon, Button } from 'semantic-ui-react';
import logo from '../../assets/images/logo.svg';
import './LandingPage.css';

export const LandingPage = () => {
	return (
		<>
			<div className='LandingPage'>
				<div className='Content'>
					{/* 헤더 */}
					<div className='HeaderSection'>
						<Menu secondary size='large'>
							<MenuItem>
								<img alt='URTurn' src={logo} style={{ width: '125px' }} />
							</MenuItem>
							<MenuItem name='Waiting Room' position='right'>
								<Button circular className='ButtonColor'>
									<Icon name='github' size='big' /> 로그인
								</Button>
							</MenuItem>
						</Menu>
					</div>

					<div className='VideoSection'>
						<video
							src='https://youtu.be/rub7vg6YG7k?si=5-SYZemFFNfS9Cfp'
							controls
							style={{ width: '40vw' }}
						></video>
					</div>

					<div className='CTASection'>
						<Header as='h3' textAlign='left' style={{ marginBottom: '2vh'}}>
							협업 능력을 향상시키는 <br />
							URTurn에서 함께 성장하세요!
						</Header>
						<Button circular className='ButtonColor'>
							<Icon name='github' size='huge' /> <span className='FontSize'>Github로 시작하기</span>
						</Button>
					</div>
				</div>
			</div>
		</>
	);
};
