import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage/HomePage.tsx';
import SubscriptionPage from './pages/SubscriptionPage/SubscriptionPage.tsx';
import PrivacyPolicy from "./pages/PrivacyPolicy/PrivacyPolicy";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/subscriptions" element={<SubscriptionPage  />} />
                <Route path={"/privacy-policy"} element={<PrivacyPolicy />} />
            </Routes>
        </Router>
    );
}

export default App;
