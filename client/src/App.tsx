import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage/HomePage.tsx';
import SubscriptionPage from './pages/SubscriptionPage/SubscriptionPage.tsx';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/subscriptions" element={<SubscriptionPage  />} />
            </Routes>
        </Router>
    );
}

export default App;
