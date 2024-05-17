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
	Modal,
	Input,
} from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import { HeaderBar } from '../../components/header/HeaderBar';
import { useEffect, useState } from 'react';
// import ProgressBar from '@ramonak/react-progress-bar';
import './MyPage.css';
import { MemberInfo } from '../../types/memberInfoTypes.ts';
import { fetchMemberInfo, updateRepository } from '../../utils/api/memberAPI.ts';
import { useAxios } from '../../utils/useAxios.ts';
import { Popup } from 'semantic-ui-react';
import { HistoryEntry } from '../../types/historyTypes.ts';
import { fetchHistory } from '../../utils/api/historyAPI.ts';

export const MyPage = () => {
	const axios = useAxios(true);
	const [memberInfo, setMemberInfo] = useState<MemberInfo | null>(null);
	const [repository, setRepository] = useState<string>('');
	const [modalOpen, setModalOpen] = useState(false);
	const [showPopup, setShowPopup] = useState(false);
	const [pageState, setPageState] = useState({
		activePage: 1,
		totalPages: 0,
	});
	const [historyData, setHistoryData] = useState<HistoryEntry[]>([]);
	// const [page, setPage] = useState(0);
	// const [totalPages, setTotalPages] = useState(0);

	// 페이지 변경 핸들러 함수
	const handlePaginationChange = (_e: React.MouseEvent, { activePage }: any) => {
		setPageState((prevState) => ({
			...prevState,
			activePage: activePage,
		}));
	};
	useEffect(() => {
		const loadHistoryData = async () => {
			try {
				const result = await fetchHistory(pageState.activePage - 1, 3, axios);
				setHistoryData(result.content);
				setPageState((prev) => ({
					...prev,
					totalPages: result.totalPages,
				}));
			} catch (error) {
				console.error('Failed to fetch history:', error);
			}
		};

		loadHistoryData();
		console.log(historyData);
	}, [pageState.activePage]);

	useEffect(() => {
		const loadMemberInfo = async () => {
			try {
				const info = await fetchMemberInfo(axios);
				setMemberInfo(info);
				console.log(info);
			} catch (error) {
				console.error('Failed to fetch member info:', error);
			}
		};

		loadMemberInfo();
	}, [modalOpen]);

	const handleRepositoryUpdate = async () => {
		console.log('repo ', repository);
		if (!repository) {
			alert('repository 명을 입력 해 주세요');
			return;
		}
		try {
			const updatedRepository = await updateRepository(axios, repository);
			// set 고려할 점... store?
			setMemberInfo((prevInfo) => {
				if (!prevInfo) {
					return {
						id: null,
						nickname: null,
						profileImage: null,
						repository: updatedRepository,
						exp: null,
						level: null,
					};
				}
				return { ...prevInfo, repository: updatedRepository };
			});
			setModalOpen(false);
			console.log('Repository 업데이트 완료');
		} catch (error) {
			console.error('Fail 레포 업데이트:', error);
		}
	};

	useEffect(() => {
		if (modalOpen) {
			// 모달이 열리면 팝업 감추기
			setShowPopup(false);
		} else {
			// 모달이 닫히면 팝업 상태를 원래 로직에 따라 설정
			if (memberInfo?.repository === '' || memberInfo?.repository === null) {
				setShowPopup(true);
			} else {
				setShowPopup(false);
			}
		}
	}, [modalOpen, repository, memberInfo?.repository]);

	const getSegmentClassName = (result: string) => {
		switch (result) {
			case 'SUCCESS':
				return 'Success';
			case 'FAILURE':
				return 'Fail';
			case 'SURRENDER':
				return 'surrender';
			default:
				return '';
		}
	};

	// const formatLevel = (level: string | null | undefined) => {
	// 	if (!level) return "";
	// 	return level.replace(/(level)(\d+)/i, '$1 $2');
	// };
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
					<Card className='ProfileCard' style={{ marginBottom: '8vh' }}>
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
								<Image size='tiny' src={memberInfo?.profileImage} />
								<CardHeader className='CardTextColor' textAlign='center' style={{ marginTop: '3vh' }}>
									{memberInfo?.nickname}
								</CardHeader>
							</CardContent>
							{/* 프로필 영역 카드 하단 - 레벨 */}
							{/*<CardContent className='ExpBar'>*/}
							{/*	<CardHeader className='CardTextColor' textAlign='center'>*/}
							{/*		레벨 : {formatLevel(memberInfo?.level)}*/}
							{/*	</CardHeader>*/}
							{/*	/!* 레벨 바 *!/*/}
							{/*	<div className='ExpBar'>*/}
							{/*		<ProgressBar*/}
							{/*			completed={77}*/}
							{/*			width='24vw'*/}
							{/*			height='2.5vh'*/}
							{/*			baseBgColor='#A67A6A'*/}
							{/*			bgColor='#5297FF'*/}
							{/*			labelAlignment='center'*/}
							{/*			labelSize='1.1rem'*/}
							{/*			animateOnRender={true}*/}
							{/*		/>*/}
							{/*	</div>*/}
							{/*</CardContent>*/}
							{/* 레벨 관련 문구 */}
							{/*<CardContent className='ContentBorder'>*/}
							{/*	<CardHeader className='CardTextColor' textAlign='center' style={{ marginTop: '2vh' }}>*/}
							{/*		다음 레벨까지*/}
							{/*	</CardHeader>*/}
							{/*</CardContent>*/}
							{/* 깃허브 주소 */}
							<CardContent className='ContentBorder'>
								<CardHeader className='CardTextColor' textAlign='center'>
									Github Repository
								</CardHeader>
								{/*null 일때 텍스트 중요 말풍선으로 빼는 식?*/}
								<CardDescription className='CardTextColor' textAlign='center'>
									{memberInfo?.repository ? memberInfo.repository : '레포지토리를 등록해 주세요'}
								</CardDescription>
								<Popup
									content='회고를 업로드할 레포지토리를 생성 후 등록해 주세요'
									open={showPopup}
									position='top center'
									trigger={
										<Button
											className='EditButton'
											floated='right'
											onClick={() => setModalOpen(true)}
										>
											등록
										</Button>
									}
								/>
							</CardContent>
						</Card>
					</Card>
				</div>
				{/* 오른쪽 문제 풀이 hystory 리스트 */}
				<div className='History'>
					<Header as='h2' style={{ marginBottom: '0px' }}>
						히스토리
					</Header>
					{/* 문제 기록 */}
					{/* 성공 */}
					{/*<Segment className='Questions Success' size='small'>*/}
					{/*	<Header as='h2' className='CardTextColor' style={{ marginBottom: '0px' }}>*/}
					{/*		성공*/}
					{/*	</Header>*/}
					{/*	<Image*/}
					{/*		size='tiny'*/}
					{/*		src='https://item.kakaocdn.net/do/592728ea7408bcf69f797c0446b584a6d0bbab1214a29e381afae56101ded106'*/}
					{/*		style={{ marginLeft: '2vw' }}*/}
					{/*	/>*/}
					{/*	<SegmentGroup className='QuestionName'>*/}
					{/*		<Segment textAlign='right' className='CardTextColor Success'>*/}
					{/*			문제1*/}
					{/*		</Segment>*/}
					{/*		<Segment textAlign='right' className='CardTextColor ContentBorder Success '>*/}
					{/*			문제2*/}
					{/*		</Segment>*/}
					{/*	</SegmentGroup>*/}
					{/*</Segment>*/}

					{/*/!* 실패 *!/*/}
					{/*<Segment className='Questions Fail' size='small'>*/}
					{/*	<Header as='h2' className='CardTextColor' style={{ marginBottom: '0px' }}>*/}
					{/*		실패*/}
					{/*	</Header>*/}
					{/*	<Image*/}
					{/*		size='tiny'*/}
					{/*		src='https://item.kakaocdn.net/do/592728ea7408bcf69f797c0446b584a6f604e7b0e6900f9ac53a43965300eb9a'*/}
					{/*		style={{ marginLeft: '2vw' }}*/}
					{/*	/>*/}
					{/*	<SegmentGroup className='QuestionName'>*/}
					{/*		<Segment textAlign='right' className='CardTextColor Fail'>*/}
					{/*			문제1*/}
					{/*		</Segment>*/}
					{/*		<Segment textAlign='right' className='CardTextColor ContentBorder Fail'>*/}
					{/*			문제2*/}
					{/*		</Segment>*/}
					{/*	</SegmentGroup>*/}
					{/*</Segment>*/}
					{historyData.length > 0 ? (
						historyData.map((entry, index) => (
							<Segment
								key={index}
								className={`Questions ${getSegmentClassName(entry.result)}`}
								size='small'
							>
								<div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
									<Header as='h2' className='CardTextColor' style={{ marginBottom: '0px' }}>
										{entry.result}
									</Header>
									<Header as='h4' className='CardTextColor' style={{ marginTop: '5px' }}>
										{'with '}
										{entry.pair.nickname}
									</Header>
								</div>
								<Image size='tiny' src={entry.pair.profileImage} style={{ marginLeft: '2vw' }} />
								<SegmentGroup className='QuestionName'>
									<Segment
										className={`CardTextColor ${getSegmentClassName(entry.result)} QuestionAlign`}
									>
										<span>{entry.problem1.title}</span>
										<span>{entry.problem1.level}</span>
									</Segment>
									<Segment
										className={`CardTextColor ContentBorder ${getSegmentClassName(
											entry.result,
										)} QuestionAlign`}
									>
										<span>{entry.problem2.title}</span>
										<span>{entry.problem2.level}</span>
									</Segment>
								</SegmentGroup>
							</Segment>
						))
					) : (
						<div style={{ textAlign: 'center', marginTop: '20px' }}>
							<p>서비스 이용 기록이 없습니다.</p>
							<p>어서 이용해 보세요!</p>
						</div>
					)}
					{/*<Pagination*/}
					{/*	activePage={pageState.activePage}*/}
					{/*	boundaryRange={pageState.boundaryRange}*/}
					{/*	onPageChange={handlePaginationChange}*/}
					{/*	size='mini'*/}
					{/*	siblingRange={pageState.siblingRange}*/}
					{/*	totalPages={pageState.totalPages}*/}
					{/*	// Heads up! All items are powered by shorthands, if you want to hide one of them, just pass `null` as value*/}
					{/*	ellipsisItem={pageState.showEllipsis ? undefined : null}*/}
					{/*	firstItem={null}*/}
					{/*	prevItem={pageState.showPreviousAndNextNav ? undefined : null}*/}
					{/*	nextItem={pageState.showPreviousAndNextNav ? undefined : null}*/}
					{/*	style={{ marginTop: '3vh' }}*/}
					{/*/>*/}
					{pageState.totalPages > 0 && (
						<Pagination
							activePage={pageState.activePage}
							totalPages={pageState.totalPages}
							onPageChange={handlePaginationChange}
							size='mini'
							style={{ marginTop: '3vh' }}
						/>
					)}
				</div>
			</div>
			<Modal open={modalOpen} onClose={() => setModalOpen(false)} size='tiny'>
				<Modal.Header>Github Repository</Modal.Header>
				<Modal.Content>
					{/*label*/}

					<Input
						fluid
						label={`https://github.com/${memberInfo?.repository}/`}
						placeholder='Repository_name'
						value={repository}
						onChange={(e) => setRepository(e.target.value)}
					/>
				</Modal.Content>
				<Modal.Actions>
					<Button onClick={() => setModalOpen(false)}>취소</Button>
					<Button className='EditButton' onClick={handleRepositoryUpdate}>
						수정
					</Button>
				</Modal.Actions>
			</Modal>
		</div>
	);
};
