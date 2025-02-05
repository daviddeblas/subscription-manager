import './HomePage.css';

function HomePage() {
    return (
        <div className="homepage-container">
            <header className="homepage-header">
                <a href="https://github.com/daviddeblas/subscription-manager/tree/master" target="_blank"
                   rel="noopener noreferrer" className="github-link" aria-label="View Source on GitHub">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="32"
                        height="32"
                        viewBox="0 0 24 24"
                        fill="#333"
                        className="github-icon"
                    >
                        {/* GitHub icon with SVG*/}
                        <path d="M12 .5C5.73.5.5 5.73.5 12c0 5.08 3.29 9.39 7.86 10.93.58.11.79-.25.79-.56 0-.28-.01-1.02-.02-2-3.2.7-3.88-1.54-3.88-1.54-.53-1.35-1.3-1.71-1.3-1.71-1.06-.73.08-.72.08-.72 1.17.08 1.78 1.2 1.78 1.2 1.04 1.78 2.73 1.27 3.4.97.11-.75.41-1.27.75-1.56-2.55-.29-5.23-1.28-5.23-5.7 0-1.26.45-2.29 1.2-3.1-.12-.3-.52-1.52.12-3.17 0 0 .98-.31 3.2 1.2a11.2 11.2 0 013-.4c.99 0 1.99.13 3 .4 2.21-1.51 3.18-1.2 3.18-1.2.64 1.65.24 2.87.12 3.17.75.81 1.2 1.84 1.2 3.1 0 4.43-2.69 5.4-5.25 5.68.42.36.8 1.08.8 2.18 0 1.57-.01 2.83-.01 3.21 0 .31.21.68.8.56C20.71 21.39 24 17.08 24 12c0-6.27-5.23-11.5-12-11.5z"/>
                    </svg>
                </a>
            </header>

            <div className="homepage-box">
                <h1 className="homepage-title">Welcome to Subscription Manager</h1>
                <p className="homepage-description">
                    This personal project helps you manage your newsletter subscriptions effortlessly.
                    Identify, view, and unsubscribe from unwanted newsletters directly from your Gmail account.
                </p>
                <p className="homepage-instruction">
                    To get started, connect your Gmail account by clicking the button below.
                </p>
                <p className="homepage-note">
                    Note: The server is hosted on a free provider, so resources are limited and scans may take several minutes.
                </p>
                <p className="homepage-note">
                    <strong>Important:</strong> Unfortunately, the only way to use the application is to provide your Gmail address via email at&nbsp;
                    <a href="mailto:daviddeblas@protonmail.com">daviddeblas@protonmail.com</a>.
                    <br /><br />
                    This is because I have to run the Google environment in test mode, which means I must manually add each email address for testing.
                    I'm forced to do this since Google requires an expensive security certification (more than 500$) to launch the application in a production environment.
                    <br /><br />
                    Thank you for your understanding.
                </p>


                {/* Todo */}
                {/* http://localhost:8080/oauth2/authorization/google */}
                {/* https://subscription-manager-wpds.onrender.com/oauth2/authorization/google */}
                <a href="https://subscription-manager-wpds.onrender.com/oauth2/authorization/google" className="google-signin-button">
                    <svg className="google-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 533.5 544.3">
                        <path d="M533.5 278.4c0-18.5-1.6-37.2-4.7-55.1H272v104.3h147.4c-6.3 34-25.3 62.7-53.9 82v68.1h87.1c50.7-46.7 80.3-115.4 80.3-198.3z" fill="#4285F4"/>
                        <path d="M272 544.3c72.4 0 133.2-23.9 177.6-64.9l-87.1-68.1c-24.1 16.2-55.2 25.8-90.5 25.8-69.5 0-128.3-46.9-149.3-110.3H34.5v69.3C78.9 490.4 167.1 544.3 272 544.3z" fill="#34A853"/>
                        <path d="M122.7 324.5c-10.8-32.2-10.8-66.7 0-98.9V156.3H34.5c-37.8 75.5-37.8 164.4 0 239.9l88.2-71.7z" fill="#FBBC05"/>
                        <path d="M272 109.7c38.7 0 73.4 13.3 101.3 39.4l75.5-75.5C404.9 24.5 344.1 0 272 0 167.1 0 78.9 53.9 34.5 156.3l88.2 71.7C143.7 156.6 202.5 109.7 272 109.7z" fill="#EA4335"/>
                    </svg>
                    <span className="google-text">Sign in with Google</span>
                </a>

            </div>

            <div className="privacy-section">
                <h2>🔒 Data Usage and Privacy</h2>
                <ul>
                    <li><strong>No Data Retention</strong>: I do not permanently store any personal information or
                        email content. All data is processed in real-time and is not saved on my servers.</li>
                    <li><strong>Temporary Cache</strong>: To enhance your user experience, I use a cache that temporarily
                        stores your data for 30 minutes. This allows you to revisit your information without having to
                        perform a full scan again, reducing wait times.</li>
                    <li><strong>Data Security</strong>: Communications between your browser and my server are secured
                        via HTTPS, ensuring that your data is transmitted in an encrypted manner.</li>
                    <li><strong>Compliance with Gmail’s Privacy Policies</strong>: I strictly adhere to Gmail’s privacy
                        policies, ensuring that your personal information remains protected at all times.</li>
                </ul>
                <p>For more details, please refer to the <a href="/privacy-policy">Privacy Policy </a>
                    and the<a href="/terms-of-service"> Terms of Service</a>.
                </p>
            </div>

            {/* Footer */}
            <footer className="homepage-footer">
                <p>&copy; 2025 Subscription Manager. All rights reserved.</p>
            </footer>
        </div>
    );

}

export default HomePage;
