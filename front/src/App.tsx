import 'semantic-ui-css/semantic.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import  MainPage  from './pages/mainPage/MainPage.tsx';

function App() {

	return (
		<>
			<BrowserRouter>
				<Routes>
					<Route path='/main' element={<MainPage />} />
				</Routes>
			</BrowserRouter>
		</>
	);
}

export default App;
