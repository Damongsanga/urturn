import { ChangeEvent, useState } from 'react';
import { Allotment } from 'allotment';
import {
	Dropdown,
	Form,
	Menu,
	MenuItem,
	TextArea,
} from 'semantic-ui-react';
import { HeaderBar } from '../../components/header/HeaderBar';
import { FooterBar } from '../../components/footer/FooterBar';
import { QuestionSideBar } from '../../components/questionSideBar/QuestionSideBar';
import CodeEditor from '../../components/solve/CodeEditor';

import 'allotment/dist/style.css';
import './SolvePage.css';


const langOptions = [
	{ key: '1', text: '1라운드', value: 'C++' },
	{ key: '2', text: '2라운드', value: 'Java' },
	{ key: '3', text: '3라운드', value: 'Python' },
	{ key: '4', text: '4라운드', value: 'JavaScript' },
];

export default function ReviewPage() {

	//const [fileContent, setFileContent] = useState('');

	//const nowIdxRef = useRef(-1);

	const [activeItem, setActiveItem] = useState('keep');
	const [activeQuestion, setActiveQuestion] = useState(1);
	const [review, setReview] = useState({ keep: true, try: false });
	const [inputValues, setInputValues] = useState([
		{ id: 1, keep: '', try: '' },
		{ id: 2, keep: '', try: '' }
	]);
	// 문제 별 회고 저장


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
									양과 늑대
								</div>
							</div>
							<div style={{ height: '100%', overflowY: 'auto', padding:'12px' }}>
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
									<Dropdown
										search
										defaultValue={langOptions[langOptions.length - 1].value}
										searchInput={{ type: 'string' }}
										options={langOptions}
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
													name='keep'
													active={activeItem === 'keep'}
													onClick={() => toggleReview('keep')}
												/>
												<MenuItem
													name='try'
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
													placeholder='Keep'
													style={{ width: '100%', height: '65.2vh', resize: 'none' }}
													value={inputValues.find(item => item.id === activeQuestion)?.keep}
													onChange={(e) => handleInputChange(e, activeQuestion, 'keep')}
												/>{' '}
											</Form>
										)}
										{review.try && (
											<Form>
												<TextArea
													placeholder='try'
													style={{ width: '100%', height: '65.2vh', resize: 'none' }}
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
			<FooterBar $mode={2}/>
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
