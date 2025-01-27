import './SubscriptionList.css';
import {EmailData} from "../interface/types.ts";

interface SubscriptionListProps {
    emails: EmailData[];
}

function SubscriptionList({ emails }: SubscriptionListProps) {
    if (!emails || emails.length === 0) {
        return <p>No subscriptions found.</p>;
    }

    return (
        <div className="subscription-list-container">
            <h2>My Subscriptions</h2>
            <ul className="subscription-list-ul">
                {emails.map((sub, idx) => (
                    <li key={idx} className="subscription-item">
                        <div className="subscription-brand">{sub.brandName}</div>
                        <div className="subscription-from">&lt;{sub.fromAddress}&gt;</div>
                        {sub.subject && (
                            <div className="subscription-subject">{sub.subject}</div>
                        )}
                        <button
                            className="unsubscribe-btn"
                            onClick={() => window.open(sub.unsubscribeLink, '_blank')}
                        >
                            Unsubscribe
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default SubscriptionList;
