import './HomePage.css';

function HomePage() {
    return (
        <div className="homepage-container">
            <div className="homepage-box">
                <h1 className="homepage-title">Welcome to the Subscription Manager application</h1>
                <p className="homepage-description">
                    This application allows you to list your newsletters and easily unsubscribe from them.
                </p>
                <p className="homepage-instruction">
                    To get started, connect your Gmail account.
                </p>

                {/* Todo */}
                {/* http://localhost:8080/oauth2/authorization/google */}
                {/* https://subscription-manager-wpds.onrender.com//oauth2/authorization/google */}
                <a href="https://subscription-manager-wpds.onrender.com//oauth2/authorization/google">
                    <button className="connect-button">Connect with Google</button>
                </a>
            </div>
        </div>
    );
}

export default HomePage;
