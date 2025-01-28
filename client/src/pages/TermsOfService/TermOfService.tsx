import './TermOfService.css'

function TermsOfService() {
    return (
        <div className="tos-container">
            <header className="tos-header">
                <h1>Terms of Service</h1>
                <p>Last updated: January 28, 2025</p>
            </header>

            <main className="tos-main">
                <section className="tos-section">
                    <h2>1. Introduction</h2>
                    <p>
                        Welcome to Subscription Manager! These Terms of Service govern your use of our web
                        application located at <a href="https://subscription-manager-ten.vercel.app/" target="_blank" rel="noopener noreferrer">https://subscription-manager-ten.vercel.app/</a>. By accessing or using our Service, you agree to comply with and be bound by these Terms. If you do not agree with any part of these Terms, please do not use our Service.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>2. Use of the Service</h2>
                    <p>
                        You are granted a non-exclusive, non-transferable, revocable license to use the Service in
                        accordance with these Terms. The Service is intended for personal use to manage your Gmail
                        newsletter subscriptions.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>3. User Responsibilities</h2>
                    <ul>
                        <li><strong>Compliance:</strong> You agree to use the Service only for lawful purposes and in a manner that does not infringe the rights of others.</li>
                        <li><strong>Account Security:</strong> If applicable, you are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account.</li>
                    </ul>
                </section>

                <section className="tos-section">
                    <h2>4. Data Usage and Privacy</h2>
                    <p>
                        We respect your privacy. Please refer to our <a href="https://subscription-manager-ten.vercel.app/privacy-policy" target="_blank" rel="noopener noreferrer">Privacy Policy</a> for detailed information on how we handle your data.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>5. Intellectual Property</h2>
                    <p>
                        All content, trademarks, and data on this Service, including but not limited to software,
                        databases, text, graphics, icons, hyperlinks, private information, designs, and agreements,
                        are the property of <strong>Subscription Manager</strong> or its licensors.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>6. Disclaimer of Warranties</h2>
                    <p>
                        The Service is provided "as is" and "as available" without any warranties of any kind, either
                        express or implied. We do not guarantee that the Service will be uninterrupted, secure, or error-free.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>7. Limitation of Liability</h2>
                    <p>
                        In no event shall <strong>Subscription Manager</strong>, its developers, or contributors be
                        liable for any indirect, incidental, special, consequential, or punitive damages arising out
                        of your use of or inability to use the Service.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>8. Indemnification</h2>
                    <p>
                        You agree to indemnify, defend, and hold harmless <strong>Subscription Manager</strong>,
                        its developers, contributors, and affiliates from any claims, liabilities, damages, losses,
                        and expenses arising out of or in any way connected with your access to or use of the Service.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>9. Termination</h2>
                    <p>
                        We reserve the right to terminate or suspend your access to the Service immediately,
                        without prior notice or liability, for any reason whatsoever, including without limitation
                        if you breach the Terms.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>10. Governing Law</h2>
                    <p>
                        These Terms shall be governed and construed in accordance with the laws of the jurisdiction
                        in which the Service is operated, without regard to its conflict of law provisions.
                    </p>
                </section>

                <section className="tos-section">
                    <h2>11. Contact Us</h2>
                    <p>
                        If you have any questions about these Terms, please contact us at
                        <a href="mailto:daviddeblas@protonmail.com"> daviddeblas@protonmail.com</a>.
                    </p>
                </section>
            </main>

            <footer className="tos-footer">
                <p>&copy; 2025 Subscription Manager. All rights reserved.</p>
            </footer>
        </div>
    );

}

export default TermsOfService;
