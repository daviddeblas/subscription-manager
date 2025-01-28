import './PrivacyPolicy.css';

function PrivacyPolicy() {
    return (
        <div className="privacy-container">
            <header className="privacy-header">
                <h1>Privacy Policy</h1>
                <p>Last updated: January 27, 2025</p>
            </header>

            <main className="privacy-main">
                <section className="privacy-section">
                    <h2>1. Introduction</h2>
                    <p>
                        Welcome to Subscription Manager. I prioritize your privacy and am committed to protecting
                        your personal information.
                    </p>
                </section>

                <section className="privacy-section">
                    <h2>2. Data I Collect</h2>
                    <p>
                        I only collect data necessary to provide my services. This includes:
                    </p>
                    <ul>
                        <li>Your Gmail account information (accessed via OAuth2)</li>
                        <li>Temporary cache data for 30 minutes to improve user experience</li>
                    </ul>
                </section>

                <section className="privacy-section">
                    <h2>3. How I Use Your Data</h2>
                    <p>
                        Your data is used solely to:
                    </p>
                    <ul>
                        <li>Scan your inbox for newsletter subscriptions</li>
                        <li>Allow you to manage and unsubscribe from newsletters</li>
                    </ul>
                </section>

                <section className="privacy-section">
                    <h2>4. Data Retention</h2>
                    <p>
                        I do not permanently store any personal information or email content.
                        All data is processed in real-time and is not saved on my servers.
                    </p>
                </section>

                <section className="privacy-section">
                    <h2>5. Data Security</h2>
                    <p>
                        All communications between your browser and my server are secured via HTTPS, ensuring that your
                        data is transmitted securely.
                    </p>
                </section>

                <section className="privacy-section">
                    <h2>6. Compliance with Policies</h2>
                    <p>
                        I strictly adhere to Gmail’s privacy policies, ensuring that your personal information remains
                        protected at all times.
                    </p>
                </section>

                <section className="privacy-section">
                    <h2>7. Changes to This Privacy Policy</h2>
                    <p>
                        I may update my Privacy Policy from time to time. I will notify you of any changes by posting
                        the new Privacy Policy on this page.
                    </p>
                </section>

                <section className="privacy-section">
                    <h2>8. Contact Me</h2>
                    <p>
                        If you have any questions about this Privacy Policy, please contact me at
                        <a href="mailto:daviddeblas@protonmail.com"> daviddeblas@protonmail.com</a>.
                    </p>
                </section>
            </main>

            <footer className="privacy-footer">
                <p>&copy; 2025 Subscription Manager. All rights reserved.</p>
            </footer>
        </div>
    );
}

export default PrivacyPolicy;
