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

                {/* Enlever http://localhost:8080/ lorsque l'app sera déployé !!! */}
                <a href="http://localhost:8080/oauth2/authorization/google">
                    <button className="connect-button">Connect with Google</button>
                </a>
            </div>
        </div>
    );
}

export default HomePage;
