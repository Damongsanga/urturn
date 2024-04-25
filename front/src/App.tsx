import { useState } from 'react';

import { LandingPage } from './pages/landingPage/LandingPage';
import { WaitingPage } from './pages/waitingPage/WaitingPage';
import { HeaderBar } from './components/header/HeaderBar';

import './App.css';
import 'semantic-ui-css/semantic.min.css';

function App() {
	const [isModal, setModal] = useState(false);

	const changeModal = () => {
		setModal(!isModal);
		console.log(isModal);
	};

	return (
		<>
			<LandingPage></LandingPage>
			<HeaderBar $main={true}></HeaderBar>
			<HeaderBar $ide={true} $mode={1}></HeaderBar>
			<HeaderBar $ide={true} $mode={2}></HeaderBar>
			<div>
				<button onClick={() => changeModal()}> 방 만들기 </button>
			</div>
			{isModal && <WaitingPage changeModal={changeModal}></WaitingPage>}
		</>
	);
}

export default App;
