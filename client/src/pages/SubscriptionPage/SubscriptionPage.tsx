import { useState, useEffect } from 'react';
import SubscriptionList from '../../component/SubscriptionList';
import './SubscriptionPage.css';
import {EmailData} from "../../interface/types.ts";


function SubscriptionPage() {
    const [emails, setEmails] = useState<EmailData[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [progress, setProgress] = useState<{ scanned: number; total: number } | null>(null);

    useEffect(() => {
        console.log('[CLIENT] SubscriptionPage mounted.');
        setIsLoading(true);

        // TODO
        // http://localhost:8080/api/emails
        // https://subscription-manager-wpds.onrender.com/api/emails
        const eventSource = new EventSource('https://subscription-manager-wpds.onrender.com/api/emails', {
            withCredentials: true
        });

        eventSource.onmessage = (event) => {
            console.log('[CLIENT] SSE onmessage => ', event.data);
            const data = JSON.parse(event.data);

            // Progress ?
            if (data.scanned && data.total) {
                setProgress({ scanned: data.scanned, total: data.total });
            }
            // Final emails ?
            else if (data.emails) {
                console.log('[CLIENT] Received final email list, closing SSE.');
                setEmails(data.emails);
                setProgress(null);
                eventSource.close();
            }

            else if (data.error) {
                console.error('[CLIENT] SSE error =>', data.error);
            }

            else if (data.info) {
                console.log('[CLIENT] SSE info =>', data.info);
            }
        };

        eventSource.onerror = (error) => {
            console.error('[CLIENT] SSE error =>', error);
            eventSource.close();
            setProgress(null);
        };

        return () => {
            console.log('[CLIENT] Unmounting SubscriptionPage, close SSE.');
            eventSource.close();
        };
    }, []);

    const renderProgress = () => {
        if (!progress) return null;
        const ratio = progress.total ? progress.scanned / progress.total : 0;
        const percentage = Math.min(100, ratio * 100).toFixed(1);

        return (
            <div style={{ textAlign: 'center' }}>
                <div className="spinner" style={{ margin: '1rem auto' }} />
                <p style={{ marginBottom: '0.5rem' }}>
                    Scanning {progress.scanned} / {progress.total} emails ({percentage}%)
                </p>
                <div className="progress-bar">
                    <div
                        className="progress-fill"
                        style={{ width: `${percentage}%` }}
                    />
                </div>
            </div>
        );
    };

    return (
        <div className="subscription-page-container">
            <div className="subscription-page-box">
                <h1 className="subscription-page-title">Subscription Manager</h1>

                <div className="status-bar">
                    {progress && renderProgress()}

                    {!progress && emails.length > 0 && (
                        <SubscriptionList emails={emails} />
                    )}

                    {!progress && emails.length === 0 && !isLoading && (
                        <p>No newsletters found (with unsubscribe link).</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default SubscriptionPage;
