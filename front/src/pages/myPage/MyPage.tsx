import {
	Button,
	Card,
	Grid,
	Image,
	GridRow,
	GridColumn,
	CardContent,
	CardDescription,
	CardHeader,
	CardMeta,
	Icon,
	CardGroup,
} from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import { HeaderBar } from '../../components/header/HeaderBar';
import './MyPage.css';

export const MyPage = () => {
	return (
		<div className='MyPage'>
			<div className='MainHeader'>
				<HeaderBar $main={true}></HeaderBar>
			</div>
			<div className='MyRecord'>
				<div className='Profile'>
					<Card className='ProfileCard'>
						<CardGroup stackable={true}>
							<Card className='MyProfile'>
								<CardContent>
									<CardHeader className='CardTextColor' textAlign='center'>
										내 정보
									</CardHeader>
								</CardContent>
								<CardContent
									style={{
										display: 'flex',
										justifyContent: 'center',
										alignItems: 'center',
									}}
								>
									<Image
										floated='left'
										size='tiny'
										src='https://shiftpsh-blog.s3.amazonaws.com/uploads/2022/04/listing216.png'
									/>
									<CardHeader
										className='CardTextColor'
										textAlign='center'
										style={{ marginLeft: '20vh' }}
									>
										한별이
									</CardHeader>
								</CardContent>
								<CardContent extra>
									<CardDescription className='CardTextColor'>깃허브 주소 :</CardDescription>
								</CardContent>
							</Card>
							<Card className='MyProfile'>
								<CardContent>
									<CardHeader textAlign='center'>현재 레벨</CardHeader>
									<CardMeta>
										<span className='date'>Joined in 2015</span>
									</CardMeta>
									<CardDescription>Matthew is a musician living in Nashville.</CardDescription>
								</CardContent>
								<CardContent extra>
									<CardDescription>
										Steve wants to add you to the group <strong>best friends</strong>
									</CardDescription>
								</CardContent>
							</Card>
						</CardGroup>
					</Card>
				</div>
				<div className='History'>기록</div>
			</div>
		</div>
	);
};
