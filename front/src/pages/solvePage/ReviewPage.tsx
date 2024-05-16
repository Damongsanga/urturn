import { ChangeEvent, useEffect, useState } from 'react';
import { Allotment } from 'allotment';
import {
	Dropdown,
	Form,
	Menu,
	MenuItem,
	Table,
	TableBody,
	TableCell,
	TableHeader,
	TableHeaderCell,
	TableRow,
	TextArea,
} from 'semantic-ui-react';
import { HeaderBar } from '../../components/header/HeaderBar';
import { FooterBar } from '../../components/footer/FooterBar';
import { QuestionSideBar } from '../../components/questionSideBar/QuestionSideBar';
import CodeEditor from '../../components/solve/CodeEditor';

import 'allotment/dist/style.css';
import './SolvePage.css';
import { useRoomStore } from '../../stores/room';
import Markdown from 'markdown-to-jsx';

const langOptions = [
	{ key: 'C++', text: 'C++', value: 'C++' },
	{ key: 'Java', text: 'Java', value: 'Java' },
	{ key: 'Python', text: 'Python', value: 'Python' },
	{ key: 'JavaScript', text: 'JavaScript', value: 'JavaScript' },
  ]
let roundOptions = [
	{ key: '', text: '', value: 0 }
];

export default function ReviewPage() {
	const roomStore = useRoomStore();

	const [activeItem, setActiveItem] = useState('keep');
	const [activeQuestion, setActiveQuestion] = useState(1);
	const [activeRound, setActiveRound] = useState(0);
	const [review, setReview] = useState({ keep: true, try: false });
	const [inputValues, setInputValues] = useState([
		{ id: 1, keep: '', try: '' },
		{ id: 2, keep: '', try: '' }
	]);
	// 문제 별 회고 저장

	useEffect(() => {
		roundOptions = [];
		const oneReviewInfos = roomStore.getReviewInfos()[activeQuestion-1] 
		for(let i = 0; i < oneReviewInfos.length; i++){
			roundOptions.push({
				key: oneReviewInfos[i].title,
				text: oneReviewInfos[i].title,
				value: i
			});
		}
	}, [activeQuestion])

	useEffect(() => {
		try{
			roomStore.setLang(roomStore.getReviewInfos()[activeQuestion-1][activeRound].language);
			roomStore.getEditor()?.setValue(roomStore.getReviewInfos()[activeQuestion-1][activeRound].content);
		}
		catch(e){
			roomStore.getEditor()?.setValue('');
		}
		
	}, [activeQuestion, activeRound])

	// keep try 전환 함수
	const toggleReview = (type: 'keep' | 'try'): void => {
		setReview((prev) => ({
			...prev,
			[type]: true,
			// 다른 키값의 boolean을 반대로 설정
			[type === 'keep' ? 'try' : 'keep']: false,
		}));
		setActiveItem(type);
	};

	// 모든 입력 필드를 처리하는 함수
	const handleInputChange = (event: ChangeEvent<HTMLTextAreaElement>, questionId: number, fieldName: string) => {
		setInputValues(currentValues =>
			currentValues.map(item =>
				item.id === questionId ? { ...item, [fieldName]: event.target.value } : item
			)
		);
		console.log(inputValues)
	};
	
	return (
		<div className='Page'>
			<HeaderBar $review={true} $mode={1} />
			<div>
				<div style={{ display: 'flex', height: 'calc(100vh - 140px)', width: '100vw' }}>
					{/* 문제 사이드바 */}
					<div className='QuestionSideBars'>
						<Menu
							secondary
							icon
							vertical
							borderless
							style={{ marginTop: '10vh', backgroundColor: 'transparent' }}
						>
							<MenuItem
								className='QuestionButton'
								name='1'
								active={activeQuestion === 1}
								onClick={() => {setActiveQuestion(1); toggleReview('keep')}}
								style={{ marginBottom: '5vh' }}
							></MenuItem>

							<MenuItem
								className='QuestionButton'
								name='2'
								onClick={() => {setActiveQuestion(2); toggleReview('keep')}}
								active={activeQuestion === 2}
							></MenuItem>
						</Menu>
						{/* 마이크, 참여자 프로필 컴포넌트 */}
						<QuestionSideBar></QuestionSideBar>
					</div>
					<Allotment>
						<Allotment.Pane minSize={350}>
							<div className='HeaderBar' style={{ height: '70px' }}>
								<div
									style={{
										width: '98%',
										textAlign: 'left',
										fontSize: '20px',
										fontWeight: 'bold',
										color: 'white',
									}}
								>
									{roomStore.questionInfos && activeQuestion > 0 && roomStore.questionInfos[activeQuestion - 1].algoQuestionTitle}
								</div>
							</div>
							<div style={{ height: '100%', overflowY: 'auto', padding:'12px', fontSize:'1.3rem' }}>
							{
								roomStore.questionInfos && activeQuestion > 0 &&
								<Markdown>{roomStore.questionInfos[activeQuestion - 1].algoQuestionContent}</Markdown>
							}
							<div className='testcase'>
									<Table>
										<TableHeader>
											<TableRow>
												{/* W */}
												<TableHeaderCell>입력값</TableHeaderCell>
												<TableHeaderCell>출력값</TableHeaderCell>
											</TableRow>
										</TableHeader>

										<TableBody>
											{
												roomStore.questionInfos &&
													roomStore.questionInfos[activeQuestion - 1].testcases.map((testcase, i) => (
														<TableRow key={i}>
															{/* <TableCell>{i + 1} 번 테스트 케이스</TableCell> */}
															<TableCell>{testcase.stdin}</TableCell>
															<TableCell>{testcase.expectedOutput}</TableCell>
														</TableRow>
													))
											}
										</TableBody>
									</Table>
								</div>
								<br/><br/><br/><br/>
							</div>
						</Allotment.Pane>
						<Allotment.Pane minSize={350}>
							<div className='HeaderBar' style={{ height: '70px' }}>
								<div
									style={{
										width: '98%',
										textAlign: 'left',
										fontSize: '20px',
										fontWeight: 'bold',
										color: 'white',
									}}
								>
									{roomStore.getLang()} &nbsp;&nbsp;&nbsp; 
									<Dropdown
										search
										defaultValue={roundOptions[0].value}
										searchInput={{ type: 'string' }}
										options={roundOptions}
										onChange={(_e, { value }) => {setActiveRound(value as number);}}
									/>
								</div>
							</div>
							<Allotment vertical>
								<Allotment.Pane minSize={5}>
									<div style={{ height: '100%', width: '100%' }}>
										<CodeEditor/>
									</div>
								</Allotment.Pane>
								<Allotment.Pane minSize={125}>
									<div className='ReviewBar' style={{ height: '50px', backgroundColor: '#000034' }}>
										<div
											style={{
												width: '98%',
												textAlign: 'left',
												fontSize: '20px',
												fontWeight: 'bold',
												color: 'white',
											}}
										>
											<Menu tabular style={{ marginLeft: '1vw', paddingTop: '1vh'}}>
												<MenuItem
													name='좋았던 점'
													active={activeItem === 'keep'}
													onClick={() => toggleReview('keep')}
												/>
												<MenuItem
													name='아쉬운 점'
													active={activeItem === 'try'}
													onClick={() => toggleReview('try')}
												/>
											</Menu>
										</div>
									</div>
									<div>
										{review.keep && (
											<Form>
												<TextArea
													placeholder='좋았던 점을 적어주세요.'
													style={{ width: '100%', height: '25.2vh', resize: 'none', fontSize:'1.1rem' }}
													value={inputValues.find(item => item.id === activeQuestion)?.keep}
													onChange={(e) => handleInputChange(e, activeQuestion, 'keep')}
												/>{' '}
											</Form>
										)}
										{review.try && (
											<Form>
												<TextArea
													placeholder='아쉬웠던 점을 적어주세요.'
													style={{ width: '100%', height: '25.2vh', resize: 'none', fontSize:'1.1rem' }}
													value={inputValues.find(item => item.id === activeQuestion)?.try}
													onChange={(e) => handleInputChange(e, activeQuestion, 'try')}
												/>{' '}
											</Form>
										)}
									</div>
								</Allotment.Pane>
							</Allotment>
						</Allotment.Pane>
					</Allotment>
				</div>
			</div>
			{/* footer */}
			<FooterBar $mode={2} $reviewValues={inputValues}/>
		</div>
	);
}

// <div>
//       <Dropdown
//           search
//           defaultValue={langOptions[0].value}
//           searchInput={{ type: 'string' }}
//           selection
//           options={langOptions}
//           style={{ backgroundColor: 'green' }}
//         />
//       </div>
