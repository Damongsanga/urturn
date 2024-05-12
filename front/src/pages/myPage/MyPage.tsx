import {
	Card,
	Image,
	CardContent,
	CardDescription,
	CardHeader,
	Segment,
	SegmentGroup,
	Header,
	Pagination,
	Button,
	Modal, Input,
} from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import { HeaderBar } from '../../components/header/HeaderBar';
import {useEffect, useState} from 'react';
import ProgressBar from '@ramonak/react-progress-bar';
import './MyPage.css';
import {MemberInfo} from "../../types/memberInfoTypes.ts";
import {fetchMemberInfo, updateRepository} from "../../utils/api/memberAPI.ts";
import {useAxios} from "../../utils/useAxios.ts";

export const MyPage = () => {
	const axios = useAxios(true);
	const [memberInfo, setMemberInfo] = useState<MemberInfo | null>(null);
	const [repository, setRepository] = useState<string>('');
	const [modalOpen, setModalOpen] = useState(false);

	const [pageState, setPageState] = useState({
		activePage: 5,
		boundaryRange: 1,
		siblingRange: 1,
		showEllipsis: true,
		showFirstAndLastNav: true,
		showPreviousAndNextNav: true,
		totalPages: 50,
	});

	// 페이지 변경 핸들러 함수
	const handlePaginationChange = (_e: React.MouseEvent, { activePage }: any) => {
		setPageState((prevState) => ({
			...prevState,
			activePage: activePage,
		}));
	};

	useEffect(() => {
		const loadMemberInfo = async () => {
			try {
				const info = await fetchMemberInfo(axios);
				setMemberInfo(info);
			} catch (error) {
				console.error('Failed to fetch member info:', error);
			}
		};

		loadMemberInfo();
	}, []);

	const handleRepositoryUpdate = async () => {
		try {
			const updatedRepository = await updateRepository(axios, repository);
			// set 고려할 점... store?
			setMemberInfo(prevInfo => {
				if (!prevInfo) {
					return {
						id: null,
						nickname: null,
						profileImage: null,
						repository: updatedRepository,
						exp: null,
						level: null
					};
				}
				return { ...prevInfo, repository: updatedRepository }; 
			});
			setModalOpen(false); 
			console.log('Repository 업데이트 완료');
		} catch (error) {
			console.error('Failed to update repository:', error);
		}
	};
	return (
		<div className='MyPage'>
			{/* 헤더 */}
			<div className='MainHeader'>
				<HeaderBar $myPage={true}></HeaderBar>
			</div>
			<div className='MyRecord'>
				{/* 왼쪽 프로필 영역 */}
				<div className='Profile'>
					{/* 프로필 영역 카드 상단 - 개인정보 */}
					<Header as='h2' style={{ marginBottom: '0px' }}>
						내 정보
					</Header>
					<Card className='ProfileCard'>
						<Card className='MyProfile'>
							{/* 프로필 사진과 닉네임 */}
							<CardContent
								className='ContentBorder'
								style={{
									display: 'flex',
									flexDirection: 'column',
									justifyContent: 'center',
									alignItems: 'center',
								}}
							>
								<Image
									size='tiny'
									src='https://shiftpsh-blog.s3.amazonaws.com/uploads/2022/04/listing216.png'
								/>
								<CardHeader className='CardTextColor' textAlign='center' style={{ marginTop: '3vh' }}>
									한별이
								</CardHeader>
							</CardContent>
							{/* 프로필 영역 카드 하단 - 레벨 */}
							<CardContent className='ExpBar'>
								<CardHeader className='CardTextColor' textAlign='center'>
									레벨 : 
								</CardHeader>
								{/* 레벨 바 */}
								<div className='ExpBar'>
									<ProgressBar
										completed={80}
										width='24vw'
										height='2.5vh'
										baseBgColor='#A67A6A'
										bgColor='#5297FF'
										labelAlignment='center'
										labelSize='1.1rem'
										animateOnRender={true}
									/>
								</div>
							</CardContent>
							{/* 레벨 관련 문구 */}
							<CardContent className='ContentBorder'>
								<CardHeader className='CardTextColor' textAlign='center' style={{ marginTop: '2vh' }}>
									다음 레벨까지
								</CardHeader>
							</CardContent>
							{/* 깃허브 주소 */}
							<CardContent className='ContentBorder'>
								<CardDescription className='CardTextColor'>깃허브 레포지토리 주소 : {memberInfo?.repository}</CardDescription>
								<Button className='EditButton' floated='right'>
									수정
								</Button>
								<Button onClick={() => setModalOpen(true)}>수정</Button>
							</CardContent>
						</Card>
					</Card>
				</div>
				{/* 오른쪽 문제 풀이 기록 리스트 */}
				<div className='History'>
					<Header as='h2' style={{ marginBottom: '0px' }}>
						히스토리
					</Header>
					{/* 문제 기록 */}
					{/* 성공 */}
					<Segment className='Questions Success' size='small'>
						<Header as='h2' className='CardTextColor' style={{ marginBottom: '0px' }}>
							성공
						</Header>
						<Image
							size='tiny'
							src='https://item.kakaocdn.net/do/592728ea7408bcf69f797c0446b584a6d0bbab1214a29e381afae56101ded106'
							style={{ marginLeft: '2vw' }}
						/>
						<SegmentGroup className='QuestionName'>
							<Segment textAlign='right' className='CardTextColor Success'>
								문제1
							</Segment>
							<Segment textAlign='right' className='CardTextColor ContentBorder Success '>
								문제2
							</Segment>
						</SegmentGroup>
					</Segment>

					{/* 실패 */}
					<Segment className='Questions Fail' size='small'>
						<Header as='h2' className='CardTextColor' style={{ marginBottom: '0px' }}>
							실패
						</Header>
						<Image
							size='tiny'
							src='https://item.kakaocdn.net/do/592728ea7408bcf69f797c0446b584a6f604e7b0e6900f9ac53a43965300eb9a'
							style={{ marginLeft: '2vw' }}
						/>
						<SegmentGroup className='QuestionName'>
							<Segment textAlign='right' className='CardTextColor Fail'>
								문제1
							</Segment>
							<Segment textAlign='right' className='CardTextColor ContentBorder Fail'>
								문제2
							</Segment>
						</SegmentGroup>
					</Segment>
					<Pagination
						activePage={pageState.activePage}
						boundaryRange={pageState.boundaryRange}
						onPageChange={handlePaginationChange}
						size='mini'
						siblingRange={pageState.siblingRange}
						totalPages={pageState.totalPages}
						// Heads up! All items are powered by shorthands, if you want to hide one of them, just pass `null` as value
						ellipsisItem={pageState.showEllipsis ? undefined : null}
						firstItem={null}
						prevItem={pageState.showPreviousAndNextNav ? undefined : null}
						nextItem={pageState.showPreviousAndNextNav ? undefined : null}
						style={{ marginTop: '3vh' }}
					/>
				</div>
			</div>
			<Modal open={modalOpen} onClose={() => setModalOpen(false)} size='tiny'>
				<Modal.Header>레포지토리 주소 수정</Modal.Header>
				<Modal.Content>
					<Input
						fluid
						label='Repository URL'
						placeholder='Enter new repository URL'
						value={repository}
						onChange={(e) => setRepository(e.target.value)}
					/>
				</Modal.Content>
				<Modal.Actions>
					<Button onClick={() => setModalOpen(false)}>취소</Button>
					<Button positive onClick={handleRepositoryUpdate}>저장</Button>
				</Modal.Actions>
			</Modal>
		</div>
	);
};
